package com.alina.volunteer_backend.dto;

import lombok.Data;
import lombok.Getter;

@Getter
public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    // Getter
}


