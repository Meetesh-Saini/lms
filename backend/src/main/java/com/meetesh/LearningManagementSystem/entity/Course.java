package com.meetesh.LearningManagementSystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CourseId")
    private Long courseId;

    @Column(name = "Title", nullable = false, length = 255)
    private String title;

    @Column(name = "Description", nullable = false, length = 1000)
    private String description;

    @Column(name = "Category", nullable = false, length = 100)
    private String category;

    @ManyToOne
    @JoinColumn(name = "InstructorId", nullable = false)
//    @JsonIgnore
    private User instructor;

    @Column(name = "CreatedDate", nullable = false)
    private Date createdDate;

    @Column(name = "Status", nullable = false, length = 50)
    private String status;
}
