package com.jewelry.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;
import java.util.Map;

@Data
public class ProductDTO {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Integer stock;
    private List<String> images;
    private Map<String, String> specifications;
    private List<CustomizationOptionDTO> customizationOptions;
    private List<String> occasions;
    private List<String> styles;

    @Data
    public static class CustomizationOptionDTO {
        private String type;
        private String name;
        private BigDecimal priceModifier;
    }
}
