package com.talentflow.api.dto.dashboard;

import java.util.List;

public record RecentActivityResponseDTO(
        List<RecentActivityDTO> activities
) {}
