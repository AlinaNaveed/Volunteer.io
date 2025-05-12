package com.alina.volunteer_backend.components;

import com.alina.volunteer_backend.entities.Users;
import com.alina.volunteer_backend.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<Users> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            Users user = new Users();
            user.setEmail(email);
            user.setPassword(null); // No password for OAuth users
            user.setProvider("google");
            userRepository.save(user);
        }

        // Redirect or respond
        response.sendRedirect("http://localhost:3000/volunteer/dashboard"); // Your frontend
    }
}
