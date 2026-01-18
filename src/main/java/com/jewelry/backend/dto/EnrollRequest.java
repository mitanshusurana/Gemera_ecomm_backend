package com.jewelry.backend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class EnrollRequest {
    private String planName;
    private BigDecimal installmentAmount;
}
