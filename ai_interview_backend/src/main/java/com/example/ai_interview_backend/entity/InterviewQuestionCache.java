package com.example.ai_interview_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "interview_question_cache")
public class InterviewQuestionCache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;
    private String difficulty;

    @Column(columnDefinition = "TEXT")
    private String questionText;

    public InterviewQuestionCache() {}

    public InterviewQuestionCache(String role, String difficulty, String questionText){
        this.role = role;
        this.difficulty = difficulty;
        this.questionText = questionText;
    }

    //Getter and Setter method
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    public String getQuestionText() {
        return questionText;
    }
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
}
