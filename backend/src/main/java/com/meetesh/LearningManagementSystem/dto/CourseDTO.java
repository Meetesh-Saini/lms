package com.meetesh.LearningManagementSystem.dto;

import com.meetesh.LearningManagementSystem.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private Long courseId;
    private String title;
    private String description;
    private String category;
    private UserDTO instructor;
    private Date createdDate;
    private String status;

    // Constructor to convert from Course entity to CourseDTO
    public CourseDTO(Course course) {
        this.courseId = course.getCourseId();
        this.title = course.getTitle();
        this.description = course.getDescription();
        this.category = course.getCategory();
        // Map User (instructor) to UserDTO
        this.instructor = new UserDTO(course.getInstructor());
        this.createdDate = course.getCreatedDate();
        this.status = course.getStatus();
    }
}
