package com.example.demo.model;

public enum Status {
    READY_EXTRACT,  // Sẵn sàng trích xuất
    SUCCESS_EXTRACT,  // Trích xuất thành công
    FAILURE_EXTRACT,  // Trích xuất thất bại
    PROCESSING,  // Đang xử lý
    ACTIVE;
}