package com.jewelry.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderTrackResponse {
    private UUID id;
    private String orderNumber;
    private String status;
    private LocalDateTime estimatedDelivery;
    private List<TrackItem> items;

    @Data
    public static class TrackItem {
        private String productName;
        private int quantity;
        private String imageUrl;
    }
}
