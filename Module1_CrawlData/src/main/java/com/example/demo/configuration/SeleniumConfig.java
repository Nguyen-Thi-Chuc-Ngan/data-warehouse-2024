package com.example.demo.configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumConfig {

    public WebDriver setupDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Chế độ chạy ẩn
        options.addArguments("--disable-gpu"); // Tắt GPU (tùy chọn này có thể cần trên Windows)
        options.addArguments("--no-sandbox"); // Tùy chọn an toàn hơn cho môi trường Linux
        options.addArguments("--disable-dev-shm-usage"); // Giảm thiểu tài nguyên bộ nhớ chia sẻ
        options.addArguments("--window-size=1920,1080"); // Đặt kích thước cửa sổ để tránh lỗi layout
        options.addArguments("--fake-user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"); // Add a fake user-agent if needed

        return new ChromeDriver(options);
    }
}
