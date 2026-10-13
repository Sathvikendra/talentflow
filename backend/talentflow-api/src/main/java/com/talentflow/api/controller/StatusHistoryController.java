package com.talentflow.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentflow.api.dto.StatusHistoryDTO;
import com.talentflow.api.service.StatusHistoryService;

@RestController
@RequestMapping("/api/v1/status-history")
public class StatusHistoryController {
    private final StatusHistoryService statusHistoryService;

    public StatusHistoryController(
            StatusHistoryService statusHistoryService) {
        this.statusHistoryService = statusHistoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusHistoryDTO.Response> getHistoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                statusHistoryService.getHistoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<StatusHistoryDTO.Response>> getAllHistory() {

        return ResponseEntity.ok(
                statusHistoryService.getAllHistory());
    }

    @GetMapping("/submission/{submissionId}")
    public ResponseEntity<List<StatusHistoryDTO.Response>>
    getHistoryBySubmission(@PathVariable Long submissionId) {

        return ResponseEntity.ok(
                statusHistoryService.getHistoryBySubmission(submissionId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StatusHistoryDTO.Response>>
    getHistoryByUser(@PathVariable Long userId) {

        return ResponseEntity.ok(
                statusHistoryService.getHistoryByUser(userId));
    }
}
