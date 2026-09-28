package com.example.ai_interview_backend.service;

import com.example.ai_interview_backend.dto.InterviewSummaryResponse;
import com.example.ai_interview_backend.entity.InterviewQuestionCache;
import com.example.ai_interview_backend.repository.InterviewQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GeminiAIService {

    @Value("${gemini.api.key:YOUR_API_KEY_HERE}")
    private String apiKey;

    private String getApiUrl() {
        return "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=" + apiKey;
    }
    @Autowired
    private InterviewQuestionRepository questionRepository;

    public String getNextAiQuestion(String role, String difficulty, String previousAnswer) {

        if (previousAnswer != null && previousAnswer.equals("Starting the mock interview session")) {
            List<InterviewQuestionCache> cachedList = questionRepository.findByRoleAndDifficulty(role, difficulty);
            if (cachedList != null && !cachedList.isEmpty()) {
                System.out.println("Fetching question from Database Cache...");
                return cachedList.get(0).getQuestionText();
            }
        }
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String prompt = "You are a tech interviewer for a " + role + " role conducting a " + difficulty + " level interview. " +
                "The candidate's last answer was: '" + previousAnswer + "'. " +
                "Ask the next logical technical interview question matching the " + difficulty + " level. Keep it concise.";

        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", Collections.singletonList(part));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", Collections.singletonList(content));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            System.out.println("Calling Gemini API for new question...");
            ResponseEntity<Map> response = restTemplate.postForEntity(getApiUrl(), entity, Map.class);

            Map body = response.getBody();
            List candidates = (List) body.get("candidates");
            Map candidate = (Map) candidates.get(0);
            Map contentMap = (Map) candidate.get("content");
            List parts = (List) contentMap.get("parts");
            Map textMap = (Map) parts.get(0);

            String generatedQuestion = (String) textMap.get("text");

            try {
                InterviewQuestionCache newCache = new InterviewQuestionCache(role, difficulty, generatedQuestion);
                questionRepository.save(newCache);
                System.out.println("New question saved to database cache!");
            } catch (Exception dbEx) {
                System.err.println("Error saving to cache: " + dbEx.getMessage());
            }

            return generatedQuestion;
        } catch (Exception e) {
            System.err.println("GEMINI API ERROR: " + e.getMessage());
            e.printStackTrace();

            return "Error: " + e.getMessage();
        }
    }

    public InterviewSummaryResponse generateInterviewSummary(String role, String transcript) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String prompt = "Analyze this mock interview for the role of " + role + ". " +
                "Transcript/Conversation: " + transcript + ". " +
                "Provide the response strictly in the following format with exact prefixes:\n" +
                "SCORE: [Only an integer number out of 100, e.g. 85]\n" +
                "FEEDBACK: [Short paragraph summarizing performance]\n" +
                "STRENGTHS: [Strength 1, Strength 2, Strength 3]\n" +
                "IMPROVEMENTS: [Improvement 1, Improvement 2, Improvement 3]";

        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", Collections.singletonList(part));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", Collections.singletonList(content));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        InterviewSummaryResponse summaryResponse = new InterviewSummaryResponse();
        summaryResponse.setRole(role);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            Map body = response.getBody();
            List candidates = (List) body.get("candidates");

            Map candidateMap = (Map) candidates.get(0);
            Map contentMap = (Map) candidateMap.get("content");
            List parts = (List) contentMap.get("parts");
            Map textMap = (Map) parts.get(0);
            String aiText = (String) textMap.get("text");

            int score = 75;
            String feedback = aiText;
            List<String> strengths = Arrays.asList("Good engagement", "Clear communication");
            List<String> improvements = Arrays.asList("Provide deeper details", "Focus on edge cases");

            String[] lines = aiText.split("\n");
            for (String line : lines) {
                String upperLine = line.toUpperCase().trim();
                if (upperLine.startsWith("SCORE:")) {
                    String scoreClean = line.replaceAll("[^0-9]", "").trim();
                    if (!scoreClean.isEmpty()) {
                        score = Integer.parseInt(scoreClean);
                    }
                } else if (upperLine.startsWith("FEEDBACK:")) {
                    feedback = line.replaceFirst("(?i)FEEDBACK:", "").trim();
                } else if (upperLine.startsWith("STRENGTHS:")) {
                    String strVal = line.replaceFirst("(?i)STRENGTHS:", "").trim();
                    if (!strVal.isEmpty()) {
                        strengths = Arrays.asList(strVal.split(",\\s*"));
                    }
                } else if (upperLine.startsWith("IMPROVEMENTS:")) {
                    String impVal = line.replaceFirst("(?i)IMPROVEMENTS:", "").trim();
                    if (!impVal.isEmpty()) {
                        improvements = Arrays.asList(impVal.split(",\\s*"));
                    }
                }
            }

            summaryResponse.setOverallScore(score);
            summaryResponse.setFeedback(feedback);
            summaryResponse.setStrengths(strengths);
            summaryResponse.setImprovements(improvements);

        } catch (Exception e) {
            System.err.println("SUMMARY API ERROR: " + e.getMessage());
            summaryResponse.setOverallScore(70);
            summaryResponse.setFeedback("Completed the interview session successfully. Review transcripts for details.");
            summaryResponse.setStrengths(Arrays.asList("Active participation", "Attempted questions"));
            summaryResponse.setImprovements(Arrays.asList("Enhance technical depth"));
        }

        return summaryResponse;
    }
}