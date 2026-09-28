package com.example.ai_interview_backend.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_history")
public class InterviewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;
    private String difficulty;
    private int overallScore;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    private LocalDateTime interviewDate = LocalDateTime.now();

    public InterviewHistory() {

    }

    public InterviewHistory(String role, String difficulty, int overallScore, String feedback) {
        this.role = role;
        this.difficulty = difficulty;
        this.overallScore = overallScore;
        this.feedback = feedback;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}
    public String getDifficulty() {return difficulty;}
    public void setDifficulty(String difficulty) {this.difficulty = difficulty;}
    public int getOverallScore() {return overallScore;}
    public void setOverallScore(int overallScore) {this.overallScore = overallScore;}
    public String getFeedback() {return feedback;}
    public void setFeedback(String feedback) {this.feedback = feedback;}
    public LocalDateTime getInterviewDate() {return interviewDate;}
    public void setInterviewDate(LocalDateTime interviewDate) {this.interviewDate = interviewDate;}

}
