package com.example.demo;

import com.example.demo.dao.ConfigDAO;
import com.example.demo.model.*;
import com.example.demo.service.ConfigService;
import com.example.demo.service.LogService;
import com.example.demo.service.crawler.CrawlService;
import com.example.demo.service.emailService.EmailServiceImpl;
import com.example.demo.service.emailService.IEmailService;
import com.example.demo.utils.CsvReader;
import com.example.demo.utils.CsvWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private CrawlService crawlService;
	private final EmailServiceImpl emailService;
	private final ConfigService configService;
	private final LogService logService;

	@Autowired
	private ExecutorService executorService;

	@Autowired
	public DemoApplication(EmailServiceImpl emailService, ConfigService configService, LogService logService) {
		this.emailService = emailService;
		this.configService = configService;
		this.logService = logService;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		CsvReader csvReader = new CsvReader();

		if (checkAndNotifyRunningConfigs()) return;

		// Lấy danh sách cấu hình đang hoạt động
		List<Config> readyConfigs = logService.getReadyStatusesToday(); // Lấy các config có trạng thái READY_EXTRACT

		if (readyConfigs.isEmpty()) {
			System.out.println("Không có cấu hình nào ở trạng thái READY_EXTRACT.");
			return; // Không có config nào để chạy
		}
		List<Future<?>> futures = new ArrayList<>();

		// Lấy danh sách cấu hình đang hoạt động
		List<Config> activeConfigs = configService.getActiveConfigs();

		// Chạy crawl cho từng cấu hình đang hoạt động
		for (Config config : activeConfigs) {
			Future<?> future = executorService.submit(() -> {
				runCrawlForConfig(config, csvReader);
			});
			futures.add(future);
		}

		// Chờ cho tất cả các công việc hoàn thành
		for (Future<?> future : futures) {
			try {
				future.get(); // Chờ cho công việc hoàn thành
			} catch (Exception e) {
				System.err.println("Có lỗi xảy ra khi chạy config: " + e.getMessage());
			}
		}
		executorService.shutdown();
		try {
			if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				executorService.shutdownNow();
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
		}
	}

	private boolean checkAndNotifyRunningConfigs() {
		boolean isRunning = logService.isConfigRunning();

		if (isRunning) {
			// Lấy danh sách các Config có trạng thái 'PROCESSING' trong ngày hôm nay
			List<Config> runningConfigs = logService.getProcessingStatusesToday();
			System.out.println("Running configs count: " + runningConfigs.size());
			if (!runningConfigs.isEmpty()) {
				String ids = runningConfigs.stream()
						.map(Config::getId)
						.map(String::valueOf)
						.collect(Collectors.joining(", "));
				String message = "Có các config đang chạy: " + ids + ". Không bắt đầu crawl.";
				System.out.println(message);
				// Log và gửi email thông báo
				runningConfigs.forEach(config -> {
					emailService.sendFailureEmail(config.getNotificationEmails(), message);
				});

				return true; // Có config đang chạy, dừng lại
			}
		}
		System.out.println("Không có config nào đang chạy");
		return false;
	}

	private List<String> getLimitedProductIds(Config readyConfig, CsvReader csvReader) {
		String currentDirectory = readyConfig.getFilePath();
		String csvFilePath = currentDirectory + FileSystems.getDefault().getSeparator()
				+ readyConfig.getDestinationPath() + FileSystems.getDefault().getSeparator()
				+ "products_id.csv";
		List<String> productIds = csvReader.readProductIdsFromCsv(csvFilePath);
		List<String> limitedProductIds;
		int dataSize = readyConfig.getDataSize();

		// Nếu danh sách có nhiều hơn số lượng được cấu hình, trộn và lấy số sản phẩm theo dataSize từ readyConfig.
		if (productIds.size() > dataSize) {
			Collections.shuffle(productIds); // Trộn danh sách sản phẩm ngẫu nhiên
			limitedProductIds = productIds.subList(0, dataSize); // Lấy số lượng sản phẩm dựa trên dataSize
		} else {
			limitedProductIds = productIds; // Nếu danh sách có ít hơn hoặc bằng dataSize, lấy toàn bộ danh sách
		}

		return limitedProductIds;
	}

	private void runCrawlForConfig(Config readyConfig, CsvReader csvReader) {
		// Kiểm tra nếu cấu hình là null
		if (readyConfig == null) {
			System.err.println("Cấu hình không hợp lệ. Bỏ qua crawl.");
			return; // Nếu cấu hình không hợp lệ, dừng lại
		}

		Log log = logService.getConfigLogByStatusToday(readyConfig.getId());
		if (log != null) {
			System.out.println("Cấu hình " + readyConfig.getId() + " đã sẵn sàng để crawl.");
			log.setStatus(Status.PROCESSING);
			log.setUpdateTime(LocalDateTime.now());
			logService.updateLog(log);

			List<String> limitedProductIds = getLimitedProductIds(readyConfig, csvReader);
			boolean crawlSuccess = executeCrawl(readyConfig, limitedProductIds, log);
			if (!crawlSuccess) {
				// Xử lý lỗi khi crawl thất bại
				handleCrawlFailure(readyConfig);
			}

		} else {
			String message = "Config " + readyConfig.getId() + " không ở trạng thái READY_EXTRACT. Bỏ qua crawl.";
			// Gửi email thông báo
			emailService.sendFailureEmail(readyConfig.getNotificationEmails(), message);
		}

	}

	private void handleCrawlFailure(Config readyConfig) {

	}

	private boolean executeCrawl(Config readyConfig, List<String> limitedProductIds, Log log) {
		boolean crawlSuccess = false;
		int retryAttempts = 0;

		// Bắt đầu tính toán thời gian crawl
		long startTime = System.currentTimeMillis();

		while (!crawlSuccess && retryAttempts < readyConfig.getRetryCount()) {
			try {
				// Gọi dịch vụ crawl để lấy sản phẩm
				System.out.println("Đang tiến hành crawl...");
				List<Product> products = crawlService.crawlProducts(limitedProductIds, readyConfig.getSourcePath());

				// Ghi kết quả vào tệp CSV
				CsvWriter csvWriter = new CsvWriter();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
				String timestamp = dateFormat.format(new Date());
				String currentDirectory = readyConfig.getFilePath();
				String tempDirPath = currentDirectory + FileSystems.getDefault().getSeparator()
							+ readyConfig.getBackupPath();

				Path tempDir = Paths.get(tempDirPath);

				// Kiểm tra nếu thư mục tạm chưa tồn tại thì tạo mới
				if (!Files.exists(tempDir)) {
					Files.createDirectories(tempDir);
					System.out.println("Thư mục tạm đã được tạo: " + tempDirPath);
				}

				// Ghi vào file tạm
				String tempCsvFilePath = tempDirPath + FileSystems.getDefault().getSeparator()
						+ "temp_" + readyConfig.getFileName() + "_" + timestamp + ".csv";

				csvWriter.writeProductsToCsv(products, tempCsvFilePath);

				// Kiểm tra xem file tạm có tồn tại và hợp lệ không
				Path tempFilePath = Paths.get(tempCsvFilePath);
				if (Files.exists(tempFilePath)) {
					System.out.println("Tệp tạm tạo thành công: " + tempCsvFilePath);

					// Nếu hợp lệ, chuyển file tạm thành file chính
					String outputCsvFilePath = currentDirectory + FileSystems.getDefault().getSeparator()
							+ readyConfig.getDestinationPath() + FileSystems.getDefault().getSeparator()
							+ readyConfig.getFileName() + "_" + timestamp + ".csv";

					Files.move(tempFilePath, Paths.get(outputCsvFilePath), StandardCopyOption.REPLACE_EXISTING);

					System.out.println("File tạm đã chuyển thành file chính: " + outputCsvFilePath);
					// Ghi log thông báo crawl thành công
					long endTime = System.currentTimeMillis();
					long duration = endTime - startTime;
					String successMessage = "Crawl thành công cho cấu hình " + readyConfig.getId() + " trong " + duration + " ms.";
					log.setErrorMessage(successMessage);
					log.setDestinationPath(outputCsvFilePath);
					log.setCount(products.size());
					log.setUpdateTime(LocalDateTime.now());
					log.setStatus(Status.SUCCESS_EXTRACT);
					logService.updateLog(log);

					emailService.sendSuccessEmail(readyConfig.getNotificationEmails(), outputCsvFilePath, products.size(), LocalDateTime.now());
					System.out.println("Crawl thành công!");
					crawlSuccess = true;
				}
				else {
					retryAttempts++;
					System.err.println("Không thể tìm thấy file tạm: " + tempCsvFilePath);
				}

			} catch (InterruptedException e) {
//				Xử lý lỗi InterruptedException
				String errorMessage = "Crawl bị gián đoạn: " + e.getMessage();
				System.err.println(errorMessage);
				handleError(readyConfig, log, errorMessage, e);
				break; // Dừng lại nếu bị gián đoạn

			} catch (JsonProcessingException e) {
				// Xử lý lỗi JsonProcessingException
				String errorMessage = "Lỗi trong quá trình xử lý JSON: " + e.getMessage();
				System.err.println(errorMessage);
				handleError(readyConfig, log, errorMessage, e);

			} catch (Exception e) {
				// Xử lý các lỗi khác
				String errorMessage = "Có lỗi xảy ra khi crawl: " + e.getMessage();
				System.err.println(errorMessage);
				handleError(readyConfig, log, errorMessage, e);
			}

			// Tăng số lần thử
			retryAttempts++;

			// Có thể thêm thời gian chờ ở đây giữa các lần thử
			try {
				Thread.sleep(readyConfig.getCrawlFrequency() * 60 * 1000);  // Chờ 2 giây trước khi thử lại
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // Khôi phục trạng thái gián đoạn
			}
		}

		return crawlSuccess;
	}

	// Phương thức xử lý lỗi để giảm thiểu trùng lặp mã
	private void handleError(Config readyConfig, Log log, String message, Exception e) {
		String stackTrace = Arrays.toString(e.getStackTrace());
		log.setLogLevel(LogLevel.ERROR);
		log.setStackTrace(stackTrace);
		log.setStatus(Status.FAILURE_EXTRACT);
		log.setErrorMessage(message);
		log.setUpdateTime(LocalDateTime.now());
		logService.updateLog(log);
		emailService.sendFailureEmail(readyConfig.getNotificationEmails(), message);
		System.err.println(message);
	}
}
