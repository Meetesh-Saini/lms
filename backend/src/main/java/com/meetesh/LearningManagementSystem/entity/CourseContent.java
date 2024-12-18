package com.meetesh.LearningManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CourseContent")
public class CourseContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ContentId")
    private Long contentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ModuleId", nullable = false)
    @JsonIgnore
    private Module module;

    @Column(name = "Type", nullable = false, length = 50)
    private String type;

    @Column(name = "FilePath", nullable = false, length = 255)
    private String filePath;

    @Column(name = "UploadDate", nullable = false)
    private Date uploadDate;
}
