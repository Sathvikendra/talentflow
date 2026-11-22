package com.talentflow.api.dto.dashboard;

public record DashboardOverviewDTO(
        CandidateMetrics candidates,
        RequirementMetrics requirements,
        SubmissionMetrics submissions,
        ClientMetrics clients
) {

    public record CandidateMetrics(
            long total,
            long active,
            long inactive,
            long withdrawn
    ) {}

    public record RequirementMetrics(
            long total,
            long open,
            long filled,
            long closed,
            long totalPositions
    ) {}

    public record SubmissionMetrics(
            long total,
            long assigned,
            long unassigned
    ) {}

    public record ClientMetrics(
            long total,
            long active,
            long inactive
    ) {}
}
