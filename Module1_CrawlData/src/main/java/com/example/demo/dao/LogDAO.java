package com.example.demo.dao;

import com.example.demo.model.Config;
import com.example.demo.model.Log;
import com.example.demo.model.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class LogDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveLog(Log log) {
        String sql = """
            INSERT INTO logs (id_config, log_level, destination_path, count, 
                              location, update_time, error_message, stack_trace, 
                              status, created_by)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        entityManager.createNativeQuery(sql)
                .setParameter(1, log.getIdConfig())
                .setParameter(2, log.getLogLevel().name()) // Chuyển đổi enum thành String
                .setParameter(3, log.getDestinationPath())
                .setParameter(4, log.getCount())
                .setParameter(5, log.getLocation())
                .setParameter(6, log.getUpdateTime())
                .setParameter(7, log.getErrorMessage())
                .setParameter(8, log.getStackTrace())
                .setParameter(9, log.getStatus().name()) // Chuyển đổi enum thành String
                .setParameter(10, log.getCreatedBy())
                .executeUpdate(); // Thực hiện câu lệnh INSERT
    }

    // Kiểm tra có log nào trong ngày hôm nay không
    public boolean isLogTodayExists() {
        String sql = """
            SELECT COUNT(*) 
            FROM logs 
            WHERE DATE(create_time) = CURDATE()
        """;

        Long count = (Long) entityManager.createNativeQuery(sql)
                .getSingleResult();

        return count > 0;
    }

    // Kiểm tra xem có log nào trong ngày hôm nay cho configId không
    public boolean isTodayLogExistsForConfig(int configId) {
        String sql = """
            SELECT COUNT(*) 
            FROM logs 
            WHERE id_config = ?1 AND DATE(create_time) = CURDATE()
        """;

        Long count = (Long) entityManager.createNativeQuery(sql)
                .setParameter(1, configId)
                .getSingleResult();

        return count > 0;
    }


    // Trả về danh sách Config theo trạng thái log trong ngày hôm nay
    public List<Config> getConfigsByLogStatusToday(String logStatus) {
        String sql = """
        SELECT c.*
        FROM config c
        JOIN logs l ON c.id = l.id_config
        WHERE l.status = ?1 AND DATE(l.create_time) = CURDATE()
    """;
        return entityManager.createNativeQuery(sql, Config.class)
                .setParameter(1, logStatus)
                .getResultList();
    }

    public Log getConfigLogByStatusToday(String logStatus, int configId) {
        String sql = """
    SELECT l.*
    FROM logs l
    JOIN config c ON c.id = l.id_config
    WHERE l.status = ?1 AND DATE(l.create_time) = CURDATE() AND c.id = ?2
    LIMIT 1
    """;

        try {
            // Trả về một log đầu tiên phù hợp
            return (Log) entityManager.createNativeQuery(sql, Log.class)
                    .setParameter(1, logStatus) // Trạng thái log
                    .setParameter(2, configId)   // ID của cấu hình
                    .getSingleResult(); // Trả về một kết quả duy nhất
        } catch (NoResultException e) {
            return null; // Nếu không có kết quả nào, trả về null
        }
    }

    // Kiểm tra xem có config nào đang ở trạng thái PROCESSING hay không
    public boolean isConfigRunning() {
        String sql = "SELECT COUNT(*) FROM logs WHERE status = 'PROCESSING' AND DATE(create_time) = CURDATE()";
        Long count = (Long) entityManager.createNativeQuery(sql)
                .getSingleResult();
        return count > 0;
    }


    // Trả về danh sách logs theo trạng thái
    public List<Log> getLogsByStatus(String status) {
        String sql = """
        SELECT * 
        FROM logs 
        WHERE status = ?1 
        ORDER BY create_time DESC
    """;
        return entityManager.createNativeQuery(sql, Log.class)
                .setParameter(1, status)
                .getResultList();
    }

    public Log getLatestLogByConfigId(int configId) {
        String sql = """
            SELECT * 
            FROM logs 
            WHERE id_config = ?1 
            ORDER BY create_time DESC 
            LIMIT 1
        """;
        try {
            return (Log) entityManager.createNativeQuery(sql, Log.class)
                    .setParameter(1, configId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Không tìm thấy log nào
        }
    }

    @Transactional
    public void updateLog(Log log) {
        String sql = """
        UPDATE logs 
        SET status = ?, 
            count = ?, 
            update_time = ?, 
            log_level = ?, 
            error_message = ?, 
            location = ?, 
            stack_trace = ?, 
            destination_path = ?
        WHERE id = ?
        """;

        entityManager.createNativeQuery(sql)
                .setParameter(1, log.getStatus().name())
                .setParameter(2, log.getCount())
                .setParameter(3, log.getUpdateTime())
                .setParameter(4, log.getLogLevel().name())
                .setParameter(5, log.getErrorMessage())
                .setParameter(6, log.getLocation())
                .setParameter(7, log.getStackTrace())
                .setParameter(8, log.getDestinationPath())
                .setParameter(9, log.getId())
                .executeUpdate();
    }
}
