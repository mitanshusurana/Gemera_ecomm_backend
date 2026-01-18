package com.jewelry.backend.service;

import com.jewelry.backend.entity.Product;
import com.jewelry.backend.entity.User;
import com.jewelry.backend.entity.Wishlist;
import com.jewelry.backend.repository.ProductRepository;
import com.jewelry.backend.repository.UserRepository;
import com.jewelry.backend.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WishlistService {

    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    public Wishlist getWishlist(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist();
                    newWishlist.setUser(user);
                    return wishlistRepository.save(newWishlist);
                });
    }

    public void addItem(String email, UUID productId) {
        Wishlist wishlist = getWishlist(email);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        wishlist.getProducts().add(product);
        wishlistRepository.save(wishlist);
    }

    public void removeItem(String email, UUID productId) {
        Wishlist wishlist = getWishlist(email);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        wishlist.getProducts().remove(product);
        wishlistRepository.save(wishlist);
    }
}
