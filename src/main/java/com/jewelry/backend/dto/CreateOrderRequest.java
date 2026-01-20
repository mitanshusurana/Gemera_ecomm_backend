package com.jewelry.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOrderRequest {
    private AddressDTO shippingAddress;
    private AddressDTO billingAddress;
    private String paymentMethod;
    private String shippingMethod;
    private List<CartItemDTO> items;
    private BigDecimal total;
    private PaymentDetailsDTO paymentDetails;

    @Data
    public static class AddressDTO {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private String city;
        private String state;
        private String zipCode;
        private String country;
    }

    @Data
    public static class CartItemDTO {
        private String id;
        private ProductDTO product;
        private Integer quantity;
        private Object options;
    }

    @Data
    public static class ProductDTO {
        private String id;
        private String name;
        private String description;
        private BigDecimal price;
        private String category;
        private Integer stock;
        private List<String> images;
    }

    @Data
    public static class PaymentDetailsDTO {
        private String razorpay_payment_id;
        private String razorpay_order_id;
        private String razorpay_signature;
    }
}
