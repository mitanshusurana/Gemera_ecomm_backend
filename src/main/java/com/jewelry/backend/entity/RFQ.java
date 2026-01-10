package com.jewelry.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@Table(name = "rfq_requests")
@Data
@EqualsAndHashCode(callSuper = true)
public class RFQ extends BaseEntity {
    @ManyToOne
    private User user;

    private String status; // PENDING, QUOTED, ACCEPTED, CANCELLED
    private String details;

    // For quotes
    private BigDecimal quoteAmount;
}
