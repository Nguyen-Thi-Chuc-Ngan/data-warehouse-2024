package com.example.demo.service.crawler;

import com.example.demo.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrawlService {

    private final RestTemplate restTemplate;

    @Autowired
    public CrawlService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0";

    public List<Product> crawlProducts(List<String> productIds,String baseUrl) throws InterruptedException, JsonProcessingException {
        List<Product> result = new ArrayList<>();

        for (String pid : productIds) {
            String url = String.format(baseUrl + "/%s", pid);
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", USER_AGENT);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Parse dữ liệu JSON thành đối tượng Product
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode productData = objectMapper.readTree(response.getBody());

                Product product = new Product();
                product.setId(getProductId(productData));
                product.setSku(getProductSku(productData));
                product.setProductName(getProductName(productData));
                product.setPrice(getProductPrice(productData));
                product.setOriginalPrice(getOriginalPrice(productData));
                product.setThumbnailUrl(getThumbnailUrl(productData));
                product.setDiscount(getDiscount(productData));
                product.setShortDescription(getShortDescription(productData));
                product.setBrandName(getBrandName(productData));
                product.setShortUrl(getShortUrl(productData));
                product.setUrlKey(getUrlKey(productData));
                product.setUrlPath(getUrlPath(productData));
                product.setType(getType(productData));
                // Lấy thông tin chi tiết về sản phẩm
                String detailsUrl = String.format("https://tiki.vn/api/v2/products/%s?platform=web", pid);
                ResponseEntity<String> detailResponse = restTemplate.exchange(detailsUrl, HttpMethod.GET, entity, String.class);

                if (detailResponse.getStatusCode() == HttpStatus.OK) {
                    JsonNode productDetailData = objectMapper.readTree(detailResponse.getBody());

                    // Lấy các thông số kỹ thuật (specifications)
                    JsonNode specifications = productDetailData.get("specifications");
                    if (specifications != null && !specifications.isNull()) {
                    } else {
                        System.out.println("Specifications not found or is null");
                    }
                    // Lấy các hình ảnh của sản phẩm
                    product.setImages(getImages(productDetailData));
                    // Lấy các màu của sản phẩm (nếu có)
                    product.setColors(getColors(productDetailData));
                    //Lấy các size của sản phẩm
                    product.setSizes(getSizes(productDetailData));
                    //Lấy ra trung bình đánh giá sao
                    product.setRatingAverage(getRatingAverage(productDetailData));
                    product.setDiscountRate(getDiscountRate(productDetailData));
                    product.setReviewCount(getReviewCount(productDetailData));
                    product.setQuantitySold(getQuantitySold(productDetailData));
                    product.setCreateTime(LocalDateTime.now());
                }
                result.add(product);
                // Đợi ngẫu nhiên giữa các yêu cầu
                Thread.sleep(randomSleep());
            } else {
                System.out.println("Failed to crawl product: " + pid);
            }
        }
        return result;
    }

    // Hàm để tạo thời gian ngủ ngẫu nhiên giữa 3-5 giây
    private long randomSleep() {
        return (long) (Math.random() * (5000 - 3000) + 3000);
    }

    private String getProductId(JsonNode productData) {
        return productData.has("id") && !productData.get("id").isNull()
                ? productData.get("id").asText()
                : "Unknown ID";
    }

    private String getProductSku(JsonNode productData) {
        return productData.has("sku") && !productData.get("sku").isNull()
                ? productData.get("sku").asText()
                : "Unknown ID";
    }

    private String getProductName(JsonNode productData) {
        return productData.has("name") && !productData.get("name").isNull()
                ? productData.get("name").asText()
                : "Unknown Name";
    }

    private String getUrlKey(JsonNode productData) {
        if (productData.has("url_key") && !productData.get("url_key").isNull()) {
            return productData.get("url_key").asText();
        } else {
//            System.out.println("URL key not found or is null");
            return "Unknown URL Key";
        }
    }

    private String getUrlPath(JsonNode productData) {
        if (productData.has("url_path") && !productData.get("url_path").isNull()) {
            return productData.get("url_path").asText();
        } else {
//            System.out.println("URL path not found or is null");
            return "Unknown URL Path";
        }
    }

    private String getShortUrl(JsonNode productData) {
        if (productData.has("short_url") && !productData.get("short_url").isNull()) {
            return productData.get("short_url").asText();
        } else {
//            System.out.println("Short URL not found or is null");
            return "Unknown Short URL";
        }
    }

    private String getType(JsonNode productData) {
        if (productData.has("type") && !productData.get("type").isNull()) {
            return productData.get("type").asText();
        } else {
//            System.out.println("Type not found or is null");
            return "Unknown Type";
        }
    }

    private String getBrandName(JsonNode productData) {
        JsonNode brandNode = productData.get("brand");

        if (brandNode != null && !brandNode.isNull() && brandNode.has("name")) {
            return brandNode.get("name").asText();
        } else {
//            System.out.println("Brand name not found or is null");
            return "Unknown Brand";
        }
    }

    private double getProductPrice(JsonNode productData) {
        return productData.has("price") && !productData.get("price").isNull()
                ? productData.get("price").asDouble()
                : 0.0;
    }

    private double getOriginalPrice(JsonNode productData) {
        return productData.has("original_price") && !productData.get("original_price").isNull()
                ? productData.get("original_price").asDouble()
                : 0.0;
    }

    private String getThumbnailUrl(JsonNode productData) {
        return productData.has("thumbnail_url") && !productData.get("thumbnail_url").isNull()
                ? productData.get("thumbnail_url").asText()
                : "";
    }

    private double getDiscount(JsonNode productData) {
        return productData.has("discount") && !productData.get("discount").isNull()
                ? productData.get("discount").asDouble()
                : 0.0;
    }

    private String getShortDescription(JsonNode productData) {
        return productData.has("short_description") && !productData.get("short_description").isNull()
                ? productData.get("short_description").asText()
                : "";
    }

    private List<String> getImages(JsonNode productDetailData) {
        JsonNode images = productDetailData.get("images");
        List<String> imageUrls = new ArrayList<>();

        if (images != null && images.isArray()) {
            for (JsonNode image : images) {
                if (image.has("large_url") && !image.get("large_url").isNull()) {
                    imageUrls.add(image.get("large_url").asText());
                }
            }
        } else {
//            System.out.println("Images not found or is not an array");
        }

        return imageUrls;
    }

    // Hàm lấy danh sách màu sắc
    private List<String> getColors(JsonNode productDetailData) {
        List<String> colors = new ArrayList<>();
        JsonNode configurableOptions = productDetailData.get("configurable_options");

        if (configurableOptions != null && configurableOptions.isArray()) {
            for (JsonNode option : configurableOptions) {
                // So sánh cả code và name để xác định thuộc tính Màu
                if ("option1".equalsIgnoreCase(option.get("code").asText()) && "Màu".equalsIgnoreCase(option.get("name").asText())) {
                    if (option.has("values") && option.get("values").isArray()) {
                        for (JsonNode value : option.get("values")) {
                            String color = value.get("label").asText();
                            // Chuẩn hóa và thêm màu vào danh sách
                            String[] splitColors = color.split("[;,]");
                            for (String c : splitColors) {
                                colors.add(c.trim().toLowerCase()); // Loại bỏ khoảng trắng và chuyển thành chữ thường
                            }
                        }
                    }
                }
            }
        }
        return colors;
    }

    private List<String> getSizes(JsonNode productDetailData) {
        List<String> sizes = new ArrayList<>();
        JsonNode configurableOptions = productDetailData.get("configurable_options");

        if (configurableOptions != null && configurableOptions.isArray()) {
            for (JsonNode option : configurableOptions) {
                // So sánh cả code và name để xác định thuộc tính Kích cỡ
                if ("option2".equalsIgnoreCase(option.get("code").asText()) && "Kích cỡ".equalsIgnoreCase(option.get("name").asText())) {
                    if (option.has("values") && option.get("values").isArray()) {
                        for (JsonNode value : option.get("values")) {
                            String size = value.get("label").asText();
                            // Chuẩn hóa và thêm kích cỡ vào danh sách
                            String[] splitSizes = size.split("[;,]");
                            for (String s : splitSizes) {
                                sizes.add(s.trim()); // Loại bỏ khoảng trắng
                            }
                        }
                    }
                }
            }
        }
        return sizes;
    }


    private double getRatingAverage(JsonNode productDetailData) {
        JsonNode ratingAverageNode = productDetailData.get("rating_average");
        if (ratingAverageNode != null && !ratingAverageNode.isNull()) {
            return ratingAverageNode.asDouble(); // Trả về giá trị double
        } else {
//            System.out.println("Rating average not found or is null");
            return 0.0;
        }
    }

    private int getReviewCount(JsonNode productDetailData) {
        JsonNode reviewCountNode = productDetailData.get("review_count");

        if (reviewCountNode != null && !reviewCountNode.isNull()) {
            return reviewCountNode.asInt(); // Trả về giá trị int
        } else {
//            System.out.println("Review count not found or is null");
            return 0;
        }
    }

    private int getDiscountRate(JsonNode productDetailData) {
        JsonNode discountRateNode = productDetailData.get("discount_rate");

        if (discountRateNode != null && !discountRateNode.isNull()) {
            return discountRateNode.asInt();
        } else {
//            System.out.println("Discount rate not found or is null");
            return 0;
        }
    }

    private int getQuantitySold(JsonNode productDetailData) {
        JsonNode quantitySoldNode = productDetailData.get("quantity_sold");

        if (quantitySoldNode != null && !quantitySoldNode.isNull()) {
            return quantitySoldNode.get("value").asInt();
        } else {
//            System.out.println("Quantity sold not found or is null");
            return 0;
        }
    }


}
