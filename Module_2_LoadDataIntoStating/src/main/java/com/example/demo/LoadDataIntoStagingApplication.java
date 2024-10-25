package com.example.demo;

import com.example.demo.configuration.StagingDatabaseConnection;
import com.example.demo.entities.Config;
import com.example.demo.entities.Log;
import com.example.demo.entities.LogLevel;
import com.example.demo.entities.Status;
import com.example.demo.services.ConfigService;
import com.example.demo.services.LogService;
import com.example.demo.services.ProductServiceImpl;
import com.example.demo.services.emailService.EmailServiceImpl;
import com.example.demo.utils.CsvValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class LoadDataIntoStagingApplication implements CommandLineRunner{

	private final StagingDatabaseConnection stagingDatabaseConnection;
	private final LogService logService;
	private final EmailServiceImpl emailService;
	private final ConfigService configService;
	private final ProductServiceImpl productService;

	@Autowired
	public LoadDataIntoStagingApplication(StagingDatabaseConnection stagingDatabaseConnection, LogService logService,
										  EmailServiceImpl emailService, ConfigService configService,
										  ProductServiceImpl productService) {
		this.stagingDatabaseConnection = stagingDatabaseConnection;
		this.logService = logService;
		this.emailService = emailService;
		this.configService = configService;
		this.productService = productService;
	}

	public static void main(String[] args) {
		SpringApplication.run(LoadDataIntoStagingApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		Optional<Config> runningConfigOptional = configService.getConfigRunning();

		// Nếu có config đang chạy, gửi email thông báo và dừng lại
		if (runningConfigOptional.isPresent()) {
			Config runningConfig = runningConfigOptional.get();
			String message = "Có một config đang chạy: " + runningConfig.getId() + ". Không bắt đầu crawl.";
			logService.logCrawlEvent(runningConfig.getId(), LogLevel.WARNING,"Không có", Status.PROCESSING, message, "", 0, 0);
			emailService.sendFailureEmail(runningConfig.getNotificationEmails(), message);
			return; // Dừng lại nếu có config đang chạy
		}


		List<Log> logs = logService.findLogsByStatusAndDate();
		// Kiểm tra xem có log nào trùng ngày không
		if (!logs.isEmpty()) {
			productService.truncateProductStagingTable();
			System.out.println("Có log với ngày hiện tại và status là SUCCESS_EXTRACT. Tiến hành tiếp!" + logs);
			for (Log log : logs) {
				// Lấy đường dẫn từ từng log
				int idConfig = log.getIdConfig();
				String destinationPath = log.getDestinationPath();
				System.out.println("Đường dẫn đích từ log: " + destinationPath);

				// Kiểm tra tính hợp lệ của file CSV
				CsvValidator csvValidator = new CsvValidator();
				boolean isValid = csvValidator.isCsvFileValid(destinationPath);

				if (isValid) {
					System.out.println("File CSV hợp lệ: " + destinationPath);
					String columns = configService.getColumnsById(idConfig).orElse("");
					productService.createProductStagingTable();
					productService.loadCsvDataIntoStaging(destinationPath, columns);

				} else {
					System.out.println("File CSV không hợp lệ: " + destinationPath);
					// Xử lý khi file CSV không hợp lệ
				}
			}
		} else {
			System.out.println("Không có log nào trùng với ngày hiện tại. Dừng lại.");
		}

	}

}
