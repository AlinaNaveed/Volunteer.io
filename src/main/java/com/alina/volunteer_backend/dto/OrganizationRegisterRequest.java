package com.alina.volunteer_backend.dto;

import lombok.Data;

@Data
public class OrganizationRegisterRequest {
    private String organizationName;
    private String email;
    private String password;
    private boolean termsAccepted;
    private String description;
}

