package com.jewelry.backend.controller;

import com.jewelry.backend.dto.CreateRazorpayOrderRequest;
import com.jewelry.backend.dto.RazorpayOrderResponse;
import com.jewelry.backend.dto.TransactionFailureRequest;
import com.jewelry.backend.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
@Tag(name = "Payments", description = "Payment processing APIs")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/payments/razorpay-order")
    @Operation(summary = "Create Razorpay Order ID")
    public ResponseEntity<RazorpayOrderResponse> createRazorpayOrder(@RequestBody CreateRazorpayOrderRequest request) {
        return ResponseEntity.ok(paymentService.createRazorpayOrder(request));
    }

    @PostMapping("/transactions/failure")
    @Operation(summary = "Log failed transaction")
    public ResponseEntity<Void> logTransactionFailure(@RequestBody TransactionFailureRequest request) {
        paymentService.logFailure(request);
        return ResponseEntity.ok().build();
    }
}
