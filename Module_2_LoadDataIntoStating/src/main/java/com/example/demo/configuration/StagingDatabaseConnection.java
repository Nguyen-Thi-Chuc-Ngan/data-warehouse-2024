package com.example.demo.configuration;


import com.example.demo.dto.ConfigDTO;
import com.example.demo.services.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class StagingDatabaseConnection {
    private final JdbcTemplate jdbcTemplate;
    private final ConfigService configService;
    @Autowired
    public StagingDatabaseConnection(JdbcTemplate jdbcTemplate, ConfigService configService) {
        this.jdbcTemplate = jdbcTemplate;
        this.configService = configService;
    }

    // Phương thức kết nối đến cơ sở dữ liệu Staging
    public Connection connectToStagingDatabase(int configId) throws SQLException {
        ConfigDTO config = configService.getConfigById(configId); // Lấy cấu hình từ database

        String url = String.format("jdbc:mysql://%s:%d/staging", config.getHost(), config.getPort()); // Thay đổi url nếu cần
        Connection connection = DriverManager.getConnection(url, config.getUsername(), config.getPassword());

        return connection;
    }


}
