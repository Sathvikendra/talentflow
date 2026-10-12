package com.talentflow.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CandidateSkillDTO {

    public record CreateCandidateSkillRequest(

            @NotNull
            Long candidateId,

            @NotNull
            Long skillId,

            String proficiency,

            BigDecimal yearsExperience,

            @NotBlank
            String source

    ) {}

    public record CandidateSkillResponse(

            Long candidateId,
            String candidateName,

            Long skillId,
            String skillName,

            String proficiency,
            BigDecimal yearsExperience,
            String source

    ) {}
}