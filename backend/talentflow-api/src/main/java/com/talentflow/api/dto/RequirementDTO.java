package com.talentflow.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class RequirementDTO {

    public record CreateRequirementRequest(

            @NotBlank
            String rrNumber,

            @NotNull
            Long clientId,

            @NotBlank
            String title,

            @NotBlank
            String description,

            String employmentType,

            String location,

            @NotBlank
            String workMode,

            @NotBlank
            String onshoreOrOffshore,

            BigDecimal minExperienceYears,
            BigDecimal maxExperienceYears,

            @NotNull
            Integer positionsCount,

            String priority,

            @NotNull
            Long createdBy,

            LocalDate targetFillDate

    ) {}

    public record RequirementResponse(

            Long id,
            String rrNumber,
            String title,
            String status,
            String clientName,
            String createdBy

    ) {}

    public record UpdateRequirementRequest(

            String title,
            String description,
            String employmentType,
            String location,
            String workMode,
            String onshoreOrOffshore,
           BigDecimal minExperienceYears,
            BigDecimal maxExperienceYears,
            Integer positionsCount,
            String priority,
            String status,
            LocalDate targetFillDate

    ) {}
}