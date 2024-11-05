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

    // Thông tin kết nối đến nguồn dữ liệu STAGING
    private String stagingSourceUsername; // Tên người dùng để truy cập nguồn dữ liệu
    private String stagingSourcePassword; // Mật khẩu để truy cập nguồn dữ liệu
    private String stagingSourceHost; // Địa chỉ host của nguồn dữ liệu
    private int stagingSourcePort; // Cổng để kết nối tới nguồn dữ liệu

    // Thông tin kết nối đến nguồn dữ liệu DW
    private String dwSourceUsername; // Tên người dùng để truy cập nguồn dữ liệu
    private String dwSourcePassword; // Mật khẩu để truy cập nguồn dữ liệu
    private String dwSourceHost; // Địa chỉ host của nguồn dữ liệu
    private int dwSourcePort; // Cổng để kết nối tới nguồn dữ liệu

    private int dataSize;   // Kích thước của dữ liệu
    private int crawlFrequency;  // Tần suất crawl dữ liệu
    private int timeout;    // Thời gian timeout tối đa cho một phiên crawl
    private int retryCount;  // Số lần thử lại khi crawl dữ liệu thất bại
    private boolean isActive;  // Thêm biến isActive để bật/tắt cấu hình
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
