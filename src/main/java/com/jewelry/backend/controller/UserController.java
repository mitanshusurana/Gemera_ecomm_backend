package com.jewelry.backend.controller;

import com.jewelry.backend.entity.User;
import com.jewelry.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<User> getMe(Principal principal) {
        return ResponseEntity.ok(userRepository.findByEmail(principal.getName()).orElseThrow());
    }
}
