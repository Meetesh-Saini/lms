package com.meetesh.LearningManagementSystem.entry;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentEntry {
    @NotNull(message = "Course not found.")
    private Long courseId;
}