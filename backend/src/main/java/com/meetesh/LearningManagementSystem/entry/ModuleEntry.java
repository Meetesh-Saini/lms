package com.meetesh.LearningManagementSystem.entry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleEntry {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Course ID is mandatory")
    private Long courseId;
}