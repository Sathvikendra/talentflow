package com.talentflow.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.talentflow.api.entity.L1Evaluation;
import com.talentflow.api.entity.L1Evaluation.Result;

public interface L1EvaluationRepository extends JpaRepository<L1Evaluation, Long> {
    List<L1Evaluation> findAllByScheduledAtIsNotNullOrderByScheduledAtDesc(
        Pageable pageable
    );

    List<L1Evaluation> findAllByConductedAtIsNotNullOrderByConductedAtDesc(
            Pageable pageable
    );
    
    List<L1Evaluation> findBySubmissionId(Long submissionId);

    List<L1Evaluation> findByInterviewerId(Long interviewerId);

    List<L1Evaluation> findByResult(Result result);

    long countByResult(Result result);

    long countByResultIsNull();

    long countByScheduledAtAfterAndResultIsNull(LocalDateTime dateTime);

    @Query("""
        SELECT COUNT(e)
        FROM L1Evaluation e
        WHERE e.scheduledAt >= :now
          AND e.result IS NULL
    """)
    long countUpcomingEvaluations(@Param("now") LocalDateTime now);
}
