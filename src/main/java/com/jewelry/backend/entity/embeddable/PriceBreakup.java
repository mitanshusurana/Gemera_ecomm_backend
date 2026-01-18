package com.jewelry.backend.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.math.BigDecimal;

@Embeddable
@Data
public class PriceBreakup {
    private BigDecimal metalComponent; // Renamed to avoid confusion with metal type
    private BigDecimal gemstoneComponent;
    private BigDecimal makingCharges;
    private BigDecimal taxComponent;
    private BigDecimal totalComponent;
    private BigDecimal discountComponent;
    private BigDecimal grandTotal;
}
