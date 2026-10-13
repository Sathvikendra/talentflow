package com.talentflow.api.dto;

import java.time.LocalDateTime;

import com.talentflow.api.entity.AIAssessment.AssessmentStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tools.jackson.databind.JsonNode;

public class AIAssessmentDTO {
    public record CreateRequest(
        @NotNull
        Long submissionId,

        Long resumeDocumentId,

        @Size(max = 255)
        String modelName,

        @Size(max = 100)
        String promptVersion
    ) {
    }

    public record CompleteRequest(
        JsonNode matchedSkills,
        JsonNode missingSkills,
        JsonNode evidence,
        JsonNode interviewQuestions,
        String summary,
        JsonNode rawResponse
    ) {
    }

    public record FailRequest(
        @NotNull
        @Size(max = 2000)
        String errorMessage
    ) {
    }

    public record Response(
        Long id,
        Long submissionId,
        Long resumeDocumentId,
        String modelName,
        String promptVersion,
        AssessmentStatus assessmentStatus,
        JsonNode matchedSkills,
        JsonNode missingSkills,
        JsonNode evidence,
        JsonNode interviewQuestions,
        String summary,
        JsonNode rawResponse,
        String errorMessage,
        LocalDateTime createdAt,
        LocalDateTime completedAt
    ) {
    }

    private AIAssessmentDTO() {
    }
}
