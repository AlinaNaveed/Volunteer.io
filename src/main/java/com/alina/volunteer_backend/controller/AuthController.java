package com.alina.volunteer_backend.controller;

import com.alina.volunteer_backend.JwtResponse;
import com.alina.volunteer_backend.dto.*;
import com.alina.volunteer_backend.entities.Organization;
import com.alina.volunteer_backend.entities.Role;
import com.alina.volunteer_backend.entities.Users;
import com.alina.volunteer_backend.entities.Volunteer;
import com.alina.volunteer_backend.repositories.OrganizationRepository;
import com.alina.volunteer_backend.repositories.UserRepository;
import com.alina.volunteer_backend.repositories.VolunteerRepository;
import com.alina.volunteer_backend.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register/volunteer")
    public ResponseEntity<?> registerVolunteer(@RequestBody VolunteerRegisterRequest request) {
        logger.info("Attempting to register volunteer with email: {}", request.getEmail());

        if (!request.isTermsAccepted()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "You must accept the terms and conditions.", null));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Email already exists.", null));
        }

        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.VOLUNTEER);
        user.setTermsAccepted(true);
        userRepository.save(user);

        Volunteer volunteer = new Volunteer();
        volunteer.setFirstName(request.getFirstName());
        volunteer.setLastName(request.getLastName());
        volunteer.setUser(user);
        volunteer.setBio(request.getBio());
        volunteer.setSkills(request.getSkills());

        volunteerRepository.save(volunteer);

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Volunteer registered successfully", new JwtResponse(token)));
    }

    @PostMapping("/register/organization")
    public ResponseEntity<?> registerOrganization(@RequestBody OrganizationRegisterRequest request) {
        logger.info("Attempting to register organization with email: {}", request.getEmail());

        if (!request.isTermsAccepted()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "You must accept the terms and conditions.", null));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Email already exists.", null));
        }

        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ORGANIZATION);
        user.setTermsAccepted(true);
        userRepository.save(user);

        Organization org = new Organization();
        org.setOrganizationName(request.getOrganizationName());
        org.setDescription(request.getDescription());
        org.setUser(user);

        organizationRepository.save(org);

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Organization registered successfully", new JwtResponse(token)));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for email: {}", loginRequest.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()
                    )
            );
            Users user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", new LoginResponse(token)));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Invalid credentials", null));
        }
    }

    @PostMapping("/oauth")
    public ResponseEntity<?> receiveOAuthUser(@RequestBody Map<String, Object> userData) {
        logger.info("OAuth user data received: {}", userData);
        return ResponseEntity.ok(new ApiResponse<>(true, "OAuth data received", userData));
    }
}
