package com.talentflow.api.controller;

import com.talentflow.api.dto.RequirementSkillDTO;
import com.talentflow.api.entity.RequirementSkill;
import com.talentflow.api.service.RequirementSkillService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requirement-skills")
public class RequirementSkillController {

    private final RequirementSkillService requirementSkillService;

    public RequirementSkillController(
            RequirementSkillService requirementSkillService
    ) {
        this.requirementSkillService = requirementSkillService;
    }

    @PostMapping
    public ResponseEntity<RequirementSkill>
    createRequirementSkill(
            @Valid
            @RequestBody
            RequirementSkillDTO.CreateRequirementSkillRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        requirementSkillService
                                .createRequirementSkill(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<RequirementSkill>>
    getAllRequirementSkills() {

        return ResponseEntity.ok(
                requirementSkillService
                        .getAllRequirementSkills()
        );
    }
}