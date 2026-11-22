package com.talentflow.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentflow.api.dto.dashboard.DashboardOverviewDTO;
import com.talentflow.api.dto.dashboard.SubmissionStatusSummaryDTO;
import com.talentflow.api.dto.dashboard.L1SummaryDTO;
import com.talentflow.api.dto.dashboard.ClientInterviewSummaryDTO;
import com.talentflow.api.dto.dashboard.RecentActivityDTO;
import com.talentflow.api.dto.dashboard.RecentActivityResponseDTO;

import com.talentflow.api.entity.CandidateSubmission;
import com.talentflow.api.entity.CandidateSubmission.SubmissionStatus;
import com.talentflow.api.entity.L1Evaluation;
import com.talentflow.api.entity.ClientInterview;
import com.talentflow.api.entity.StatusHistory;

import com.talentflow.api.repository.CandidateRepository;
import com.talentflow.api.repository.RequirementRepository;
import com.talentflow.api.repository.CandidateSubmissionRepository;
import com.talentflow.api.repository.ClientRepository;
import com.talentflow.api.repository.L1EvaluationRepository;
import com.talentflow.api.repository.ClientInterviewRepository;
import com.talentflow.api.repository.StatusHistoryRepository;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final CandidateRepository candidateRepository;
    private final RequirementRepository requirementRepository;
    private final CandidateSubmissionRepository submissionRepository;
    private final ClientRepository clientRepository;
    private final L1EvaluationRepository l1EvaluationRepository;
    private final ClientInterviewRepository clientInterviewRepository;
    private final StatusHistoryRepository statusHistoryRepository;

    public DashboardService(
            CandidateRepository candidateRepository,
            RequirementRepository requirementRepository,
            CandidateSubmissionRepository submissionRepository,
            ClientRepository clientRepository,
            L1EvaluationRepository l1EvaluationRepository,
            ClientInterviewRepository clientInterviewRepository,
            StatusHistoryRepository statusHistoryRepository) {

        this.candidateRepository = candidateRepository;
        this.requirementRepository = requirementRepository;
        this.submissionRepository = submissionRepository;
        this.clientRepository = clientRepository;
        this.l1EvaluationRepository = l1EvaluationRepository;
        this.clientInterviewRepository = clientInterviewRepository;
        this.statusHistoryRepository = statusHistoryRepository;
    }

    // 1. Overall dashboard overview
    public DashboardOverviewDTO getOverview() {

        long totalCandidates = candidateRepository.count();

        var candidateMetrics =
                new DashboardOverviewDTO.CandidateMetrics(
                        totalCandidates,
                        candidateRepository.countByStatus("ACTIVE"),
                        candidateRepository.countByStatus("INACTIVE"),
                        candidateRepository.countByStatus("WITHDRAWN")
                );

        long totalRequirements = requirementRepository.count();

        Long positions = requirementRepository.getTotalPositions();

        var requirementMetrics =
                new DashboardOverviewDTO.RequirementMetrics(
                        totalRequirements,
                        requirementRepository.countByStatus("OPEN"),
                        requirementRepository.countByStatus("FILLED"),
                        requirementRepository.countByStatus("CLOSED"),
                        positions == null ? 0 : positions
                );

        var submissionMetrics =
                new DashboardOverviewDTO.SubmissionMetrics(
                        submissionRepository.count(),
                        submissionRepository.countByAssignedToIsNotNull(),
                        submissionRepository.countByAssignedToIsNull()
                );

        var clientMetrics =
                new DashboardOverviewDTO.ClientMetrics(
                        clientRepository.count(),
                        clientRepository.countByIsActive(true),
                        clientRepository.countByIsActive(false)
                );

        return new DashboardOverviewDTO(
                candidateMetrics,
                requirementMetrics,
                submissionMetrics,
                clientMetrics
        );
    }

    // 2. Submission status distribution
    public SubmissionStatusSummaryDTO getSubmissionStatusSummary() {

        Map<SubmissionStatus, Long> statusCounts =
                new EnumMap<>(SubmissionStatus.class);

        for (SubmissionStatus status : SubmissionStatus.values()) {
            statusCounts.put(status, 0L);
        }

        List<Object[]> results =
                submissionRepository.countSubmissionsByStatus();

        for (Object[] row : results) {
            SubmissionStatus status = (SubmissionStatus) row[0];
            Long count = (Long) row[1];

            statusCounts.put(status, count);
        }

        return new SubmissionStatusSummaryDTO(
                submissionRepository.count(),
                statusCounts
        );
    }

    // 3. L1 evaluation summary
    public L1SummaryDTO getL1Summary() {

        LocalDateTime now = LocalDateTime.now();

        long cleared =
                l1EvaluationRepository.countByResult(L1Evaluation.Result.CLEARED);

        long rejected =
                l1EvaluationRepository.countByResult(L1Evaluation.Result.REJECTED);

        long pending =
                l1EvaluationRepository.countByResultIsNull();

        long upcoming =
                l1EvaluationRepository.countUpcomingEvaluations(now);

        return new L1SummaryDTO(
                l1EvaluationRepository.count(),
                cleared,
                rejected,
                pending,
                upcoming
        );
    }

    // 4. Client interview summary
    public ClientInterviewSummaryDTO getClientInterviewSummary() {

        LocalDateTime now = LocalDateTime.now();

        long accepted =
                clientInterviewRepository.countByResult(
                        ClientInterview.Result.ACCEPTED);

        long rejected =
                clientInterviewRepository.countByResult(
                        ClientInterview.Result.REJECTED);

        long pending =
                clientInterviewRepository.countByResultIsNull();

        long upcoming =
                clientInterviewRepository.countUpcomingInterviews(now);

        return new ClientInterviewSummaryDTO(
                clientInterviewRepository.count(),
                accepted,
                rejected,
                pending,
                upcoming
        );
    }

    // 5. Recent activity feed
    
    
    public RecentActivityResponseDTO getRecentActivity() {

        Pageable pageable = PageRequest.of(0, 10);

        List<RecentActivityDTO> activities = new ArrayList<>();

        // 1. Submission status history
        statusHistoryRepository
                .findAllByOrderByChangedAtDescIdDesc(pageable)
                .stream()
                .map(this::mapStatusHistory)
                .forEach(activities::add);

        // 2. L1 scheduled activities
        l1EvaluationRepository
                .findAllByScheduledAtIsNotNullOrderByScheduledAtDesc(pageable)
                .forEach(evaluation -> {

                    Long submissionId = evaluation.getSubmission().getId();

                    activities.add(new RecentActivityDTO(
                            "L1_EVALUATION_SCHEDULED",
                            submissionId,
                            "L1 evaluation scheduled for submission " + submissionId,
                            evaluation.getInterviewer().getUsername(),
                            evaluation.getScheduledAt()
                    ));
                });

        // 3. L1 completed activities
        l1EvaluationRepository
                .findAllByConductedAtIsNotNullOrderByConductedAtDesc(pageable)
                .forEach(evaluation -> {

                    if (evaluation.getResult() != null) {

                        Long submissionId = evaluation.getSubmission().getId();

                        activities.add(new RecentActivityDTO(
                                "L1_EVALUATION_COMPLETED",
                                submissionId,
                                "L1 evaluation completed with result: "
                                        + evaluation.getResult(),
                                evaluation.getInterviewer().getUsername(),
                                evaluation.getConductedAt()
                        ));
                    }
                });

        // 4. Client interview scheduled activities
        clientInterviewRepository
                .findAllByScheduledAtIsNotNullOrderByScheduledAtDesc(pageable)
                .forEach(interview -> {

                    Long submissionId = interview.getSubmission().getId();

                    activities.add(new RecentActivityDTO(
                            "CLIENT_INTERVIEW_SCHEDULED",
                            submissionId,
                            "Client interview scheduled for submission " + submissionId,
                            interview.getClientInterviewerName(),
                            interview.getScheduledAt()
                    ));
                });

        // 5. Client interview completed activities
        clientInterviewRepository
                .findAllByConductedAtIsNotNullOrderByConductedAtDesc(pageable)
                .forEach(interview -> {

                    if (interview.getResult() != null) {

                        Long submissionId = interview.getSubmission().getId();

                        activities.add(new RecentActivityDTO(
                                "CLIENT_INTERVIEW_COMPLETED",
                                submissionId,
                                "Client interview completed with result: "
                                        + interview.getResult(),
                                interview.getClientInterviewerName(),
                                interview.getConductedAt()
                        ));
                    }
                });

        // 6. Merge and sort all activities
        activities.sort(
                Comparator.comparing(
                        RecentActivityDTO::timestamp,
                        Comparator.nullsLast(Comparator.reverseOrder())
                )
        );

        // 7. Return latest 10 activities
        List<RecentActivityDTO> latestActivities = activities.stream()
                .limit(10)
                .toList();

        return new RecentActivityResponseDTO(latestActivities);
    }


    private RecentActivityDTO mapStatusHistory(StatusHistory history) {

        CandidateSubmission submission = history.getSubmission();

        String description =
                "Submission moved from " +
                (history.getOldStatus() == null
                        ? "NONE"
                        : history.getOldStatus()) +
                " to " + history.getNewStatus();

        String performedBy =
                history.getChangedBy().getUsername();

        return new RecentActivityDTO(
                "SUBMISSION_STATUS_CHANGED",
                submission.getId(),
                description,
                performedBy,
                history.getChangedAt()
        );
    }
}
