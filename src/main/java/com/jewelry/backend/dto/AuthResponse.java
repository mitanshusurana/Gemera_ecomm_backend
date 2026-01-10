package com.jewelry.backend.dto;

import com.jewelry.backend.entity.User;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String refreshToken; // dummy for now
    private User user;

    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
        this.refreshToken = "dummy-refresh-token";
    }
}
