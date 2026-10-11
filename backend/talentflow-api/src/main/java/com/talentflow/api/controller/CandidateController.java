package com.talentflow.api.controller;

import com.talentflow.api.dto.CandidateDTO;
import com.talentflow.api.entity.Candidate;
import com.talentflow.api.service.CandidateService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(
            CandidateService candidateService
    ) {
        this.candidateService = candidateService;
    }

    @PostMapping
    public ResponseEntity<Candidate> createCandidate(
            @Valid
            @RequestBody
            CandidateDTO.CreateCandidateRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        candidateService.createCandidate(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {

        return ResponseEntity.ok(
                candidateService.getAllCandidates()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                candidateService.getCandidateById(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(
            @PathVariable Long id
    ) {

        candidateService.deleteCandidate(id);

        return ResponseEntity.noContent().build();
    }
}