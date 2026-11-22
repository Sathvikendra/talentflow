package com.talentflow.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.talentflow.api.dto.dashboard.DashboardOverviewDTO;
import com.talentflow.api.dto.dashboard.SubmissionStatusSummaryDTO;
import com.talentflow.api.dto.dashboard.L1SummaryDTO;
import com.talentflow.api.dto.dashboard.ClientInterviewSummaryDTO;
import com.talentflow.api.dto.dashboard.RecentActivityResponseDTO;

import com.talentflow.api.service.DashboardService;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // 1. Overall dashboard overview
    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewDTO> getOverview() {

        return ResponseEntity.ok(
                dashboardService.getOverview()
        );
    }

    // 2. Submission status distribution
    @GetMapping("/submissions/status")
    public ResponseEntity<SubmissionStatusSummaryDTO> getSubmissionStatusSummary() {

        return ResponseEntity.ok(
                dashboardService.getSubmissionStatusSummary()
        );
    }

    // 3. L1 evaluation summary
    @GetMapping("/l1-summary")
    public ResponseEntity<L1SummaryDTO> getL1Summary() {

        return ResponseEntity.ok(
                dashboardService.getL1Summary()
        );
    }

    // 4. Client interview summary
    @GetMapping("/interview-summary")
    public ResponseEntity<ClientInterviewSummaryDTO> getClientInterviewSummary() {

        return ResponseEntity.ok(
                dashboardService.getClientInterviewSummary()
        );
    }

    // 5. Recent activity
    @GetMapping("/recent-activity")
    public ResponseEntity<RecentActivityResponseDTO> getRecentActivity() {

        return ResponseEntity.ok(
                dashboardService.getRecentActivity()
        );
    }
}
