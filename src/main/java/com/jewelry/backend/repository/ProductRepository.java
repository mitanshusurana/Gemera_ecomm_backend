package com.jewelry.backend.repository;

import com.jewelry.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query(value = "SELECT p.* FROM products p " +
            "LEFT JOIN categories c ON p.category_id = c.id " +
            "LEFT JOIN categories c_parent ON c.parent_id = c_parent.id " +
            "WHERE " +
            "(:category IS NULL OR c.name = :category OR c_parent.name = :category) AND " +
            "(:priceMin IS NULL OR p.price >= :priceMin) AND " +
            "(:priceMax IS NULL OR p.price <= :priceMax) AND " +
            "(:search IS NULL OR p.name ILIKE :search OR p.description ILIKE :search)",
            countQuery = "SELECT count(*) FROM products p " +
            "LEFT JOIN categories c ON p.category_id = c.id " +
            "LEFT JOIN categories c_parent ON c.parent_id = c_parent.id " +
            "WHERE " +
            "(:category IS NULL OR c.name = :category OR c_parent.name = :category) AND " +
            "(:priceMin IS NULL OR p.price >= :priceMin) AND " +
            "(:priceMax IS NULL OR p.price <= :priceMax) AND " +
            "(:search IS NULL OR p.name ILIKE :search OR p.description ILIKE :search)",
            nativeQuery = true)
    Page<Product> findWithFilters(
            @Param("category") String category,
            @Param("priceMin") BigDecimal priceMin,
            @Param("priceMax") BigDecimal priceMax,
            @Param("search") String search,
            Pageable pageable);
}
