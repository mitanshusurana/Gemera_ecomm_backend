package com.jewelry.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateOrderRequest {
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
    private String shippingMethod;
}
