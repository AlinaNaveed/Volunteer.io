package com.alina.volunteer_backend.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        // Example logic: check email or some field to determine role
        String email = oauth2User.getAttribute("email");

        String role;
        if (email.endsWith("@volunteer.com")) {
            role = "ROLE_VOLUNTEER";
        } else if (email.endsWith("@organization.com")) {
            role = "ROLE_ORGANIZATION";
        } else {
            role = "ROLE_USER"; // default
        }

        // Wrap the user with role
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        return new DefaultOAuth2User(authorities, oauth2User.getAttributes(), "id");
//        return new DefaultOAuth2User(authorities, attributes, "id");  // or "login"

    }
}
