package com.example.ai_interview_backend.repository;

import com.example.ai_interview_backend.entity.InterviewQuestionCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestionCache, Long> {
    List<InterviewQuestionCache> findByRoleAndDifficulty(String role, String difficulty);
}
