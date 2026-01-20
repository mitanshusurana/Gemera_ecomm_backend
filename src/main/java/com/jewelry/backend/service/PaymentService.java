package com.jewelry.backend.service;

import com.jewelry.backend.dto.CreateRazorpayOrderRequest;
import com.jewelry.backend.dto.RazorpayOrderResponse;
import com.jewelry.backend.dto.TransactionFailureRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

@Service
public class PaymentService {

    private static final Logger LOGGER = Logger.getLogger(PaymentService.class.getName());

    public RazorpayOrderResponse createRazorpayOrder(CreateRazorpayOrderRequest request) {
        // Mock Razorpay Order Creation
        String mockId = "order_" + UUID.randomUUID().toString().replace("-", "").substring(0, 14);
        return new RazorpayOrderResponse(mockId, request.getAmount(), request.getCurrency(), "created");
    }

    public void logFailure(TransactionFailureRequest request) {
        LOGGER.severe("Transaction Failed: " + request.toString());
    }
}
