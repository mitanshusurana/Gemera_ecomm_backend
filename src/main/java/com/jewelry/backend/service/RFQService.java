package com.jewelry.backend.service;

import com.jewelry.backend.entity.RFQ;
import com.jewelry.backend.entity.User;
import com.jewelry.backend.repository.RFQRepository;
import com.jewelry.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RFQService {

    @Autowired
    RFQRepository rfqRepository;

    @Autowired
    UserRepository userRepository;

    public RFQ createRequest(String userEmail, RFQ rfq) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        rfq.setUser(user);
        rfq.setStatus("PENDING");
        return rfqRepository.save(rfq);
    }

    public RFQ getRequest(UUID id) {
        return rfqRepository.findById(id).orElseThrow(() -> new RuntimeException("RFQ not found"));
    }

    public List<RFQ> getUserRequests(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return rfqRepository.findByUser(user);
    }

    // Critical Addendum: Cancel Request
    public RFQ cancelRequest(String userEmail, UUID id) {
        RFQ rfq = getRequest(id);
        if (!rfq.getUser().getEmail().equals(userEmail)) {
             throw new RuntimeException("Not authorized");
        }
        rfq.setStatus("CANCELLED");
        return rfqRepository.save(rfq);
    }

    // Critical Addendum: Admin Statistics
    public Map<String, Object> getStatistics() {
        long total = rfqRepository.count();
        // This would ideally require custom queries. For now mocking logic based on retrieval or simple counts.
        // Real implementation would be `countByStatus`
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRequests", total);
        stats.put("totalQuotes", 5); // Mock
        stats.put("acceptanceRate", 0.5); // Mock
        stats.put("averageQuoteValue", 5000); // Mock
        return stats;
    }
}
