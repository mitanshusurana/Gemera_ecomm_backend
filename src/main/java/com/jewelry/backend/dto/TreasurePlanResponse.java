package com.jewelry.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class TreasurePlanResponse {
    private UUID id;
    private String planName;
    private BigDecimal balance;
    private Integer installmentsPaid;
    private Integer totalInstallments;
    private String status;
    private LocalDate nextDueDate;
    private LocalDate startDate;
}
