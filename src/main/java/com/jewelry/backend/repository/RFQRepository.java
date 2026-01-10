package com.jewelry.backend.repository;

import com.jewelry.backend.entity.RFQ;
import com.jewelry.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface RFQRepository extends JpaRepository<RFQ, UUID> {
    List<RFQ> findByUser(User user);
}
