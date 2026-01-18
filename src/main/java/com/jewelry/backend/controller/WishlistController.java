package com.jewelry.backend.controller;

import com.jewelry.backend.dto.CartResponse;
import com.jewelry.backend.entity.Wishlist;
import com.jewelry.backend.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/wishlist")
public class WishlistController {

    @Autowired
    WishlistService wishlistService;

    @PostMapping("/items")
    public ResponseEntity<Map<String, String>> addItem(@RequestBody Map<String, UUID> body, Principal principal) {
        wishlistService.addItem(principal.getName(), body.get("productId"));
        return ResponseEntity.ok(Map.of("message", "Item added to wishlist"));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Map<String, String>> removeItem(@PathVariable UUID productId, Principal principal) {
        wishlistService.removeItem(principal.getName(), productId);
        return ResponseEntity.ok(Map.of("message", "Item removed from wishlist"));
    }
}
