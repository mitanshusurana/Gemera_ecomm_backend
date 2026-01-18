package com.jewelry.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "treasure_plan_installments")
@Data
@EqualsAndHashCode(callSuper = true)
public class TreasurePlanInstallment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "treasure_plan_id")
    private TreasurePlan treasurePlan;

    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentReference;
}
