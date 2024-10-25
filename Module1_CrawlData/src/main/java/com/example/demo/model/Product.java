package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Product {
    private String id;
    private String sku;
    private String productName;
    private String shortDescription;
    private double price;
    private double originalPrice;
    private double discount;
    private int quantitySold;
    private String description;
    private List<String> images;
    private List<String> sizes;
    private List<String> colors;
    private String brandName;
    private String thumbnailUrl;
    private int discountRate;
    private double ratingAverage;
    private int reviewCount;
    private String urlKey;
    private String urlPath;
    private String shortUrl;
    private String type;
    private LocalDateTime createTime;

    public void validate() throws IllegalArgumentException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID không được rỗng");
        }
        if (productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được rỗng");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Giá sản phẩm phải lớn hơn 0");
        }
        if (originalPrice < price) {
            throw new IllegalArgumentException("Giá gốc phải lớn hơn hoặc bằng giá sản phẩm");
        }
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("Danh sách hình ảnh không được rỗng");
        }
    }
}