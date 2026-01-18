package com.jewelry.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jewelry.backend.entity.embeddable.Address;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity {

    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    private String status; // PENDING_PAYMENT, CONFIRMED, SHIPPED, DELIVERED
    private BigDecimal total;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "firstName", column = @Column(name = "shipping_firstName")),
        @AttributeOverride(name = "lastName", column = @Column(name = "shipping_lastName")),
        @AttributeOverride(name = "email", column = @Column(name = "shipping_email")),
        @AttributeOverride(name = "phone", column = @Column(name = "shipping_phone")),
        @AttributeOverride(name = "address", column = @Column(name = "shipping_address")),
        @AttributeOverride(name = "city", column = @Column(name = "shipping_city")),
        @AttributeOverride(name = "state", column = @Column(name = "shipping_state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "shipping_zipCode")),
        @AttributeOverride(name = "country", column = @Column(name = "shipping_country"))
    })
    private Address shippingAddress;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "firstName", column = @Column(name = "billing_firstName")),
        @AttributeOverride(name = "lastName", column = @Column(name = "billing_lastName")),
        @AttributeOverride(name = "email", column = @Column(name = "billing_email")),
        @AttributeOverride(name = "phone", column = @Column(name = "billing_phone")),
        @AttributeOverride(name = "address", column = @Column(name = "billing_address")),
        @AttributeOverride(name = "city", column = @Column(name = "billing_city")),
        @AttributeOverride(name = "state", column = @Column(name = "billing_state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "billing_zipCode")),
        @AttributeOverride(name = "country", column = @Column(name = "billing_country"))
    })
    private Address billingAddress;

    private String paymentMethod;
    private String shippingMethod;
    private String trackingNumber;
    private LocalDateTime estimatedDelivery;
}
