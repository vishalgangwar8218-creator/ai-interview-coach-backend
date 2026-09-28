package com.example.ai_interview_backend.repository;

import com.example.ai_interview_backend.entity.InterviewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewHistoryRepository extends JpaRepository<InterviewHistory,Long> {
    List<InterviewHistory> findByRole(String role);
}
