package com.example.ai_interview_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "interview_sessions")
@Data
public class InterviewSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    @Column(columnDefinition = "TEXT")
    private String candidateAnswer;

    @Column(columnDefinition = "TEXT")
    private String aiResponseQuestion;
}
