package com.jewelry.backend.service;

import com.jewelry.backend.dto.CartResponse;
import com.jewelry.backend.dto.CreateOrderRequest;
import com.jewelry.backend.dto.OrderResponse;
import com.jewelry.backend.dto.OrderTrackResponse;
import com.jewelry.backend.entity.*;
import com.jewelry.backend.repository.CartRepository;
import com.jewelry.backend.repository.OrderItemRepository;
import com.jewelry.backend.repository.OrderRepository;
import com.jewelry.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public OrderResponse createOrder(String userEmail, CreateOrderRequest request) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotal(cart.getTotal());
        order.setStatus("PENDING_PAYMENT");
        order.setShippingAddress(request.getShippingAddress());
        order.setBillingAddress(request.getBillingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setShippingMethod(request.getShippingMethod());
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setOptions(cartItem.getOptions());

            orderItemRepository.save(orderItem);
        }

        // Clear cart
        cart.getItems().clear();
        cart.setSubtotal(BigDecimal.ZERO);
        cart.setTotal(BigDecimal.ZERO);
        cart.setTax(BigDecimal.ZERO);
        cart.setShipping(BigDecimal.ZERO);
        cartRepository.save(cart);

        return mapToResponse(savedOrder);
    }

    public Page<OrderResponse> getUserOrders(String userEmail, Pageable pageable) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Page<Order> orders = orderRepository.findByUser(user, pageable);
        return orders.map(this::mapToResponse);
    }

    public OrderResponse getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToResponse(order);
    }

    public OrderTrackResponse trackOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        OrderTrackResponse response = new OrderTrackResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setStatus(order.getStatus());
        response.setEstimatedDelivery(order.getEstimatedDelivery());
        response.setItems(order.getItems().stream().map(item -> {
            OrderTrackResponse.TrackItem tItem = new OrderTrackResponse.TrackItem();
            tItem.setProductName(item.getProduct().getName());
            tItem.setQuantity(item.getQuantity());
            if (!item.getProduct().getImages().isEmpty()) {
                tItem.setImageUrl(item.getProduct().getImages().get(0).getUrl());
            }
            return tItem;
        }).collect(Collectors.toList()));
        return response;
    }

    public OrderResponse updateStatus(UUID orderId, String status, String trackingNumber) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        if (trackingNumber != null) {
            order.setTrackingNumber(trackingNumber);
        }
        return mapToResponse(orderRepository.save(order));
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setUserId(order.getUser().getId());
        response.setStatus(order.getStatus());
        response.setTotal(order.getTotal());
        response.setTrackingNumber(order.getTrackingNumber());
        response.setEstimatedDelivery(order.getEstimatedDelivery());
        response.setCreatedAt(order.getCreatedAt());

        response.setItems(order.getItems().stream().map(item -> {
            OrderResponse.OrderItemResponse iResp = new OrderResponse.OrderItemResponse();
            iResp.setId(item.getId());
            iResp.setQuantity(item.getQuantity());

            CartResponse.CartProductResponse pResp = new CartResponse.CartProductResponse();
            pResp.setId(item.getProduct().getId());
            pResp.setName(item.getProduct().getName());
            if (!item.getProduct().getImages().isEmpty()) {
                pResp.setImageUrl(item.getProduct().getImages().get(0).getUrl());
            }
            iResp.setProduct(pResp);

            if (item.getOptions() != null) {
                if (item.getOptions().getMetal() != null)
                    iResp.setSelectedMetal(new CartResponse.SelectedOption(item.getOptions().getMetal()));
                if (item.getOptions().getDiamond() != null)
                    iResp.setSelectedDiamond(new CartResponse.SelectedOption(item.getOptions().getDiamond()));
            }
            return iResp;
        }).collect(Collectors.toList()));

        return response;
    }
}
