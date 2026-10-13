package com.talentflow.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentflow.api.entity.L1Evaluation;
import com.talentflow.api.entity.L1Evaluation.Result;

public interface L1EvaluationRepository extends JpaRepository<L1Evaluation, Long> {
    List<L1Evaluation> findBySubmissionId(Long submissionId);

    List<L1Evaluation> findByInterviewerId(Long interviewerId);

    List<L1Evaluation> findByResult(Result result);
}
