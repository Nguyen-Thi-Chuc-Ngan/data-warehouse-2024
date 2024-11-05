package com.example.demo.services;

import com.example.demo.dao.LogDAO;
import com.example.demo.entities.Config;
import com.example.demo.entities.Log;
import com.example.demo.entities.LogLevel;
import com.example.demo.entities.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogDAO logDAO;
    // Lấy danh sách Config theo trạng thái log trong ngày hôm nay
    public List<Config> getConfigsByLogStatusToday(String logStatus) {
        return logDAO.getConfigsByLogStatusToday(logStatus);
    }

    // Lấy danh sách trạng thái 'PROCESSING' trong ngày hôm nay
    public List<Config> getProcessingStatusesToday() {
        return logDAO.getConfigsByLogStatusToday("PROCESSING");
    }

    // Lấy danh sách trạng thái 'PROCESSING' trong ngày hôm nay
    public List<Config> getReadyStatusesToday() {
        return logDAO.getConfigsByLogStatusToday("READY_EXTRACT");
    }

    // Lấy danh sách trạng thái 'READY_EXTRACT' trong ngày hôm nay
    public Log getConfigLogByStatusToday(int id) {
        // Gọi phương thức trong logDAO để lấy cấu hình với trạng thái 'READY_EXTRACT' và id được chỉ định
        return logDAO.getConfigLogByStatusToday("READY_EXTRACT", id);
    }

    // Kiểm tra xem có config nào đang ở trạng thái PROCESSING hay không
    public boolean isConfigRunning() {
        return logDAO.isConfigRunning();
    }


    // Phương thức ghi log cho sự kiện crawl
    public void logCrawlEvent(int idConfig, LogLevel logLevel, String destinationPath, Status status, String message, String stackTrace, int count, LocalDateTime time) {
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
        log.setUpdateTime(time);
        log.setCreatedBy("Admin");

        // Lưu log vào cơ sở dữ liệu
        logDAO.saveLog(log);
    }

    // Lấy danh sách các log theo trạng thái
    public List<Log> getLogsByStatus(String status) {
        return logDAO.getLogsByStatus(status);
    }

    // Cập nhật trạng thái của log
    public void updateLog(Log log) {
        logDAO.updateLog(log);
    }

    // Lấy log mới nhất theo configId
    public Log getLatestLogByConfigId(int configId) {
        return logDAO.getLatestLogByConfigId(configId);
    }

    public boolean isLogTodayExists() {
        return logDAO.isLogTodayExists();
    }

    public boolean isTodayLogExistsForConfig(int configId) {
        return logDAO.isTodayLogExistsForConfig(configId);
    }
    public void saveLog(Log log) {
        logDAO.saveLog(log);
    }

}
