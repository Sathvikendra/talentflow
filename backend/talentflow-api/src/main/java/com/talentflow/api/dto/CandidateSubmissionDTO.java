package com.talentflow.api.dto;

import com.talentflow.api.entity.CandidateSubmission.SubmissionStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CandidateSubmissionDTO {

    public record CreateRequest(
        @NotNull
        Long candidateId,

        @NotNull
        Long requirementId,

        String submissionNotes
    ) {}

    public record UpdateRequest(
        SubmissionStatus currentStatus,
        Long assignedTo,
        String submissionNotes
    ) {}

    public record StatusUpdateRequest(
        @NotNull
        SubmissionStatus status,

        String reason,

        String comments
    ) {}

    public record AssignRequest(
        @NotNull
        Long assignedTo
    ) {}

    public record Response(
        Long id,
        Long candidateId,
        Long requirementId,
        SubmissionStatus currentStatus,
        Long submittedBy,
        Long assignedTo,
        String submissionNotes,
        LocalDateTime assignedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {}
}