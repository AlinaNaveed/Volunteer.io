package com.alina.volunteer_backend.entities;

import com.alina.volunteer_backend.entities.Users;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Volunteer {
    @Id
    private Long id;

    private String firstName;
    private String lastName;
    private String bio;
    private String skills;
//    private String location;
//    private String availability;// e.g., "Weekends", "Full-time"
//    private String title;
//    private String volunteerPerference;


    @OneToOne
    @MapsId
    private Users user;
}
