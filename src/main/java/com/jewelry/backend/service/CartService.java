package com.jewelry.backend.service;

import com.jewelry.backend.dto.AddToCartRequest;
import com.jewelry.backend.entity.Cart;
import com.jewelry.backend.entity.CartItem;
import com.jewelry.backend.entity.Product;
import com.jewelry.backend.entity.User;
import com.jewelry.backend.repository.CartItemRepository;
import com.jewelry.backend.repository.CartRepository;
import com.jewelry.backend.repository.ProductRepository;
import com.jewelry.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public Cart getCart(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
    }

    @Transactional
    public Cart addItemToCart(String userEmail, AddToCartRequest request) {
        Cart cart = getCart(userEmail);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // For simplicity in this demo, always add new item if options are present or not matching exactly.
        // In a real scenario, we'd compare options deeply.
        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setProduct(product);
        newItem.setQuantity(request.getQuantity());
        newItem.setOptions(request.getOptions());

        cart.getItems().add(newItem);
        cartItemRepository.save(newItem); // Cascade might handle this but safer to save

        recalculateCart(cart);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateItemQuantity(String userEmail, UUID itemId, int quantity) {
        Cart cart = getCart(userEmail);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
             throw new RuntimeException("Item does not belong to user cart");
        }

        if (quantity <= 0) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        recalculateCart(cart);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItem(String userEmail, UUID itemId) {
        return updateItemQuantity(userEmail, itemId, 0);
    }

    @Transactional
    public Cart updateCartOptions(String userEmail, boolean giftWrap) {
        Cart cart = getCart(userEmail);
        cart.setGiftWrap(giftWrap);
        recalculateCart(cart);
        return cartRepository.save(cart);
    }

    private void recalculateCart(Cart cart) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            BigDecimal itemTotal = item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity()));
            subtotal = subtotal.add(itemTotal);
        }
        cart.setSubtotal(subtotal);
        cart.setTax(subtotal.multiply(new BigDecimal("0.10"))); // 10% tax
        cart.setShipping(subtotal.compareTo(new BigDecimal("1000")) > 0 ? BigDecimal.ZERO : new BigDecimal("50"));

        BigDecimal total = cart.getSubtotal().add(cart.getTax()).add(cart.getShipping());
        if (cart.isGiftWrap()) {
            total = total.add(new BigDecimal("5"));
        }
        cart.setTotal(total);
    }
}
