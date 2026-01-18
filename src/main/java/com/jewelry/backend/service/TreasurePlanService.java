package com.jewelry.backend.service;

import com.jewelry.backend.dto.EnrollRequest;
import com.jewelry.backend.dto.TreasurePlanResponse;
import com.jewelry.backend.entity.TreasurePlan;
import com.jewelry.backend.entity.User;
import com.jewelry.backend.repository.TreasurePlanRepository;
import com.jewelry.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TreasurePlanService {

    @Autowired
    TreasurePlanRepository treasurePlanRepository;

    @Autowired
    UserRepository userRepository;

    public TreasurePlanResponse getAccount(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        TreasurePlan plan = treasurePlanRepository.findByUserIdAndStatus(user.getId(), "ACTIVE")
                .orElseThrow(() -> new RuntimeException("No active plan found"));
        return mapToResponse(plan);
    }

    public TreasurePlanResponse enroll(String email, EnrollRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Check if already has active plan
        if (treasurePlanRepository.findByUserIdAndStatus(user.getId(), "ACTIVE").isPresent()) {
            throw new RuntimeException("User already has an active plan");
        }

        TreasurePlan plan = new TreasurePlan();
        plan.setUser(user);
        plan.setPlanName(request.getPlanName());
        plan.setBalance(BigDecimal.ZERO); // Initial balance is 0 until first payment? Or does enroll include payment?
        // The requirements say Enroll response has status ACTIVE and startDate.
        // Assuming first payment happens separately or this is just enrollment.
        // Logic says "installmentAmount" in request. Maybe it charges the first one.
        // For now, I'll just set it up.
        plan.setInstallmentsPaid(0);
        plan.setTotalInstallments(12); // Default
        plan.setStatus("ACTIVE");
        plan.setStartDate(LocalDate.now());
        plan.setNextDueDate(LocalDate.now().plusMonths(1));

        plan = treasurePlanRepository.save(plan);
        return mapToResponse(plan);
    }

    private TreasurePlanResponse mapToResponse(TreasurePlan plan) {
        TreasurePlanResponse response = new TreasurePlanResponse();
        response.setId(plan.getId());
        response.setPlanName(plan.getPlanName());
        response.setBalance(plan.getBalance());
        response.setInstallmentsPaid(plan.getInstallmentsPaid());
        response.setTotalInstallments(plan.getTotalInstallments());
        response.setStatus(plan.getStatus());
        response.setNextDueDate(plan.getNextDueDate());
        response.setStartDate(plan.getStartDate());
        return response;
    }
}
