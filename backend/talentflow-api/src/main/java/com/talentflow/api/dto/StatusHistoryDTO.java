package com.talentflow.api.dto;

import java.time.LocalDateTime;

import com.talentflow.api.entity.CandidateSubmission.SubmissionStatus;

public class StatusHistoryDTO {
    public record Response(
        Long id,
        Long submissionId,
        SubmissionStatus oldStatus,
        SubmissionStatus newStatus,
        Long changedBy,
        String transitionReason,
        String comments,
        LocalDateTime changedAt
    ) {}
}
