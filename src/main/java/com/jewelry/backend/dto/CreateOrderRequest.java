package com.jewelry.backend.dto;

import com.jewelry.backend.entity.embeddable.Address;
import lombok.Data;

@Data
public class CreateOrderRequest {
    private Address shippingAddress;
    private Address billingAddress;
    private String paymentMethod;
    private String shippingMethod;
}
