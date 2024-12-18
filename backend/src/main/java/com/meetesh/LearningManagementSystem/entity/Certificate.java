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
@Table(name = "Certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CertificateId")
    private Long certificateId;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "CourseId", nullable = false)
    private Course course;

    @Column(name = "DateIssued", nullable = false)
    private Date dateIssued;

    @Column(name = "CertificatePath", nullable = false, length = 255)
    private String certificatePath;
}
