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
import com.talentflow.api.dto.L1EvaluationDTO;
import com.talentflow.api.service.L1EvaluationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/l1-evaluations")
public class L1EvaluationController {
    private final L1EvaluationService evaluationService;

    public L1EvaluationController(L1EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping
    public ResponseEntity<L1EvaluationDTO.Response> createEvaluation(
            @Valid @RequestBody L1EvaluationDTO.CreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(evaluationService.createEvaluation(request, principal));
    }

    @GetMapping("/{id}")
    public ResponseEntity<L1EvaluationDTO.Response> getEvaluationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                evaluationService.getEvaluationById(id));
    }

    @GetMapping
    public ResponseEntity<List<L1EvaluationDTO.Response>> getAllEvaluations() {
        return ResponseEntity.ok(
                evaluationService.getAllEvaluations());
    }

    @GetMapping("/submission/{submissionId}")
    public ResponseEntity<List<L1EvaluationDTO.Response>>
            getEvaluationsBySubmission(
                    @PathVariable Long submissionId) {

        return ResponseEntity.ok(
                evaluationService.getEvaluationsBySubmission(submissionId));
    }

    @GetMapping("/interviewer/{interviewerId}")
    public ResponseEntity<List<L1EvaluationDTO.Response>>
            getEvaluationsByInterviewer(
                    @PathVariable Long interviewerId) {

        return ResponseEntity.ok(
                evaluationService.getEvaluationsByInterviewer(interviewerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<L1EvaluationDTO.Response> updateEvaluation(
            @PathVariable Long id,
            @Valid @RequestBody L1EvaluationDTO.UpdateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(
                evaluationService.updateEvaluation(id, request, principal));
    }

    @PatchMapping("/{id}/schedule")
    public ResponseEntity<L1EvaluationDTO.Response> scheduleEvaluation(
            @PathVariable Long id,
            @Valid @RequestBody L1EvaluationDTO.ScheduleRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(
                evaluationService.scheduleEvaluation(id, request, principal));
    }

    @PatchMapping("/{id}/result")
    public ResponseEntity<L1EvaluationDTO.Response> recordResult(
            @PathVariable Long id,
            @Valid @RequestBody L1EvaluationDTO.ResultRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(
                evaluationService.recordResult(id, request, principal));
    }
}
