package com.example.demo.dao;

import com.example.demo.dto.ConfigDTO;
import com.example.demo.entities.Config;
import com.example.demo.entities.Status;
import com.example.demo.repository.ConfigRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ConfigDAO {
    private final ConfigRepository configRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ConfigDAO(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    // Trả về config đầu tiên có trạng thái READY_EXTRACT
    public Optional<Config> getReadyConfig() {
        return configRepository.findAll().stream()
                .filter(config -> config.getStatus().equals(Status.READY_EXTRACT))
                .findFirst();
    }

    // Kiểm tra xem có config nào đang ở trạng thái PROCESSING hay không
    public Optional<Config> getConfigRunning() {
        return configRepository.findAll().stream()
                .filter(config -> config.getStatus().equals(Status.PROCESSING))
                .findFirst();
    }

    // Lưu Config
    public Config save(Config config) {
        return configRepository.save(config);
    }

    // Lấy tất cả các Config
    public List<Config> getAllConfigs() {
        return configRepository.findAll();
    }

    // Phương thức kiểm tra điều kiện crawl
    public boolean isCrawlAllowed(Config config) {
        // Kiểm tra xem đã đến thời điểm crawl theo tần suất
        LocalDateTime now = LocalDateTime.now();
        return config.getLastCrawlTime() == null ||
                config.getLastCrawlTime().plusMinutes(config.getCrawlFrequency()).isBefore(now);
    }

    // Phương thức lấy cấu hình dựa trên ID
    public ConfigDTO getConfigById(int id) {
        String sql = "SELECT STAGING_source_username AS username, STAGING_source_password AS password, " +
                "STAGING_source_host AS host, STAGING_source_port AS port " +
                "FROM config WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            // Khởi tạo và trả về đối tượng DTO
            return new ConfigDTO(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("host"),
                    rs.getInt("port")
            );
        });
    }

    public Optional<String> getColumnsById(int id) {
        String sql = "SELECT columns FROM config WHERE id = ?";
        // Sử dụng query để lấy kết quả
        List<String> result = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> rs.getString("columns"));
        // Trả về Optional
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
    @Transactional
    public Config updateConfig(Config config) {
        if (config.getId() > 0 && configRepository.existsById(config.getId())) {
            return configRepository.save(config);
        } else {
            throw new IllegalArgumentException("Config không tồn tại hoặc ID không hợp lệ.");
        }
    }

}
