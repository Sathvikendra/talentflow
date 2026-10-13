package com.talentflow.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talentflow.api.entity.AIAssessment;
import com.talentflow.api.entity.AIAssessment.AssessmentStatus;

@Repository 
public interface AIAssessmentRepository extends JpaRepository<AIAssessment, Long> {
    List<AIAssessment> findBySubmissionId(Long submissionId);

    List<AIAssessment> findByAssessmentStatus(
            AssessmentStatus assessmentStatus);

    List<AIAssessment> findBySubmissionIdOrderByCreatedAtDesc(
            Long submissionId);
}
