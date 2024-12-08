package com.chubb.LearningManagementSystem.entry;

import lombok.Data;

@Data
public class CourseContentEntry {
    private Long moduleId;
    private String type; // e.g., "image", "pdf", "video"
}