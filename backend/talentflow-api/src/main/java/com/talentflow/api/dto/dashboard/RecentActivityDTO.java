package com.talentflow.api.dto.dashboard;

import java.time.LocalDateTime;

public record RecentActivityDTO(
        String activityType,
        Long submissionId,
        String description,
        String performedBy,
        LocalDateTime timestamp
) {}
