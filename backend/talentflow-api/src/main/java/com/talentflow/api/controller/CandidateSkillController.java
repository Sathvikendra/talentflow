package com.talentflow.api.controller;

import com.talentflow.api.dto.CandidateSkillDTO;
import com.talentflow.api.entity.CandidateSkill;
import com.talentflow.api.service.CandidateSkillService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidate-skills")
public class CandidateSkillController {

    private final CandidateSkillService candidateSkillService;

    public CandidateSkillController(
            CandidateSkillService candidateSkillService
    ) {
        this.candidateSkillService = candidateSkillService;
    }

    @PostMapping
    public ResponseEntity<CandidateSkill> createCandidateSkill(
            @Valid
            @RequestBody
            CandidateSkillDTO.CreateCandidateSkillRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        candidateSkillService.createCandidateSkill(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<CandidateSkill>> getAllCandidateSkills() {

        return ResponseEntity.ok(
                candidateSkillService.getAllCandidateSkills()
        );
    }
}