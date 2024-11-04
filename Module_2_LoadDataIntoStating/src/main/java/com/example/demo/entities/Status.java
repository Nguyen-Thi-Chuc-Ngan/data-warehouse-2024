package com.example.demo.entities;

public enum Status {
    READY_EXTRACT,  // Sẵn sàng trích xuất
    SUCCESS_EXTRACT,  // Trích xuất thành công
    FAILURE_EXTRACT,  // Trích xuất thất bại
    PROCESSING,  // Đang xử lý
    ACTIVE;
}