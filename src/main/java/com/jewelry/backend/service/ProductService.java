package com.jewelry.backend.service;

import com.jewelry.backend.dto.CategoryResponse;
import com.jewelry.backend.entity.Category;
import com.jewelry.backend.entity.Product;
import com.jewelry.backend.repository.CategoryRepository;
import com.jewelry.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public Page<Product> getAllProducts(
            String category,
            BigDecimal priceMin,
            BigDecimal priceMax,
            String search,
            Pageable pageable) {

        return productRepository.findWithFilters(category, priceMin, priceMax, search, pageable);
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public CategoryResponse getCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        // Return only root categories to avoid duplication (children are included in parents)
        List<Category> roots = allCategories.stream()
                .filter(c -> c.getParent() == null)
                .collect(Collectors.toList());
        return new CategoryResponse(roots);
    }

    // Admin only - strictly for seeding/testing
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}
