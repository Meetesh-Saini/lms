package com.meetesh.LearningManagementSystem.dto;

import com.meetesh.LearningManagementSystem.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private Long id;
    private String name;
    private Collection<PrivilegeDTO> privileges;

    // Constructor to convert from Role entity to RoleDTO
    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        // Map privileges to PrivilegeDTO
        this.privileges = role.getPrivileges().stream()
                .map(PrivilegeDTO::new)
                .collect(Collectors.toList());
    }
}

