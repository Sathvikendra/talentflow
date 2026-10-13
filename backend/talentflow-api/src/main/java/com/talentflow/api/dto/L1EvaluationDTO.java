package com.talentflow.api.dto;

import java.time.LocalDateTime;

import com.talentflow.api.entity.L1Evaluation.Result;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class L1EvaluationDTO {
    private L1EvaluationDTO() {
    }

    public record CreateRequest(
            @NotNull Long submissionId,
            @NotNull Long interviewerId,
            @Future LocalDateTime scheduledAt,
            @Min(1) Integer durationMinutes,
            @Size(max = 1000) String meetingLink
    ) {
    }

    public record ScheduleRequest(
            @NotNull @Future LocalDateTime scheduledAt,
            @Min(1) Integer durationMinutes,
            @Size(max = 1000) String meetingLink
    ) {
    }

    public record ResultRequest(
            @NotNull Result result,
            LocalDateTime conductedAt,
            String technicalComments,
            String overallComments,
            @Size(max = 30) String recommendation
    ) {
    }

    public record UpdateRequest(
            @Future LocalDateTime scheduledAt,
            @Min(1) Integer durationMinutes,
            @Size(max = 1000) String meetingLink,
            LocalDateTime conductedAt,
            String technicalComments,
            String overallComments,
            @Size(max = 30) String recommendation
    ) {
    }

    public record Response(
            Long id,
            Long submissionId,
            Long interviewerId,
            LocalDateTime scheduledAt,
            Integer durationMinutes,
            String meetingLink,
            LocalDateTime conductedAt,
            Result result,
            String technicalComments,
            String overallComments,
            String recommendation,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
    }
}
