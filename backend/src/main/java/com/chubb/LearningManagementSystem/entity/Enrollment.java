package com.chubb.LearningManagementSystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EnrollmentId")
    private Long enrollmentId;

    @ManyToOne
    @JoinColumn(name = "CourseId", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    private User student;

    @Column(name = "EnrollmentDate", nullable = false)
    private Date enrollmentDate;

    @Column(name = "Status", nullable = false, length = 50)
    private String status;
}
