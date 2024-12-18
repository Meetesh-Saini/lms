package com.meetesh.LearningManagementSystem.utilities;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final UserDetails principal;
    private String credentials;

    public JwtAuthenticationToken(UserDetails principal, String credentials) {
        super(principal.getAuthorities());
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true);  // Marks the token as authenticated
    }

    @Override
    public Object getCredentials() {
        return credentials;  // Can be set as null since JWT doesn't require a password
    }

    @Override
    public Object getPrincipal() {
        return principal;  // The user details object
    }
}
