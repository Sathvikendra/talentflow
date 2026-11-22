package com.talentflow.api.dto.dashboard;

public record ClientInterviewSummaryDTO(
        long total,
        long accepted,
        long rejected,
        long pending,
        long upcoming
) {}
