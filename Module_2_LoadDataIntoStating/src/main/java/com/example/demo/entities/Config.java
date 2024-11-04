package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "config")
public class Config {
    @Id
    private int id;

    private String fileName;
    private String filePath;  // Đường dẫn tới file trên hệ thống lưu trữ

    @Enumerated(EnumType.STRING)
    private FileType fileType;  // Loại file dữ liệu (hoặc extension)

    private String fileEncoding;  // Mã hóa của file dữ liệu (ví dụ: UTF-8)
    private String sourcePath;  // Đường dẫn nguồn của dữ liệu
    private String destinationPath;   // Đường dẫn đích đến
    private String backupPath;  // Đường dẫn lưu trữ bản sao dự phòng
    private String delimiter;  // Dấu phân cách trong file (ví dụ: , ; \t)
    private String columns;  // Danh sách các cột của dữ liệu cần được crawl
    private String tables;  // Danh sách các bảng trong cơ sở dữ liệu
    @Enumerated(EnumType.STRING)
    private Status status;  // Trạng thái của quá trình crawl
    private String STAGING_source_username; // Tên người dùng để truy cập nguồn dữ liệu
    private String STAGING_source_password; // Mật khẩu để truy cập nguồn dữ liệu
    private String STAGING_source_host; // Địa chỉ host của nguồn dữ liệu
    private int STAGING_source_port; // Cổng để kết nối tới nguồn dữ liệu
    private String DW_source_username; // Tên người dùng để truy cập nguồn dữ liệu
    private String DW_source_password; // Mật khẩu để truy cập nguồn dữ liệu
    private String DW_source_host; // Địa chỉ host của nguồn dữ liệu
    private int DW_source_port; // Cổng để kết nối tới nguồn dữ liệu
    private int dataSize;   // Kích thước của dữ liệu
    private int crawlFrequency;  // Tần suất crawl dữ liệu
    private int timeout;    // Thời gian timeout tối đa cho một phiên crawl
    private LocalDateTime lastCrawlTime;  // Thời gian crawl dữ liệu cuối cùng
    private int retryCount;  // Số lần thử lại khi crawl dữ liệu thất bại
    private boolean isActive;  // Thêm biến isActive để bật/tắt cấu hình
    private LocalDateTime lastUpdated;  // Thời gian cập nhật cấu hình gần nhất
    private String notificationEmails;  // Danh sách email thông báo
    private String note;  // Ghi chú thêm về cấu hình
    private String version;  // Phiên bản của cấu hình
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createTime;
    @Column(nullable = false)
    private LocalDateTime updateTime;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.updateTime = LocalDateTime.now();
    }
}
