package com.jewelry.backend.repository;

import com.jewelry.backend.entity.User;
import com.jewelry.backend.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {
    Optional<Wishlist> findByUser(User user);
}
