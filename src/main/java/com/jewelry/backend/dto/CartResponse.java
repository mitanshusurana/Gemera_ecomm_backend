package com.jewelry.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CartResponse {
    private UUID id;
    private List<CartItemResponse> items;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal shipping;
    private BigDecimal total;
    private BigDecimal appliedDiscount;
    private List<WishlistItemResponse> wishlist;

    @Data
    public static class CartItemResponse {
        private UUID id;
        private UUID productId;
        private int quantity;
        private BigDecimal price;
        private CartProductResponse product;
        private SelectedOption selectedMetal;
        private SelectedOption selectedDiamond;
    }

    @Data
    public static class CartProductResponse {
        private UUID id;
        private String name;
        private String imageUrl;
    }

    @Data
    public static class WishlistItemResponse {
        private UUID id;
        private String name;
        private BigDecimal price;
        private String imageUrl;
    }

    @Data
    public static class SelectedOption {
        private String name;
        public SelectedOption(String name) { this.name = name; }
    }
}
