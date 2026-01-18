package com.jewelry.backend.controller;

import com.jewelry.backend.dto.ProductDetailResponse;
import com.jewelry.backend.dto.ProductResponse;
import com.jewelry.backend.entity.Product;
import com.jewelry.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String occasions,
            @RequestParam(required = false) String styles,
            Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(category, priceMin, priceMax, search, occasions, styles, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories() {
        return ResponseEntity.ok(Collections.singletonMap("categories", productService.getCategoryTree()));
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProducts(
            @RequestParam String query,
            Pageable pageable) {
        Page<ProductResponse> results = productService.getAllProducts(null, null, null, query, null, null, pageable);
        return ResponseEntity.ok(Collections.singletonMap("results", results.getContent()));
    }

    // Helper to seed data - accepting generic Product for now, though it might fail if JSON doesn't match exactly Entity
    // This endpoint was in original code, keeping it for utility
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.status(201).body(productService.createProduct(product));
    }
}
