package com.jewelry.backend.controller;

import com.jewelry.backend.dto.CreateOrderRequest;
import com.jewelry.backend.dto.OrderTracking;
import com.jewelry.backend.entity.Order;
import com.jewelry.backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Order management APIs")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    @Operation(summary = "Create new order")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request, Principal principal) {
        return ResponseEntity.status(201).body(orderService.createOrder(principal.getName(), request));
    }

    @GetMapping
    @Operation(summary = "Get user orders")
    public ResponseEntity<Page<Order>> getOrders(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            Principal principal) {
        return ResponseEntity.ok(orderService.getUserOrders(principal.getName(), PageRequest.of(page, size)));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order details")
    public ResponseEntity<Order> getOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping("/track/{id}")
    @Operation(summary = "Track order (Public)")
    public ResponseEntity<OrderTracking> trackOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.trackOrder(id));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update status (Admin)")
    public ResponseEntity<Order> updateStatus(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(orderService.updateStatus(id, body.get("status"), body.get("trackingNumber")));
    }
}
