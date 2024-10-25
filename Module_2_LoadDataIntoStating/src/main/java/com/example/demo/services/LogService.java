package com.example.demo.services;

import com.example.demo.dao.LogDAO;
import com.example.demo.entities.Log;
import com.example.demo.entities.LogLevel;
import com.example.demo.entities.Status;
import com.example.demo.repository.LogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {
    private final LogRepository logRepository;
    private final LogDAO logDAO;
    public LogService(LogRepository logRepository, LogDAO logDAO) {
        this.logRepository = logRepository;
        this.logDAO = logDAO;
    }

    public Log save(Log log) {
        return logRepository.save(log);
    }

    // Phương thức ghi log cho sự kiện crawl
    public void logCrawlEvent(int idConfig, LogLevel logLevel, String destinationPath, Status status, String message, String stackTrace, int count, long time) {
        Log log = new Log();
        log.setIdConfig(idConfig);
        log.setLogLevel(logLevel);
        log.setDestinationPath(destinationPath);
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

    public List<Log> findLogsByStatusAndDate(){
        return logDAO.findLogsByStatusAndDate();
    }
}
