package com.meetesh.LearningManagementSystem.dto;

import com.meetesh.LearningManagementSystem.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer id;
    private String username;
    private Boolean isActive;
    private Collection<RoleDTO> roles;

    // Constructor to convert from User entity to UserDTO
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.isActive = user.getIsActive();
        // Map roles to RoleDTO
        this.roles = user.getRoles().stream()
                .map(RoleDTO::new)
                .collect(Collectors.toList());
    }
}