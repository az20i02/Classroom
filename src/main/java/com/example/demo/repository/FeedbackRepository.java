package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Add this import
import com.example.demo.tables.Feedback;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Find all feedback for a specific assessment
    List<Feedback> findByAssessment_Id(Long assessmentId);

    // Find feedback by a specific student for a specific assessment
    Optional<Feedback> findByAssessment_IdAndStudentUsername(Long assessmentId, String studentUsername);

    // Find all feedback given to a specific student
    List<Feedback> findByStudentUsername(String studentUsername);
}
