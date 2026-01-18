package com.jewelry.backend.entity.embeddable;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Data
public class ProductSpecification {
    private Double carat;
    private String clarity;
    private String color;
    private String cut;
    private String origin;

    @Embedded
    private ProductDetails productDetails;

    @ElementCollection
    private List<MetalDetail> metalDetails = new ArrayList<>();

    @ElementCollection
    private List<DiamondDetail> diamondDetails = new ArrayList<>();
}
