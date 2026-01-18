package com.jewelry.backend.controller;

import com.jewelry.backend.dto.AddToCartRequest;
import com.jewelry.backend.dto.CartOptionsRequest;
import com.jewelry.backend.dto.CartResponse;
import com.jewelry.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(Principal principal) {
        return ResponseEntity.ok(cartService.getCartResponse(principal.getName()));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@RequestBody AddToCartRequest request, Principal principal) {
        return ResponseEntity.ok(cartService.addItemToCart(principal.getName(), request));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> updateQuantity(
            @PathVariable UUID itemId,
            @RequestBody Map<String, Integer> body,
            Principal principal) {
        return ResponseEntity.ok(cartService.updateItemQuantity(principal.getName(), itemId, body.get("quantity")));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Map<String, String>> removeItem(@PathVariable UUID itemId, Principal principal) {
        cartService.removeItem(principal.getName(), itemId);
        return ResponseEntity.ok(Map.of("message", "Item removed from cart"));
    }

    @PostMapping("/options")
    public ResponseEntity<CartResponse> updateOptions(@RequestBody CartOptionsRequest request, Principal principal) {
        return ResponseEntity.ok(cartService.updateCartOptions(principal.getName(), request.isGiftWrap()));
    }

    @PostMapping("/apply-coupon")
    public ResponseEntity<CartResponse> applyCoupon(@RequestBody Map<String, String> body, Principal principal) {
        return ResponseEntity.ok(cartService.applyCoupon(principal.getName(), body.get("couponCode")));
    }
}
