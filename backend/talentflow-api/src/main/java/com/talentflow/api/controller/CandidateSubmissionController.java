package com.talentflow.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentflow.api.config.UserPrincipal;
import com.talentflow.api.dto.CandidateSubmissionDTO;
import com.talentflow.api.entity.CandidateSubmission.SubmissionStatus;
import com.talentflow.api.service.CandidateSubmissionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/submissions")
public class CandidateSubmissionController {
    private final CandidateSubmissionService submissionService;

    public CandidateSubmissionController(
            CandidateSubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    public ResponseEntity<CandidateSubmissionDTO.Response> createSubmission(
            @Valid @RequestBody CandidateSubmissionDTO.CreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(submissionService.createSubmission(request, principal));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateSubmissionDTO.Response> getSubmissionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                submissionService.getSubmissionById(id));
    }

    @GetMapping
    public ResponseEntity<List<CandidateSubmissionDTO.Response>> getAllSubmissions() {
        return ResponseEntity.ok(
                submissionService.getAllSubmissions());
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<CandidateSubmissionDTO.Response>> getByCandidate(
            @PathVariable Long candidateId) {

        return ResponseEntity.ok(
                submissionService.getSubmissionsByCandidate(candidateId));
    }

    @GetMapping("/requirement/{requirementId}")
    public ResponseEntity<List<CandidateSubmissionDTO.Response>> getByRequirement(
            @PathVariable Long requirementId) {

        return ResponseEntity.ok(
                submissionService.getSubmissionsByRequirement(requirementId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CandidateSubmissionDTO.Response>> getByStatus(
            @PathVariable SubmissionStatus status) {

        return ResponseEntity.ok(
                submissionService.getSubmissionsByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateSubmissionDTO.Response> updateSubmission(
            @PathVariable Long id,
            @Valid @RequestBody CandidateSubmissionDTO.UpdateRequest request) {

        return ResponseEntity.ok(
                submissionService.updateSubmission(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CandidateSubmissionDTO.Response> changeSubmissionStatus(
            @PathVariable Long id,
            @Valid @RequestBody CandidateSubmissionDTO.StatusUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(
                submissionService.changeSubmissionStatus(
                        id, request, principal));
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<CandidateSubmissionDTO.Response> assignSubmission(
            @PathVariable Long id,
            @Valid @RequestBody CandidateSubmissionDTO.AssignRequest request) {

        return ResponseEntity.ok(
                submissionService.assignSubmission(id, request));
    }
}
