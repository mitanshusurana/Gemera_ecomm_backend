package com.jewelry.backend.controller;

import com.jewelry.backend.entity.Address;
import com.jewelry.backend.entity.User;
import com.jewelry.backend.repository.UserRepository;
import com.jewelry.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<User> getMe(Principal principal) {
        return ResponseEntity.ok(userRepository.findByEmail(principal.getName()).orElseThrow());
    }

    @PutMapping("/profile")
    @Operation(summary = "Update user profile")
    public ResponseEntity<User> updateProfile(@RequestBody Map<String, Object> updates, Principal principal) {
        return ResponseEntity.ok(userService.updateUserProfile(principal.getName(), updates));
    }

    @GetMapping("/loyalty")
    @Operation(summary = "Get loyalty points")
    public ResponseEntity<Map<String, Object>> getLoyalty(Principal principal) {
        return ResponseEntity.ok(userService.getLoyalty(principal.getName()));
    }

    @PostMapping("/addresses")
    @Operation(summary = "Add address")
    public ResponseEntity<User> addAddress(@RequestBody Address address, Principal principal) {
        return ResponseEntity.ok(userService.addAddress(principal.getName(), address));
    }

    @PutMapping("/addresses/{id}")
    @Operation(summary = "Update address")
    public ResponseEntity<User> updateAddress(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> updates,
            Principal principal) {
        return ResponseEntity.ok(userService.updateAddress(principal.getName(), id, updates));
    }

    @DeleteMapping("/addresses/{id}")
    @Operation(summary = "Delete address")
    public ResponseEntity<User> deleteAddress(@PathVariable UUID id, Principal principal) {
        return ResponseEntity.ok(userService.deleteAddress(principal.getName(), id));
    }
}
