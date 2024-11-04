package com.example.demo.service;

import com.example.demo.dao.ConfigDAO;
import com.example.demo.model.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigService {
    @Autowired
    private ConfigDAO configDAO;

    // Tạo mới một Config
    public Config createConfig(Config config) {
        return configDAO.save(config);
    }

    // Cập nhật một Config
    public Config updateConfig(Config config) {
        return configDAO.updateConfig(config);
    }

    // Lấy tất cả các Config
    public List<Config> getAllConfigs() {
        return configDAO.getAllConfigs();
    }

    public List<Config> getActiveConfigs(){
        return configDAO.getActiveConfigs();
    }


}
