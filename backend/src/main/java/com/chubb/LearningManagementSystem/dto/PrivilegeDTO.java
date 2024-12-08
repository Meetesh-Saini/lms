package com.chubb.LearningManagementSystem.dto;

import com.chubb.LearningManagementSystem.entity.Privilege;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeDTO {

    private Long id;
    private String name;

    // Constructor to convert from Privilege entity to PrivilegeDTO
    public PrivilegeDTO(Privilege privilege) {
        this.id = privilege.getId();
        this.name = privilege.getName();
    }
}
