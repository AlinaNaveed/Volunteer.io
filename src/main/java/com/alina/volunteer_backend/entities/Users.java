package com.alina.volunteer_backend.entities;

import com.alina.volunteer_backend.entities.Organization;
import com.alina.volunteer_backend.entities.Volunteer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean termsAccepted;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Volunteer volunteer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Organization organization;
}
