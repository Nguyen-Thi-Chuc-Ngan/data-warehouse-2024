package com.example.demo.dao;

import com.example.demo.model.Config;
import com.example.demo.model.Status;
import com.example.demo.repository.ConfigRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ConfigDAO {
    private final ConfigRepository configRepository;

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

    @Transactional
    public Config updateConfig(Config config) {
        if (config.getId() > 0 && configRepository.existsById(config.getId())) {
            return configRepository.save(config);
        } else {
            throw new IllegalArgumentException("Config không tồn tại hoặc ID không hợp lệ.");
        }
    }

}
