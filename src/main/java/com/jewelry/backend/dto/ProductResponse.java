package com.jewelry.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Double rating;
    private Integer reviewCount;
    private String imageUrl; // Main image
    private String videoUrl;
    private String category;
    private String subcategory;
    private List<String> gemstones;
    private String metal;
    private Double weight;
    private Integer stock;
    private String sku;
    private List<String> certifications;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
