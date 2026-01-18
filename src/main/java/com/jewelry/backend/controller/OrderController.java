package com.jewelry.backend.controller;

import com.jewelry.backend.dto.CreateOrderRequest;
import com.jewelry.backend.dto.OrderResponse;
import com.jewelry.backend.dto.OrderTrackResponse;
import com.jewelry.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request, Principal principal) {
        return ResponseEntity.status(201).body(orderService.createOrder(principal.getName(), request));
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrders(Principal principal, Pageable pageable) {
        return ResponseEntity.ok(orderService.getUserOrders(principal.getName(), pageable));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping("/track/{orderId}")
    public ResponseEntity<OrderTrackResponse> trackOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.trackOrder(orderId));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable UUID orderId,
            @RequestBody Map<String, String> body) {
        // In real app, check for ADMIN role here
        return ResponseEntity.ok(orderService.updateStatus(orderId, body.get("status"), body.get("trackingNumber")));
    }
}
