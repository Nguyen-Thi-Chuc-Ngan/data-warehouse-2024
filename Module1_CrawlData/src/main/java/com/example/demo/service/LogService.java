package com.example.demo.service;

import com.example.demo.model.Log;
import com.example.demo.model.LogLevel;
import com.example.demo.model.Status;
import com.example.demo.repository.LogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogService {
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public Log save(Log log) {
        return logRepository.save(log);
    }

    // Phương thức ghi log cho sự kiện crawl
    public void logCrawlEvent(int idConfig, LogLevel logLevel, Status status, String message, String stackTrace, int count, long time) {
        Log log = new Log();
        log.setIdConfig(idConfig);
        log.setLogLevel(logLevel);
        log.setCount(count);
        log.setStackTrace(stackTrace);
        log.setLocation("Crawl Data");
        log.setCreateTime(LocalDateTime.now());
        log.setErrorMessage(message);
        log.setStatus(status);
        log.setTime(time);
        log.setCreatedBy("Admin");

        // Lưu log vào cơ sở dữ liệu
        save(log);
    }
}
