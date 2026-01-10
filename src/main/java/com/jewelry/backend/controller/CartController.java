package com.jewelry.backend.controller;

import com.jewelry.backend.dto.AddToCartRequest;
import com.jewelry.backend.dto.CartOptionsRequest;
import com.jewelry.backend.entity.Cart;
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
    public ResponseEntity<Cart> getCart(Principal principal) {
        return ResponseEntity.ok(cartService.getCart(principal.getName()));
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addItem(@RequestBody AddToCartRequest request, Principal principal) {
        return ResponseEntity.ok(cartService.addItemToCart(principal.getName(), request));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<Cart> updateQuantity(
            @PathVariable UUID itemId,
            @RequestBody Map<String, Integer> body,
            Principal principal) {
        return ResponseEntity.ok(cartService.updateItemQuantity(principal.getName(), itemId, body.get("quantity")));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Cart> removeItem(@PathVariable UUID itemId, Principal principal) {
        return ResponseEntity.ok(cartService.removeItem(principal.getName(), itemId));
    }

    @PostMapping("/options")
    public ResponseEntity<Cart> updateOptions(@RequestBody CartOptionsRequest request, Principal principal) {
        return ResponseEntity.ok(cartService.updateCartOptions(principal.getName(), request.isGiftWrap()));
    }
}
