package com.example.demo.services;

import com.example.demo.configuration.StagingDatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class ProductServiceImpl  implements IProductService{

    private final StagingDatabaseConnection stagingDatabaseConnection;

    @Autowired
    public ProductServiceImpl(StagingDatabaseConnection stagingDatabaseConnection) {
        this.stagingDatabaseConnection = stagingDatabaseConnection;
    }

    // Phương thức tạo bảng product_staging nếu chưa tồn tại
    public void createProductStagingTable() {
        String createTableSQL = """ 
                CREATE TABLE IF NOT EXISTS product_staging (
                                        product_id INT,
        								sku VARCHAR(50),
                                        product_name VARCHAR(255),
                                        price DECIMAL(10,2),
                                        original_price DECIMAL(10,2),
                                        brand_name VARCHAR(100),
                                        discount DECIMAL(18,2),
                                        thumbnail_url TEXT,
                                        short_description TEXT,
                                        images TEXT,
                                        colors TEXT,
                                        size TEXT,
                                        rating_average DECIMAL(3,2),
                                        review_count INT,
                                        discount_rate DECIMAL(5,2),
                                        quantity_sold INT,
                                        url_key VARCHAR(255),
                                        url_path VARCHAR(255),
                                        short_url VARCHAR(255),
                                        type VARCHAR(50),
                                        create_time VARCHAR(50)             
                    );""";

        try (Connection connection = stagingDatabaseConnection.connectToStagingDatabase(1); // Giả sử ID là 1
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Bảng product_staging đã được tạo hoặc đã tồn tại.");
        } catch (SQLException e) {
            System.err.println("Có lỗi xảy ra khi tạo bảng: " + e.getMessage());
        }
    }

    // Phương thức nạp dữ liệu từ file CSV vào bảng product_staging
    public void loadCsvDataIntoStaging(String filePath, String columns) {
        String normalizedFilePath = filePath.replace("\\", "/");
        String sql = String.format("LOAD DATA INFILE '%s' INTO TABLE product_staging " +
                "FIELDS TERMINATED BY ',' " +
                "OPTIONALLY ENCLOSED BY '\"' " +
                "LINES TERMINATED BY '\\n' " +
                "IGNORE 1 LINES (%s);", normalizedFilePath, columns);

        try (Connection connection = stagingDatabaseConnection.connectToStagingDatabase(1); // Giả sử ID là 1
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Dữ liệu đã được nạp thành công từ file CSV vào bảng product_staging.");
        } catch (SQLException e) {
            System.err.println("Có lỗi xảy ra khi nạp dữ liệu từ CSV: " + e.getMessage());
        }
    }

    public void truncateProductStagingTable() {
        String truncateTableSQL = "TRUNCATE TABLE product_staging;";

        try (Connection connection = stagingDatabaseConnection.connectToStagingDatabase(1);
             Statement statement = connection.createStatement()) {
            statement.execute(truncateTableSQL);
            System.out.println("Đã xóa toàn bộ dữ liệu trong bảng product_staging.");
        } catch (SQLException e) {
            System.err.println("Có lỗi xảy ra khi truncate bảng: " + e.getMessage());
        }
    }
}
