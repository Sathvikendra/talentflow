package com.talentflow.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class RequirementSkillDTO {

    public record CreateRequirementSkillRequest(

            @NotNull
            Long requirementId,

            @NotNull
            Long skillId,

            @NotBlank
            String importance,

            BigDecimal minimumYears

    ) {}

    public record RequirementSkillResponse(

            Long requirementId,
            String requirementTitle,

            Long skillId,
            String skillName,

            String importance,
            BigDecimal minimumYears

    ) {}
}