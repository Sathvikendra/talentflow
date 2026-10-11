package com.talentflow.api.dto;

import jakarta.validation.constraints.NotBlank;

public class SkillDTO {

    public record CreateSkillRequest(

            @NotBlank
            String name,

            @NotBlank
            String normalizedName,

            String category

    ) {}

    public record UpdateSkillRequest(

            String name,
            String normalizedName,
            String category

    ) {}

    public record SkillResponse(

            Long id,
            String name,
            String normalizedName,
            String category

    ) {}
}