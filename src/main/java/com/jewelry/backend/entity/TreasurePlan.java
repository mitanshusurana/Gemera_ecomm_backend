package com.jewelry.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "treasure_plans")
@Data
@EqualsAndHashCode(callSuper = true)
public class TreasurePlan extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String planName;
    private BigDecimal balance = BigDecimal.ZERO;
    private Integer installmentsPaid = 0;
    private Integer totalInstallments = 11; // Usually schemes are 11+1 free or similar. Setting default.
    private String status; // ACTIVE, COMPLETED, CLOSED
    private LocalDate startDate;
    private LocalDate nextDueDate;

    @OneToMany(mappedBy = "treasurePlan", cascade = CascadeType.ALL)
    private List<TreasurePlanInstallment> installments = new ArrayList<>();
}
