package com.jewelry.backend.controller;

import com.jewelry.backend.dto.AddToCartRequest;
import com.jewelry.backend.dto.ApplyCouponRequest;
import com.jewelry.backend.dto.CartOptionsRequest;
import com.jewelry.backend.entity.Cart;
import com.jewelry.backend.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Cart", description = "Cart management APIs")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping
    @Operation(summary = "Get current user's cart")
    public ResponseEntity<Cart> getCart(Principal principal) {
        return ResponseEntity.ok(cartService.getCart(principal.getName()));
    }

    @PostMapping("/items")
    @Operation(summary = "Add item to cart")
    public ResponseEntity<Cart> addItem(@RequestBody AddToCartRequest request, Principal principal) {
        return ResponseEntity.ok(cartService.addItemToCart(principal.getName(), request));
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "Update item quantity")
    public ResponseEntity<Cart> updateQuantity(
            @PathVariable UUID itemId,
            @RequestBody Map<String, Integer> body,
            Principal principal) {
        return ResponseEntity.ok(cartService.updateItemQuantity(principal.getName(), itemId, body.get("quantity")));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Remove item from cart")
    public ResponseEntity<Cart> removeItem(@PathVariable UUID itemId, Principal principal) {
        return ResponseEntity.ok(cartService.removeItem(principal.getName(), itemId));
    }

    @PostMapping("/apply-coupon")
    @Operation(summary = "Apply discount code")
    public ResponseEntity<Cart> applyCoupon(@RequestBody ApplyCouponRequest request, Principal principal) {
        return ResponseEntity.ok(cartService.applyCoupon(principal.getName(), request.getCode()));
    }

    @PostMapping("/options")
    @Operation(summary = "Update cart options (e.g. Gift Wrap)")
    public ResponseEntity<Cart> updateOptions(@RequestBody CartOptionsRequest request, Principal principal) {
        return ResponseEntity.ok(cartService.updateCartOptions(principal.getName(), request.isGiftWrap()));
    }

    @PostMapping("/wishlist")
    @Operation(summary = "Add item to wishlist")
    public ResponseEntity<Cart> addToWishlist(@RequestBody Map<String, String> body, Principal principal) {
        return ResponseEntity.ok(cartService.addToWishlist(principal.getName(), UUID.fromString(body.get("productId"))));
    }

    @DeleteMapping("/wishlist/{productId}")
    @Operation(summary = "Remove item from wishlist")
    public ResponseEntity<Cart> removeFromWishlist(@PathVariable UUID productId, Principal principal) {
        return ResponseEntity.ok(cartService.removeFromWishlist(principal.getName(), productId));
    }
}
