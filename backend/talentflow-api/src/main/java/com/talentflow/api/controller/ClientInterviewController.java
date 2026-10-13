package com.talentflow.api.controller;

import java.util.List;

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
import com.talentflow.api.dto.ClientInterviewDTO;
import com.talentflow.api.service.ClientInterviewService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/api/v1/client-interviews")
public class ClientInterviewController {
    private final ClientInterviewService clientInterviewService;

    public ClientInterviewController(
            ClientInterviewService clientInterviewService) {
        this.clientInterviewService = clientInterviewService;
    }

    @PostMapping
    public ClientInterviewDTO.Response createInterview(
            @Valid @RequestBody ClientInterviewDTO.CreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return clientInterviewService.createInterview(request, principal);
    }

    @GetMapping("/{id}")
    public ClientInterviewDTO.Response getInterviewById(
            @PathVariable Long id) {

        return clientInterviewService.getInterviewById(id);
    }

    @GetMapping
    public List<ClientInterviewDTO.Response> getAllInterviews() {
        return clientInterviewService.getAllInterviews();
    }

    @GetMapping("/submission/{submissionId}")
    public List<ClientInterviewDTO.Response> getInterviewsBySubmission(
            @PathVariable Long submissionId) {

        return clientInterviewService.getInterviewsBySubmission(submissionId);
    }

    @PutMapping("/{id}")
    public ClientInterviewDTO.Response updateInterview(
            @PathVariable Long id,
            @Valid @RequestBody ClientInterviewDTO.UpdateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return clientInterviewService.updateInterview(id, request, principal);
    }

    @PatchMapping("/{id}/schedule")
    public ClientInterviewDTO.Response scheduleInterview(
            @PathVariable Long id,
            @Valid @RequestBody ClientInterviewDTO.ScheduleRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return clientInterviewService.scheduleInterview(
                id, request, principal);
    }

    @PatchMapping("/{id}/result")
    public ClientInterviewDTO.Response recordResult(
            @PathVariable Long id,
            @Valid @RequestBody ClientInterviewDTO.ResultRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return clientInterviewService.recordResult(id, request, principal);
    }
}
