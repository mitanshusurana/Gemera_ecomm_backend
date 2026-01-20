package com.jewelry.backend.service;

import com.jewelry.backend.dto.TreasureEnrollRequest;
import com.jewelry.backend.entity.TreasureChestAccount;
import com.jewelry.backend.entity.User;
import com.jewelry.backend.repository.TreasureChestAccountRepository;
import com.jewelry.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TreasurePlanService {

    @Autowired
    TreasureChestAccountRepository treasureChestAccountRepository;

    @Autowired
    UserRepository userRepository;

    public TreasureChestAccount getAccount(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return treasureChestAccountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Transactional
    public TreasureChestAccount enroll(String userEmail, TreasureEnrollRequest request) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        if (treasureChestAccountRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("User already enrolled");
        }

        TreasureChestAccount account = new TreasureChestAccount();
        account.setUser(user);
        account.setPlanName(request.getPlanName());
        account.setInstallmentAmount(request.getInstallmentAmount());
        account.setCurrentBalance(BigDecimal.ZERO);
        account.setStatus("ACTIVE");

        return treasureChestAccountRepository.save(account);
    }
}
