package com.jewelry.backend.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class DiamondDetail {
    private String type;
    private Integer count;
    private Double totalWeight;
    private String clarity;
    private String color;
}
