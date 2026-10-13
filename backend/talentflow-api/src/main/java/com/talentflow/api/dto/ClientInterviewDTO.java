package com.talentflow.api.dto;

import java.time.LocalDateTime;

import com.talentflow.api.entity.ClientInterview.Result;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClientInterviewDTO {
    public record CreateRequest(
        @NotNull Long submissionId,

        @Size(max = 255)
        String clientInterviewerName,

        @Email
        @Size(max = 255)
        String clientInterviewerEmail,

        @Future
        LocalDateTime scheduledAt,

        @Min(1)
        Integer durationMinutes,

        @Size(max = 1000)
        String meetingLink
    ) {
    }

    public record ScheduleRequest(
        @NotNull
        @Future
        LocalDateTime scheduledAt,

        @Min(1)
        Integer durationMinutes,

        @Size(max = 1000)
        String meetingLink
    ) {
    }

    public record UpdateRequest(
        @Size(max = 255)
        String clientInterviewerName,

        @Email
        @Size(max = 255)
        String clientInterviewerEmail,

        @Future
        LocalDateTime scheduledAt,

        @Min(1)
        Integer durationMinutes,

        @Size(max = 1000)
        String meetingLink,

        String comments
    ) {
    }

    public record ResultRequest(
        @NotNull Result result,
        LocalDateTime conductedAt,
        String comments
    ) {
    }

    public record Response(
        Long id,
        Long submissionId,
        String clientInterviewerName,
        String clientInterviewerEmail,
        LocalDateTime scheduledAt,
        Integer durationMinutes,
        String meetingLink,
        LocalDateTime conductedAt,
        Result result,
        String comments,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
    }

    private ClientInterviewDTO() {
    }
}
