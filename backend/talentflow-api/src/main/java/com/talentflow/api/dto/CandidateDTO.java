package com.talentflow.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CandidateDTO {

    public record CreateCandidateRequest(

            @NotBlank
            String candidateReference,

            @NotBlank
            String firstName,

            String lastName,

            @Email
            String email,

            String phone,

            BigDecimal totalExperienceYears,

            String currentTitle,

            String currentCompany,

            String currentLocation,

            String preferredLocation,

            Integer noticePeriodDays,

            String profileSummary,

            @NotNull
            Long createdBy

    ) {}

    public record UpdateCandidateRequest(

            String firstName,
            String lastName,
            String email,
            String phone,
            BigDecimal totalExperienceYears,
            String currentTitle,
            String currentCompany,
            String currentLocation,
            String preferredLocation,
            Integer noticePeriodDays,
            String profileSummary,
            String status

    ) {}

    public record CandidateResponse(

            Long id,
            String candidateReference,
            String firstName,
            String lastName,
            String email,
            String phone,
            BigDecimal totalExperienceYears,
            String currentTitle,
            String currentCompany,
            String status

    ) {}
}