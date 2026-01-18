package com.jewelry.backend.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class MetalDetail {
    private String type;
    private Double weight;
    private String purity;
}
