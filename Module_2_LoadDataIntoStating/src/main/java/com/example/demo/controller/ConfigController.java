package com.example.demo.controller;

import com.example.demo.dto.ConfigDTO;
import com.example.demo.entities.Config;
import com.example.demo.services.ConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/configs")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping
    public ResponseEntity<List<Config>> findAll() throws Exception {
        return new ResponseEntity<>(configService.getAllConfigs(), HttpStatus.OK);
    }

    // Lưu một Config
    @PostMapping
    public ResponseEntity<Config> save(@RequestBody Config config) {
        try {
            Config savedConfig = configService.save(config);
            return new ResponseEntity<>(savedConfig, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Kiểm tra xem có config nào đang ở trạng thái PROCESSING hay không
    @GetMapping("/running")
    public ResponseEntity<Optional<Config>> getConfigRunning() {
        try {
            Optional<Config> runningConfig = configService.getConfigRunning();
            return new ResponseEntity<>(runningConfig, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Trả về config đầu tiên có trạng thái READY_EXTRACT
    @GetMapping("/ready")
    public ResponseEntity<Optional<Config>> getReadyConfig() {
        try {
            Optional<Config> readyConfig = configService.getReadyConfig();
            return new ResponseEntity<>(readyConfig, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Kiểm tra điều kiện crawl
    @PostMapping("/crawl-allowed")
    public ResponseEntity<Boolean> isCrawlAllowed(@RequestBody Config config) {
        try {
            boolean isAllowed = configService.isCrawlAllowed(config);
            return new ResponseEntity<>(isAllowed, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Lấy cấu hình theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ConfigDTO> getConfigById(@PathVariable int id) {
        try {
            ConfigDTO config = configService.getConfigById(id);
            return new ResponseEntity<>(config, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint để lấy cột 'columns' theo ID
    @GetMapping("/{id}/columns")
    public ResponseEntity<String> getColumnsById(@PathVariable int id) {
        Optional<String> columns = configService.getColumnsById(id);
        return columns.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cập nhật cấu hình
    @PutMapping("/{id}")
    public ResponseEntity<Config> updateConfig(@PathVariable int id, @RequestBody Config config) {
        try {
            config.setId(id); // Gán ID cho config
            Config updatedConfig = configService.updateConfig(config);
            return new ResponseEntity<>(updatedConfig, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
