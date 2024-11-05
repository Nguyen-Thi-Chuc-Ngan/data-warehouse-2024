package com.example.demo;

import com.example.demo.configuration.StagingDatabaseConnection;
import com.example.demo.services.ConfigService;
import com.example.demo.services.LogService;
import com.example.demo.services.emailService.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class LoadDataIntoStagingApplication implements CommandLineRunner{

	private final StagingDatabaseConnection stagingDatabaseConnection;
	private final LogService logService;
	private final EmailServiceImpl emailService;
	private final ConfigService configService;

	@Autowired
	public LoadDataIntoStagingApplication(StagingDatabaseConnection stagingDatabaseConnection, LogService logService,
										  EmailServiceImpl emailService, ConfigService configService) {
		this.stagingDatabaseConnection = stagingDatabaseConnection;
		this.logService = logService;
		this.emailService = emailService;
		this.configService = configService;
	}

	public static void main(String[] args) {
		SpringApplication.run(LoadDataIntoStagingApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		int configId = 1; // Bạn có thể thay đổi configId theo cấu hình của bạn

		try {
			// Kết nối đến cơ sở dữ liệu Staging
			Connection connection = stagingDatabaseConnection.connectToStagingDatabase(configId);
			if (connection != null) {
				System.out.println("Kết nối đến cơ sở dữ liệu Staging thành công!");
				// Đóng kết nối khi không còn sử dụng nữa
				connection.close();
			}
		} catch (SQLException e) {
			System.err.println("Lỗi khi kết nối đến cơ sở dữ liệu Staging: " + e.getMessage());
		}


	}

}
