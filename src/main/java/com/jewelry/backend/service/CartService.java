package com.jewelry.backend.service;

import com.jewelry.backend.dto.AddToCartRequest;
import com.jewelry.backend.dto.CartResponse;
import com.jewelry.backend.entity.Cart;
import com.jewelry.backend.entity.CartItem;
import com.jewelry.backend.entity.Product;
import com.jewelry.backend.entity.User;
import com.jewelry.backend.entity.Wishlist;
import com.jewelry.backend.repository.CartItemRepository;
import com.jewelry.backend.repository.CartRepository;
import com.jewelry.backend.repository.ProductRepository;
import com.jewelry.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WishlistService wishlistService;

    @Transactional
    public CartResponse getCartResponse(String userEmail) {
        Cart cart = getCartEntity(userEmail);
        return mapToResponse(cart, userEmail);
    }

    @Transactional
    public CartResponse addItemToCart(String userEmail, AddToCartRequest request) {
        Cart cart = getCartEntity(userEmail);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setProduct(product);
        newItem.setQuantity(request.getQuantity());

        // Map options
        if (request.getOptions() != null) {
            CartItem.CartItemOptions opts = new CartItem.CartItemOptions();
            if (request.getOptions().getMetal() != null) opts.setMetal(request.getOptions().getMetal().getName());
            if (request.getOptions().getDiamond() != null) opts.setDiamond(request.getOptions().getDiamond().getName());
            opts.setCustomization(request.getOptions().getEngraving());
            newItem.setOptions(opts);
        }

        cart.getItems().add(newItem);
        cartItemRepository.save(newItem);

        recalculateCart(cart);
        cart = cartRepository.save(cart);
        return mapToResponse(cart, userEmail);
    }

    @Transactional
    public CartResponse updateItemQuantity(String userEmail, UUID itemId, int quantity) {
        if (quantity <= 0) {
            return removeItem(userEmail, itemId);
        }

        Cart cart = getCartEntity(userEmail);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
             throw new RuntimeException("Item does not belong to user cart");
        }

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        recalculateCart(cart);
        cart = cartRepository.save(cart);
        return mapToResponse(cart, userEmail);
    }

    @Transactional
    public CartResponse removeItem(String userEmail, UUID itemId) {
        Cart cart = getCartEntity(userEmail);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
             throw new RuntimeException("Item does not belong to user cart");
        }

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        recalculateCart(cart);
        cart = cartRepository.save(cart);
        return mapToResponse(cart, userEmail);
    }

    @Transactional
    public CartResponse applyCoupon(String userEmail, String code) {
        Cart cart = getCartEntity(userEmail);
        BigDecimal discount = BigDecimal.ZERO;

        if ("SAVE20".equals(code)) {
            // Calculate 20% discount on subtotal
            discount = cart.getSubtotal().multiply(new BigDecimal("0.20"));
        }

        CartResponse response = mapToResponse(cart, userEmail);

        // Temporarily apply discount to response since we aren't persisting it
        if (discount.compareTo(BigDecimal.ZERO) > 0) {
            response.setAppliedDiscount(discount);
            response.setTotal(response.getTotal().subtract(discount));
        }

        return response;
    }

    @Transactional
    public CartResponse updateCartOptions(String userEmail, boolean giftWrap) {
        Cart cart = getCartEntity(userEmail);
        cart.setGiftWrap(giftWrap);
        recalculateCart(cart);
        cart = cartRepository.save(cart);
        return mapToResponse(cart, userEmail);
    }

    private Cart getCartEntity(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
    }

    private void recalculateCart(Cart cart) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            BigDecimal price = item.getProduct().getPrice();
            subtotal = subtotal.add(price.multiply(new BigDecimal(item.getQuantity())));
        }
        cart.setSubtotal(subtotal);
        cart.setTax(subtotal.multiply(new BigDecimal("0.10")));
        cart.setShipping(subtotal.compareTo(new BigDecimal("100000")) > 0 ? BigDecimal.ZERO : new BigDecimal("500"));
        cart.setTotal(cart.getSubtotal().add(cart.getTax()).add(cart.getShipping()));
    }

    private CartResponse mapToResponse(Cart cart, String email) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setSubtotal(cart.getSubtotal());
        response.setTax(cart.getTax());
        response.setShipping(cart.getShipping());
        response.setTotal(cart.getTotal());
        response.setAppliedDiscount(BigDecimal.ZERO); // Default

        response.setItems(cart.getItems().stream().map(item -> {
            CartResponse.CartItemResponse itemResp = new CartResponse.CartItemResponse();
            itemResp.setId(item.getId());
            itemResp.setProductId(item.getProduct().getId());
            itemResp.setQuantity(item.getQuantity());
            itemResp.setPrice(item.getProduct().getPrice());

            CartResponse.CartProductResponse prodResp = new CartResponse.CartProductResponse();
            prodResp.setId(item.getProduct().getId());
            prodResp.setName(item.getProduct().getName());
            if (!item.getProduct().getImages().isEmpty()) {
                prodResp.setImageUrl(item.getProduct().getImages().get(0).getUrl());
            }
            itemResp.setProduct(prodResp);

            if (item.getOptions() != null) {
                if (item.getOptions().getMetal() != null)
                    itemResp.setSelectedMetal(new CartResponse.SelectedOption(item.getOptions().getMetal()));
                if (item.getOptions().getDiamond() != null)
                    itemResp.setSelectedDiamond(new CartResponse.SelectedOption(item.getOptions().getDiamond()));
            }
            return itemResp;
        }).collect(Collectors.toList()));

        // Wishlist
        Wishlist wishlist = wishlistService.getWishlist(email);
        response.setWishlist(wishlist.getProducts().stream().map(p -> {
            CartResponse.WishlistItemResponse wItem = new CartResponse.WishlistItemResponse();
            wItem.setId(p.getId());
            wItem.setName(p.getName());
            wItem.setPrice(p.getPrice());
            if (!p.getImages().isEmpty()) {
                wItem.setImageUrl(p.getImages().get(0).getUrl());
            }
            return wItem;
        }).collect(Collectors.toList()));

        return response;
    }
}
