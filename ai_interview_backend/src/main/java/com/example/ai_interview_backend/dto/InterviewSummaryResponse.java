package com.example.ai_interview_backend.dto;

import java.util.List;

public class InterviewSummaryResponse {
    private String role;
    private int overallScore;
    private String feedback;
    private List<String> strengths;
    private List<String> improvements;

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public int getOverallScore() {
        return overallScore;
    }
    public void setOverallScore(int overallScore) {
        this.overallScore = overallScore;
    }

    public String getFeedback() {
        return feedback;
    }
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public List<String> getStrengths() {
        return strengths;
    }
    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getImprovements() {
        return improvements;
    }
    public void setImprovements(List<String> improvements) {
        this.improvements = improvements;
    }
}
