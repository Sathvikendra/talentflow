package com.talentflow.api.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NotificationDTO {
    public record CreateRequest(
        @NotNull Long userId,

        @NotBlank
        @Size(max = 50)
        String type,

        @NotBlank
        @Size(max = 1000)
        String message,

        @Size(max = 50)
        String referenceType,

        Long referenceId
    ) {
    }

    public record Response(
        Long id,
        Long userId,
        String type,
        String message,
        String referenceType,
        Long referenceId,
        boolean isRead,
        LocalDateTime createdAt,
        LocalDateTime readAt
    ) {
    }

    private NotificationDTO() {
    }
}
