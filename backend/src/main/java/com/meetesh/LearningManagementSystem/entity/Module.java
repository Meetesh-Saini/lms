package com.meetesh.LearningManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Modules")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ModuleId")
    private Long moduleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CourseId", nullable = false)
    private Course course;

    @Column(name = "ModuleName", nullable = false, length = 255)
    private String moduleName;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CourseContent> contents;
}
