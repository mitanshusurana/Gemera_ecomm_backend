package com.jewelry.backend.service;

import com.jewelry.backend.dto.CreateOrderRequest;
import com.jewelry.backend.entity.*;
import com.jewelry.backend.repository.CartRepository;
import com.jewelry.backend.repository.OrderItemRepository;
import com.jewelry.backend.repository.OrderRepository;
import com.jewelry.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

    @Autowired
    CartService cartService;

    @Transactional
    public Order createOrder(String userEmail, CreateOrderRequest request) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Cart cart = cartService.getCart(userEmail);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotal(cart.getTotal());
        order.setStatus("PENDING_PAYMENT");
        order.setShippingAddress(String.valueOf(request.getShippingAddress()));
        order.setBillingAddress(String.valueOf(request.getBillingAddress()));
        order.setPaymentMethod(request.getPaymentMethod());
        order.setShippingMethod(request.getShippingMethod());

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
        cart.setSubtotal(java.math.BigDecimal.ZERO);
        cart.setTotal(java.math.BigDecimal.ZERO);
        cartRepository.save(cart);

        return savedOrder;
    }

    public List<Order> getUserOrders(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return orderRepository.findByUser(user);
    }

    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }
}
