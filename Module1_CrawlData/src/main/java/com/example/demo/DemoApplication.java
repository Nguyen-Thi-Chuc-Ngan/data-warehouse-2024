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
		List<Config> configs = configService.getAllConfigs();

		Optional<Config> runningConfigOptional = configService.getConfigRunning();

		// Nếu có config đang chạy, gửi email thông báo và dừng lại
		if (runningConfigOptional.isPresent()) {
			Config runningConfig = runningConfigOptional.get();
			String message = "Có một config đang chạy: " + runningConfig.getId() + ". Không bắt đầu crawl.";
			logService.logCrawlEvent(runningConfig.getId(), LogLevel.WARNING, Status.PROCESSING, message, "", 0, 0);
			emailService.sendFailureEmail(runningConfig.getNotificationEmails(), message);
			return; // Dừng lại nếu có config đang chạy
		}

		List<Future<?>> futures = new ArrayList<>();

		configs.stream()
				.filter(Config::isActive) // Chỉ chọn config đang hoạt động
				.forEach(config -> {
					Future<?> future = executorService.submit(() -> {
						runCrawlForConfig(config, csvReader);
					});
					futures.add(future);
				});

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

	private void runCrawlForConfig(Config readyConfig, CsvReader csvReader) {
		if (!readyConfig.getStatus().equals(Status.READY_EXTRACT)) {
			System.err.println("Config " + readyConfig.getId() + " không ở trạng thái sẵn sàng. Bỏ qua crawl.");
			return; // Nếu không ở trạng thái sẵn sàng, dừng lại
		}

		// Cập nhật trạng thái thành "đang chạy" và ghi log
		readyConfig.setStatus(Status.PROCESSING);
		configService.updateConfig(readyConfig);
		logService.logCrawlEvent(readyConfig.getId(), LogLevel.INFO, Status.PROCESSING,
				"Bắt đầu crawl với config.", "", 0, 0);
    
		try {
			System.out.println("Bắt đầu crawl với config: " + readyConfig.getId());
			String currentDirectory = readyConfig.getDestinationPath();
			String csvFilePath = currentDirectory + FileSystems.getDefault().getSeparator()
					+ readyConfig.getFilePath() + FileSystems.getDefault().getSeparator()
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

			boolean crawlSuccess = false;
			int retryAttempts = 0;
			List<Product> products = List.of();
			String outputCsvFilePath = "";

			// Bắt đầu tính toán thời gian crawl
			long startTime = System.currentTimeMillis();

			while (!crawlSuccess && retryAttempts < readyConfig.getRetryCount()) {
				try {
					products = crawlService.crawlProducts(limitedProductIds);
					readyConfig.setLastCrawlTime(LocalDateTime.now());
					readyConfig.setStatus(Status.SUCCESS_EXTRACT);
					configService.updateConfig(readyConfig);

					// Ghi kết quả vào tệp CSV
					CsvWriter csvWriter = new CsvWriter();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
					String timestamp = dateFormat.format(new Date());

					String tempDirPath = currentDirectory + FileSystems.getDefault().getSeparator()
							+ readyConfig.getFilePath() + "_temporary";

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

						// Kiểm tra tính hợp lệ của các sản phẩm
						for (Product product : products) {
							try {
								product.validate();
							} catch (IllegalArgumentException e) {
								System.err.println("Sản phẩm không hợp lệ: " + e.getMessage());
								logService.logCrawlEvent(readyConfig.getId(), LogLevel.ERROR, Status.FAILURE_EXTRACT,
										"Sản phẩm không hợp lệ: " + product.getId() + " - " + e.getMessage(), "", 1, 0);
								return; // Dừng lại nếu có sản phẩm không hợp lệ
							}
						}

						// Nếu hợp lệ, chuyển file tạm thành file chính
						outputCsvFilePath = currentDirectory + FileSystems.getDefault().getSeparator()
								+ readyConfig.getFilePath() + FileSystems.getDefault().getSeparator()
								+ readyConfig.getFileName() + "_" + timestamp + ".csv";

						Files.move(tempFilePath, Paths.get(outputCsvFilePath), StandardCopyOption.REPLACE_EXISTING);

						System.out.println("File tạm đã chuyển thành file chính: " + outputCsvFilePath);
						emailService.sendSuccessEmail(readyConfig.getNotificationEmails(), outputCsvFilePath, products.size(), LocalDateTime.now());
						System.out.println("Crawl thành công!");

						crawlSuccess = true;
					}
					 else {
						retryAttempts++;
						System.err.println("Không thể tìm thấy file tạm: " + tempCsvFilePath);
					}
				} catch (Exception e) {
					retryAttempts++;
					String message = "Có lỗi xảy ra khi crawl config " + readyConfig.getId() + ": " + e.getMessage() +
							" (Thử lại lần " + retryAttempts + ")";
					handleError(readyConfig, message, e);
				}
			}

			if (crawlSuccess) {
				long endTime = System.currentTimeMillis();
				long duration = endTime - startTime;
				logService.logCrawlEvent(readyConfig.getId(), LogLevel.INFO, Status.SUCCESS_EXTRACT,
						"Crawl hoàn thành trong " + duration + " ms", "", products.size(), 0);
			} else {
				String message = "Crawl không thành công sau " + retryAttempts + " lần thử.";
				handleError(readyConfig, message, new Exception("Crawl không thành công"));
			}
		} catch (Exception e) {
			handleError(readyConfig, "Lỗi không xác định khi chạy crawl với config " + readyConfig.getId(), e);
		}
	}

	// Phương thức xử lý lỗi để giảm thiểu trùng lặp mã
	private void handleError(Config readyConfig, String message, Exception e) {
		readyConfig.setStatus(Status.FAILURE_EXTRACT);
		configService.updateConfig(readyConfig);
		String stackTrace = Arrays.toString(e.getStackTrace());
		emailService.sendFailureEmail(readyConfig.getNotificationEmails(), message);
		logService.logCrawlEvent(readyConfig.getId(), LogLevel.ERROR, Status.FAILURE_EXTRACT, message, stackTrace, 1, 0);
		System.err.println(message);
	}
}
