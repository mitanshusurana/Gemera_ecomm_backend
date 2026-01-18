package com.jewelry.backend.service;

import com.jewelry.backend.dto.CategoryResponse;
import com.jewelry.backend.dto.ProductDetailResponse;
import com.jewelry.backend.dto.ProductResponse;
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
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public Page<ProductResponse> getAllProducts(String category, BigDecimal priceMin, BigDecimal priceMax, String search, String occasions, String styles, Pageable pageable) {
        // Prepare search term for LIKE query
        // Ensure it is lowercase here to avoid LOWER(:search) in JPQL
        String searchTerm = search != null ? "%" + search.toLowerCase() + "%" : null;

        Page<Product> products = productRepository.findWithFilters(category, priceMin, priceMax, searchTerm, pageable);
        return products.map(this::mapToResponse);
    }

    public ProductDetailResponse getProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDetailResponse(product);
    }

    public List<CategoryResponse> getCategoryTree() {
        List<Category> roots = categoryRepository.findByParentIsNull();
        return roots.stream().map(this::mapToCategoryResponse).collect(Collectors.toList());
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setOriginalPrice(product.getOriginalPrice());
        response.setRating(product.getRating());
        response.setReviewCount(product.getReviewCount());
        // Use first image as main image
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            response.setImageUrl(product.getImages().get(0).getUrl());
        }
        response.setVideoUrl(product.getVideoUrl());

        if (product.getCategory() != null) {
            // If it has a parent, then current is subcategory, parent is category
            if (product.getCategory().getParent() != null) {
                response.setCategory(product.getCategory().getParent().getName());
                response.setSubcategory(product.getCategory().getName());
            } else {
                response.setCategory(product.getCategory().getName());
            }
        }

        response.setGemstones(product.getGemstones());
        response.setMetal(product.getMetal());
        response.setWeight(product.getWeight());
        response.setStock(product.getStock());
        response.setSku(product.getSku());
        response.setCertifications(product.getCertifications());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }

    private ProductDetailResponse mapToDetailResponse(Product product) {
        ProductDetailResponse response = new ProductDetailResponse();
        // Copy base fields
        ProductResponse base = mapToResponse(product);
        response.setId(base.getId());
        response.setName(base.getName());
        response.setDescription(base.getDescription());
        response.setPrice(base.getPrice());
        response.setOriginalPrice(base.getOriginalPrice());
        response.setRating(base.getRating());
        response.setReviewCount(base.getReviewCount());
        response.setImageUrl(base.getImageUrl());
        response.setVideoUrl(base.getVideoUrl());
        response.setCategory(base.getCategory());
        response.setSubcategory(base.getSubcategory());
        response.setGemstones(base.getGemstones());
        response.setMetal(base.getMetal());
        response.setWeight(base.getWeight());
        response.setStock(base.getStock());
        response.setSku(base.getSku());
        response.setCertifications(base.getCertifications());
        response.setCreatedAt(base.getCreatedAt());
        response.setUpdatedAt(base.getUpdatedAt());

        // Detail fields
        response.setImages(product.getImages());
        response.setSpecifications(product.getSpecifications());
        response.setRelatedProducts(product.getRelatedProducts());
        response.setPriceBreakup(product.getPriceBreakup());

        return response;
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDisplayName(category.getDisplayName());
        response.setImage(category.getImage());
        if (category.getSubcategories() != null) {
            response.setSubcategories(category.getSubcategories().stream()
                    .map(this::mapToCategoryResponse)
                    .collect(Collectors.toList()));
        }
        return response;
    }

    // Kept for backward compatibility if needed, though unused now
    public Map<String, List<CategoryResponse>> getCategories() {
        return null;
    }
}
