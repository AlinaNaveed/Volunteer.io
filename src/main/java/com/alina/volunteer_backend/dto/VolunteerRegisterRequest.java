package com.alina.volunteer_backend.dto;

import lombok.Data;

@Data
public class VolunteerRegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String bio;
    private String skills;
    private boolean termsAccepted;
}

