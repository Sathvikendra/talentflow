package com.talentflow.api.controller;

import com.talentflow.api.dto.RequirementDTO;
import com.talentflow.api.entity.Requirement;
import com.talentflow.api.service.RequirementService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requirements")
public class RequirementController {

    private final RequirementService requirementService;

    public RequirementController(
            RequirementService requirementService
    ) {
        this.requirementService = requirementService;
    }

    @PostMapping
    public ResponseEntity<Requirement> createRequirement(
            @Valid
            @RequestBody
            RequirementDTO.CreateRequirementRequest request
    ) {

        return ResponseEntity.status(
                HttpStatus.CREATED
        ).body(
                requirementService.createRequirement(
                        request
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<Requirement>> getAllRequirements() {

        return ResponseEntity.ok(
                requirementService.getAllRequirements()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Requirement> getRequirementById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                requirementService.getRequirementById(id)
        );
    }
}