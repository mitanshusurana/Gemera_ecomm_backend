package com.jewelry.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity {
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    private BigDecimal total;
    private String status; // PENDING_PAYMENT, PAID, SHIPPED, etc.

    // Address fields could be simplified for now
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
    private String shippingMethod;
}
