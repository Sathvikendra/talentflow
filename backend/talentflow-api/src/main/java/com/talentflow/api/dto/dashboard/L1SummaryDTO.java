package com.talentflow.api.dto.dashboard;

public record L1SummaryDTO(
        long total,
        long cleared,
        long rejected,
        long pending,
        long upcoming
) {}
