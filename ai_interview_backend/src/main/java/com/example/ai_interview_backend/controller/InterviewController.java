package com.example.ai_interview_backend.controller;

import com.example.ai_interview_backend.dto.InterviewRequest;
import com.example.ai_interview_backend.dto.InterviewSummaryResponse;
import com.example.ai_interview_backend.entity.InterviewHistory;
import com.example.ai_interview_backend.repository.InterviewHistoryRepository;
import com.example.ai_interview_backend.service.GeminiAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interview")
public class InterviewController {
    @Autowired
    private GeminiAIService geminiAIService;

    @Autowired
    private InterviewHistoryRepository historyRepository;

    @PostMapping("/next-question")
    public ResponseEntity<Map<String, String>> getNextQuestion(@RequestBody InterviewRequest request) {
        String nextQuestion = geminiAIService.getNextAiQuestion(
                request.getRole(),
                request.getDifficulty() != null ? request.getDifficulty() : "Medium",
                request.getCandidateAnswer());

        Map<String, String> response = new HashMap<>();
        response.put("question", nextQuestion);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/summary")
    public ResponseEntity<InterviewSummaryResponse> getInterviewSummary(@RequestBody InterviewRequest request) {
        InterviewSummaryResponse summary  = geminiAIService.generateInterviewSummary(request.getRole(), request.getCandidateAnswer());

        try {
            InterviewHistory history = new InterviewHistory(
                    request.getRole(),
                    request.getDifficulty() != null ? request.getDifficulty() : "Medium",
                    summary.getOverallScore(),
                    summary.getFeedback()
            );
            historyRepository.save(history);
            System.out.println("History saved successfully to database!");
        } catch (Exception e) {
            System.out.println("Error saving history: " + e.getMessage());
        }
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/history")
    public ResponseEntity<List<InterviewHistory>> getAllHistory() {
        return ResponseEntity.ok(historyRepository.findAll());
    }
}
