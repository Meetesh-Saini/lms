package com.meetesh.LearningManagementSystem.service;


import com.meetesh.LearningManagementSystem.entry.RegisterRequest;
import com.meetesh.LearningManagementSystem.utilities.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import  org.springframework.security.core.Authentication;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public String login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
//        System.out.println(authentication.getPrincipal());
//        System.out.println(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenUtil.generateToken((User) authentication.getPrincipal());
    }

    public void register(RegisterRequest registerRequest) {
        userService.createUser(registerRequest);
    }
}
