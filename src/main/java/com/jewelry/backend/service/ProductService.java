package com.jewelry.backend.service;

import com.jewelry.backend.entity.Product;
import com.jewelry.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Page<Product> getAllProducts(
            String category,
            BigDecimal priceMin,
            BigDecimal priceMax,
            String search,
            String occasions,
            String styles,
            Pageable pageable) {

        String effectiveSearch = search;
        if (occasions != null && !occasions.isEmpty()) {
            effectiveSearch = (effectiveSearch == null ? "" : effectiveSearch + " ") + occasions.replace(",", " ");
        }
        if (styles != null && !styles.isEmpty()) {
            effectiveSearch = (effectiveSearch == null ? "" : effectiveSearch + " ") + styles.replace(",", " ");
        }

        return productRepository.findWithFilters(category, priceMin, priceMax, effectiveSearch != null && effectiveSearch.isBlank() ? null : effectiveSearch, pageable);
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Map<String, List<String>> getCategories() {
        Map<String, List<String>> response = new HashMap<>();
        response.put("categories", productRepository.findAllCategories());
        return response;
    }

    // Admin only - strictly for seeding/testing
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}
