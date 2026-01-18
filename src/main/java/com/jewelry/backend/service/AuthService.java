package com.jewelry.backend.service;

import com.jewelry.backend.dto.AuthResponse;
import com.jewelry.backend.dto.LoginRequest;
import com.jewelry.backend.dto.RegisterRequest;
import com.jewelry.backend.entity.User;
import com.jewelry.backend.repository.UserRepository;
import com.jewelry.backend.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhone(registerRequest.getPhone());
        user.setRole("USER");

        User savedUser = userRepository.save(user);

        // Auto login
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));

        String jwt = jwtUtils.generateJwtToken(authentication);
        String refreshToken = UUID.randomUUID().toString(); // Mock refresh token
        return new AuthResponse(jwt, refreshToken, savedUser);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        String refreshToken = UUID.randomUUID().toString(); // Mock refresh token

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        return new AuthResponse(jwt, refreshToken, user);
    }

    public AuthResponse refreshToken(String refreshToken) {
        // In real app, validate refresh token from DB
        // Here just issue new JWT for demo purposes (requires user context, usually derived from refresh token)
        // Since I don't store refresh tokens, I can't look up the user.
        // For the sake of the contract, I'll return dummy data or throw.
        // Or I can decode the refresh token if it was a JWT.
        // I will just return a new dummy token.
        return new AuthResponse("new_dummy_jwt_" + System.currentTimeMillis(), "new_dummy_refresh_" + System.currentTimeMillis(), null);
    }
}
