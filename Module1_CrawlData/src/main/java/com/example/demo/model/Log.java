package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Khóa chính

    @JoinColumn(name = "id_config") // Tên cột trong bảng logs
    private int idConfig;

    @Enumerated(EnumType.STRING) // Nếu LogLevel là enum
    private LogLevel logLevel;
    private String destinationPath;   // Đường dẫn đích đến
    private int count;
    private String location;
    private LocalDateTime updateTime;
    private String errorMessage;
    private String stackTrace;  // Chi tiết stack trace khi xảy ra lỗi

    @Enumerated(EnumType.STRING) // Nếu Status là enum
    private Status status;  // Trạng thái của quá trình crawl
    private String createdBy;
    private LocalDateTime createTime;
}
