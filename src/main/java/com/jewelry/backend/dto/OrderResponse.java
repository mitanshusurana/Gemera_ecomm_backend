package com.jewelry.backend.dto;

import com.jewelry.backend.dto.CartResponse.CartProductResponse;
import com.jewelry.backend.dto.CartResponse.SelectedOption;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponse {
    private UUID id;
    private String orderNumber;
    private UUID userId;
    private List<OrderItemResponse> items;
    private String status;
    private BigDecimal total;
    private String trackingNumber;
    private LocalDateTime estimatedDelivery;
    private LocalDateTime createdAt;

    @Data
    public static class OrderItemResponse {
        private UUID id;
        private CartProductResponse product;
        private int quantity;
        private SelectedOption selectedMetal;
        private SelectedOption selectedDiamond;
    }
}
