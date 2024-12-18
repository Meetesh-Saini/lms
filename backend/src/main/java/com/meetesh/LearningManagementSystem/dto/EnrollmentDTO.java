package com.meetesh.LearningManagementSystem.dto;

import com.meetesh.LearningManagementSystem.entity.Enrollment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {
    private Long enrollmentId;
    private CourseDTO course;
    private UserDTO user;
    private Date enrollmentDate;

    public EnrollmentDTO(Enrollment enrollment) {
        this.enrollmentId = enrollment.getEnrollmentId();
        this.course = new CourseDTO(enrollment.getCourse());
        this.user = new UserDTO(enrollment.getStudent());
        this.enrollmentDate = enrollment.getEnrollmentDate();
    }
}
