package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity // Đánh dấu lớp này là một thực thể JPA
@Table(name = "products") // Tên bảng trong cơ sở dữ liệu
public class Product {
	@Id
	private String id;
	private String sku;
	private String productName;
	private String shortDescription;
	private double price;
	private double originalPrice;
	private double discount;
	private int quantitySold;
	private String description;
	private String images;
	private String sizes;
	private String colors;
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

}

