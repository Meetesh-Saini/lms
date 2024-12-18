package com.meetesh.LearningManagementSystem.controller;

import com.meetesh.LearningManagementSystem.entry.ApiResponse;
import com.meetesh.LearningManagementSystem.entry.LoginRequest;
import com.meetesh.LearningManagementSystem.entry.RegisterRequest;
import com.meetesh.LearningManagementSystem.exception.UserAlreadyExistsException;
import com.meetesh.LearningManagementSystem.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            ApiResponse<Map<String, String>> response = new ApiResponse<>(
                    Map.of("token", token),
                    "Login successful",
                    true,
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex) {
            ApiResponse<String> errorResponse = new ApiResponse<>(
                    "Invalid username or password",
                    "Authentication failed",
                    false,
                    HttpStatus.UNAUTHORIZED.value()
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }




    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Validation failed
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            ApiResponse<Map<String, String>> response = new ApiResponse<>(errors, "Validation failed", false, HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            authService.register(registerRequest);
            ApiResponse<String> response = new ApiResponse<>("User registered successfully", "Registration successful", true, HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException ex) {
            // Handle user already exists exception
            ApiResponse<String> response = new ApiResponse<>(null, ex.getMessage(), false, HttpStatus.CONFLICT.value());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginForm() {
        return ResponseEntity.ok("Hello!");
    }
}

