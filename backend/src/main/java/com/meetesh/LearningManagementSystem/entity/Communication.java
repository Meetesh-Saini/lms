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
@Table(name = "Communications")
public class Communication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MessageId")
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "SenderId", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "ReceiverId", nullable = false)
    private User receiver;

    @Column(name = "Message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "DateSent", nullable = false)
    private Date dateSent;

    @Column(name = "Type", nullable = false, length = 50)
    private String type;
}

