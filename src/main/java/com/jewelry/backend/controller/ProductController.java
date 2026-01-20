package com.jewelry.backend.controller;

import com.jewelry.backend.dto.CategoryResponse;
import com.jewelry.backend.entity.Product;
import com.jewelry.backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*")
@Tag(name = "Products", description = "Product catalog APIs")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    @Operation(summary = "Get paginated products")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            @RequestParam(required = false) String search,
            @Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(category, priceMin, priceMax, search, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product details")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all categories")
    public ResponseEntity<CategoryResponse> getCategories() {
        return ResponseEntity.ok(productService.getCategories());
    }

    @GetMapping("/search")
    @Operation(summary = "Search products")
    public ResponseEntity<Map<String, List<Product>>> searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        Page<Product> page = productService.getAllProducts(null, null, null, query, PageRequest.of(0, limit));
        return ResponseEntity.ok(Map.of("results", page.getContent()));
    }

    // Helper to seed data
    @PostMapping
    @Operation(summary = "Create product (Admin)")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.status(201).body(productService.createProduct(product));
    }
}
