package com.example.ai_interview_backend.dto;

public class InterviewRequest {
    private String role;
    private String candidateAnswer;
    private String difficulty;
    private String transcript;

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public String getCandidateAnswer() {
        return candidateAnswer;
    }
    public void setCandidateAnswer(String candidateAnswer) {
        this.candidateAnswer = candidateAnswer;
    }

    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTranscript() {
        return transcript;
    }
    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }
}
