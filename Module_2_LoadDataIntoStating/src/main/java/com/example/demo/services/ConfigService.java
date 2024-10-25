package com.example.demo.services;

import com.example.demo.dao.ConfigDAO;
import com.example.demo.dto.ConfigDTO;
import com.example.demo.entities.Config;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigService {

    private final ConfigDAO configDAO;
    public ConfigService(ConfigDAO configDAO) {
        this.configDAO = configDAO;
    }
    // Lưu một Config
    public Config save(Config config) {
        return configDAO.save(config);
    }

    // Lấy tất cả các Config
    public List<Config> getAllConfigs() {
        return configDAO.getAllConfigs();
    }

    // Kiểm tra xem có config nào đang ở trạng thái PROCESSING hay không
    public  Optional<Config> getConfigRunning() {
        return configDAO.getConfigRunning();
    }

    // Trả về config đầu tiên có trạng thái READY_EXTRACT nếu có
    public Optional<Config> getReadyConfig() {
        return configDAO.getReadyConfig();
    }

    public boolean isCrawlAllowed(Config config) {
        return configDAO.isCrawlAllowed(config);
    }

    // Lấy cấu hình theo ID
    public ConfigDTO getConfigById(int id) {
        return configDAO.getConfigById(id);
    }

    // Phương thức lấy cột 'columns' theo ID
    public Optional<String> getColumnsById(int id) {
        return configDAO.getColumnsById(id);
    }

    public Config updateConfig(Config config) {
        return configDAO.updateConfig(config);
    }

}
