package com.alina.volunteer_backend.dto;

import lombok.Data;

@Data
public class AdminRegisterRequest {
    private String email;
    private String password;
}

