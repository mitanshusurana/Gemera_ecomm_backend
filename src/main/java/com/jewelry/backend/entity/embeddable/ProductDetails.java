package com.jewelry.backend.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ProductDetails {
    private String productSku; // Renamed from sku to avoid conflict if flattened
    private String width;
    private String height;
    private Double grossWeight;
}
