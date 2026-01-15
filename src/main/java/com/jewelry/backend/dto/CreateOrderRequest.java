package com.jewelry.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {
  private AddressDTO shippingAddress;      // Changed to object
  private AddressDTO billingAddress;       // Changed to object
  private String paymentMethod;
  private String shippingMethod;
  private List<CartItemDTO> items;         // Add items list
  private Double total;                     // Add total
}

@Data
class AddressDTO {
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
class CartItemDTO {
  private String id;
  private ProductDTO product;
  private Integer quantity;
  private Object options;
}

@Data
class ProductDTO {
  private String id;
  private String name;
  private String description;
  private Double price;
  private String category;
  private Integer stock;
  private List<String> images;
}
