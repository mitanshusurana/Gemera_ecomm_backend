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

    @Query("SELECT p FROM Product p LEFT JOIN p.category c WHERE " +
            "(:category IS NULL OR c.name = :category OR c.parent.name = :category) AND " +
            "(:priceMin IS NULL OR p.price >= :priceMin) AND " +
            "(:priceMax IS NULL OR p.price <= :priceMax) AND " +
            "(:search IS NULL OR " +
            "LOWER(p.name) LIKE :search OR " +
            "LOWER(p.description) LIKE :search)")
    Page<Product> findWithFilters(
            @Param("category") String category,
            @Param("priceMin") BigDecimal priceMin,
            @Param("priceMax") BigDecimal priceMax,
            @Param("search") String search,
            Pageable pageable);
}
