package com.meetesh.LearningManagementSystem.service;

import com.meetesh.LearningManagementSystem.entity.User;
import com.meetesh.LearningManagementSystem.entry.RegisterRequest;
import com.meetesh.LearningManagementSystem.exception.UserAlreadyExistsException;
import com.meetesh.LearningManagementSystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setIsActive(true);
        Optional.ofNullable(registerRequest.getPhone())
                .ifPresent(user::setPhoneNumber);

        userRepository.save(user);
    }
}
