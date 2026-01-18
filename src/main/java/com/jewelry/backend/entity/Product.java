package com.jewelry.backend.entity;

import com.jewelry.backend.entity.embeddable.PriceBreakup;
import com.jewelry.backend.entity.embeddable.ProductImage;
import com.jewelry.backend.entity.embeddable.ProductSpecification;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity {
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal price;
    private BigDecimal originalPrice;
    private Double rating;
    private Integer reviewCount;
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ElementCollection
    private List<String> gemstones = new ArrayList<>();

    private String metal; // e.g. "18K White Gold"
    private Double weight;
    private Integer stock;
    private String sku;

    @ElementCollection
    private List<String> certifications = new ArrayList<>();

    @ElementCollection
    private List<ProductImage> images = new ArrayList<>();

    @Embedded
    private ProductSpecification specifications;

    @Embedded
    private PriceBreakup priceBreakup;

    @ElementCollection
    private List<UUID> relatedProducts = new ArrayList<>();
}
