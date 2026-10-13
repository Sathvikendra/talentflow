package com.talentflow.api.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.talentflow.api.config.UserPrincipal;
import com.talentflow.api.dto.AIAssessmentDTO;
import com.talentflow.api.entity.AIAssessment.AssessmentStatus;
import com.talentflow.api.service.AIAssessmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/ai-assessments")
public class AIAssessmentController {
    private final AIAssessmentService aiAssessmentService;

    public AIAssessmentController(
            AIAssessmentService aiAssessmentService) {
        this.aiAssessmentService = aiAssessmentService;
    }

    @PostMapping
    public AIAssessmentDTO.Response createAssessment(
            @Valid @RequestBody AIAssessmentDTO.CreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return aiAssessmentService.createAssessment(request, principal);
    }

    @GetMapping("/{id}")
    public AIAssessmentDTO.Response getAssessmentById(
            @PathVariable Long id) {

        return aiAssessmentService.getAssessmentById(id);
    }

    @GetMapping
    public List<AIAssessmentDTO.Response> getAllAssessments(
            @RequestParam(required = false) AssessmentStatus status) {

        if (status != null) {
            return aiAssessmentService.getAssessmentsByStatus(status);
        }

        return aiAssessmentService.getAllAssessments();
    }

    @GetMapping("/submission/{submissionId}")
    public List<AIAssessmentDTO.Response> getAssessmentsBySubmission(
            @PathVariable Long submissionId) {

        return aiAssessmentService
                .getAssessmentsBySubmission(submissionId);
    }

    @PatchMapping("/{id}/complete")
    public AIAssessmentDTO.Response completeAssessment(
            @PathVariable Long id,
            @Valid @RequestBody AIAssessmentDTO.CompleteRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return aiAssessmentService.completeAssessment(
                id, request, principal);
    }

    @PatchMapping("/{id}/fail")
    public AIAssessmentDTO.Response failAssessment(
            @PathVariable Long id,
            @Valid @RequestBody AIAssessmentDTO.FailRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return aiAssessmentService.failAssessment(
                id, request, principal);
    }
}
