package com.example.demo.dao;

import com.example.demo.dto.ConfigDTO;
import com.example.demo.entities.Config;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ConfigDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Config saveConfig(Config config) {
        entityManager.persist(config); // Lưu đối tượng config vào cơ sở dữ liệu
        return config; // Trả về đối tượng đã lưu
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
    @Transactional
    public Config save(Config config) {
        String sql = """
        INSERT INTO config (id, file_name, file_path, file_encoding, crawl_frequency, data_size, 
                            retry_count, timeout, dw_source_port, staging_source_port, source_path, 
                            destination_path, backup_path, file_type, delimiter, dw_source_host, 
                            dw_source_password, dw_source_username, staging_source_host, staging_source_password, 
                            staging_source_username, columns, tables, note, notification_emails, 
                            , create_time, created_by, updated_by, version, is_active) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        entityManager.createNativeQuery(sql)
                .setParameter(1, config.getId())
                .setParameter(2, config.getFileName())
                .setParameter(3, config.getFilePath())
                .setParameter(4, config.getFileEncoding())
                .setParameter(5, config.getCrawlFrequency())
                .setParameter(6, config.getDataSize())
                .setParameter(7, config.getRetryCount())
                .setParameter(8, config.getTimeout())
                .setParameter(9, config.getDwSourcePort())
                .setParameter(10, config.getStagingSourcePort())
                .setParameter(11, config.getSourcePath())
                .setParameter(12, config.getDestinationPath())
                .setParameter(13, config.getBackupPath())
                .setParameter(14, config.getFileType() != null ? config.getFileType().name() : null)
                .setParameter(15, config.getDelimiter())
                .setParameter(16, config.getDwSourceHost())
                .setParameter(17, config.getDwSourcePassword())
                .setParameter(18, config.getDwSourceUsername())
                .setParameter(19, config.getStagingSourceHost())
                .setParameter(20, config.getStagingSourcePassword())
                .setParameter(21, config.getStagingSourceUsername())
                .setParameter(22, config.getColumns())
                .setParameter(23, config.getTables())
                .setParameter(24, config.getNote())
                .setParameter(25, config.getNotificationEmails())
                .setParameter(26, LocalDateTime.now()) // Thời gian tạo
                .setParameter(27, config.getCreatedBy())
                .setParameter(28, config.getUpdatedBy())
                .setParameter(29, config.getVersion())
                .setParameter(30, config.isActive())
                .executeUpdate(); // Thực hiện câu lệnh INSERT

        return config;
    }

    // Lấy tất cả các Config
    public List<Config> getAllConfigs() {
        String sql = "SELECT c FROM Config c"; // Query để lấy tất cả các đối tượng Config
        return entityManager.createQuery(sql, Config.class).getResultList();
    }


    @Transactional
    public Config updateConfig(Config config) {
        String sql = """
            UPDATE config
            SET file_name = ?, 
                file_path = ?, 
                file_encoding = ?, 
                crawl_frequency = ?, 
                data_size = ?, 
                retry_count = ?, 
                timeout = ?, 
                dw_source_port = ?, 
                staging_source_port = ?, 
                source_path = ?, 
                destination_path = ?, 
                backup_path = ?, 
                file_type = ?, 
                delimiter = ?, 
                dw_source_host = ?, 
                dw_source_password = ?, 
                dw_source_username = ?, 
                staging_source_host = ?, 
                staging_source_password = ?, 
                staging_source_username = ?, 
                columns = ?, 
                tables = ?, 
                note = ?, 
                notification_emails = ?, 
                updated_time = ?, 
                updated_by = ?, 
                version = ?, 
                is_active = ? 
            WHERE id = ?;
        """;

        entityManager.createNativeQuery(sql)
                .setParameter(1, config.getFileName())
                .setParameter(2, config.getFilePath())
                .setParameter(3, config.getFileEncoding())
                .setParameter(4, config.getCrawlFrequency())
                .setParameter(5, config.getDataSize())
                .setParameter(6, config.getRetryCount())
                .setParameter(7, config.getTimeout())
                .setParameter(8, config.getDwSourcePort())
                .setParameter(9, config.getStagingSourcePort())
                .setParameter(10, config.getSourcePath())
                .setParameter(11, config.getDestinationPath())
                .setParameter(12, config.getBackupPath())
                .setParameter(13, config.getFileType() != null ? config.getFileType().name() : null)
                .setParameter(14, config.getDelimiter())
                .setParameter(15, config.getDwSourceHost())
                .setParameter(16, config.getDwSourcePassword())
                .setParameter(17, config.getDwSourceUsername())
                .setParameter(18, config.getStagingSourceHost())
                .setParameter(19, config.getStagingSourcePassword())
                .setParameter(20, config.getStagingSourceUsername())
                .setParameter(21, config.getColumns())
                .setParameter(22, config.getTables())
                .setParameter(23, config.getNote())
                .setParameter(24, config.getNotificationEmails())
                .setParameter(25, LocalDateTime.now()) // Thời gian sửa đổi
                .setParameter(26, config.getUpdatedBy())
                .setParameter(27, config.getVersion())
                .setParameter(28, config.isActive())
                .setParameter(29, config.getId()) // ID để xác định bản ghi cần cập nhật
                .executeUpdate(); // Thực hiện câu lệnh UPDATE

        return config;
    }
    // Lấy tất cả các Config đang hoạt động (is_active = 1)
    public List<Config> getActiveConfigs() {
        String sql = "SELECT * FROM config WHERE is_active = 1"; // Câu truy vấn để lấy tất cả các Config đang hoạt động
        return entityManager.createNativeQuery(sql, Config.class).getResultList();
    }

}
