package com.jewelry.backend.repository;

import com.jewelry.backend.entity.TreasurePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TreasurePlanRepository extends JpaRepository<TreasurePlan, UUID> {
    Optional<TreasurePlan> findByUserIdAndStatus(UUID userId, String status);
}
