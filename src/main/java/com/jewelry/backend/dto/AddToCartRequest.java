package com.jewelry.backend.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class AddToCartRequest {
    private UUID productId;
    private int quantity;
    private RequestOptions options;

    @Data
    public static class RequestOptions {
        private OptionDetail metal;
        private OptionDetail diamond;
        private String engraving;
    }

    @Data
    public static class OptionDetail {
        private String name;
    }
}
