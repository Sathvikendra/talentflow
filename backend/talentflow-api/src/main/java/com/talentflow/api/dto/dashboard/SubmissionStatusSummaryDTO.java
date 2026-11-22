package com.talentflow.api.dto.dashboard;

import java.util.Map;

import com.talentflow.api.entity.CandidateSubmission.SubmissionStatus;

public record SubmissionStatusSummaryDTO(
        long total,
        Map<SubmissionStatus, Long> statusCounts
) {}