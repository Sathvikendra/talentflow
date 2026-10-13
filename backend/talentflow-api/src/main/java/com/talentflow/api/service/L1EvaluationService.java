package com.talentflow.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentflow.api.config.UserPrincipal;
import com.talentflow.api.dto.CandidateSubmissionDTO;
import com.talentflow.api.dto.L1EvaluationDTO;
import com.talentflow.api.entity.CandidateSubmission;
import com.talentflow.api.entity.CandidateSubmission.SubmissionStatus;
import com.talentflow.api.entity.L1Evaluation;
import com.talentflow.api.entity.L1Evaluation.Result;
import com.talentflow.api.entity.User;
import com.talentflow.api.exception.CandidateSubmissionNotFoundException;
import com.talentflow.api.exception.L1EvaluationNotFoundException;
import com.talentflow.api.exception.UserNotFoundException;
import com.talentflow.api.repository.CandidateSubmissionRepository;
import com.talentflow.api.repository.L1EvaluationRepository;
import com.talentflow.api.repository.UserRepository;

@Service 
public class L1EvaluationService {
    private final L1EvaluationRepository evaluationRepository;
    private final CandidateSubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final CandidateSubmissionService submissionService;

    public L1EvaluationService(
            L1EvaluationRepository evaluationRepository,
            CandidateSubmissionRepository submissionRepository,
            UserRepository userRepository,
            CandidateSubmissionService submissionService) {
        this.evaluationRepository = evaluationRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
        this.submissionService = submissionService;
    }

    @Transactional
    public L1EvaluationDTO.Response createEvaluation(
            L1EvaluationDTO.CreateRequest request,
            UserPrincipal principal) {

        requireReviewerOrAdmin(principal);

        CandidateSubmission submission = submissionRepository
                .findById(request.submissionId())
                .orElseThrow(() ->
                        new CandidateSubmissionNotFoundException(request.submissionId()));

        User interviewer = userRepository.findById(request.interviewerId())
                .orElseThrow(() ->
                        new UserNotFoundException(request.interviewerId()));

        if (submission.getCurrentStatus() != SubmissionStatus.L1_PENDING
                && submission.getCurrentStatus()
                        != SubmissionStatus.L1_SCHEDULED) {
            throw new IllegalArgumentException(
                    "An L1 evaluation can only be created when the submission "
                    + "is L1_PENDING or L1_SCHEDULED");
        }

        L1Evaluation evaluation = new L1Evaluation();
        evaluation.setSubmission(submission);
        evaluation.setInterviewer(interviewer);
        evaluation.setScheduledAt(request.scheduledAt());
        evaluation.setDurationMinutes(request.durationMinutes());
        evaluation.setMeetingLink(request.meetingLink());

        L1Evaluation saved = evaluationRepository.save(evaluation);

        if (request.scheduledAt() != null
                && submission.getCurrentStatus()
                        == SubmissionStatus.L1_PENDING) {
            changeSubmissionStatus(
                    submission.getId(),
                    SubmissionStatus.L1_SCHEDULED,
                    "L1 evaluation scheduled",
                    "L1 evaluation ID: " + saved.getId(),
                    principal);
        }

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public L1EvaluationDTO.Response getEvaluationById(Long id) {
        return toResponse(findEvaluation(id));
    }

    @Transactional(readOnly = true)
    public List<L1EvaluationDTO.Response> getAllEvaluations() {
        return evaluationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<L1EvaluationDTO.Response> getEvaluationsBySubmission(
            Long submissionId) {

        if (!submissionRepository.existsById(submissionId)) {
            throw new CandidateSubmissionNotFoundException(submissionId);
        }

        return evaluationRepository.findBySubmissionId(submissionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<L1EvaluationDTO.Response> getEvaluationsByInterviewer(
            Long interviewerId) {

        if (!userRepository.existsById(interviewerId)) {
            throw new UserNotFoundException(interviewerId);
        }

        return evaluationRepository.findByInterviewerId(interviewerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public L1EvaluationDTO.Response scheduleEvaluation(
            Long id,
            L1EvaluationDTO.ScheduleRequest request,
            UserPrincipal principal) {

        requireReviewerOrAdmin(principal);

        L1Evaluation evaluation = findEvaluation(id);
        CandidateSubmission submission = evaluation.getSubmission();

        if (evaluation.getResult() != null) {
            throw new IllegalArgumentException(
                    "A completed L1 evaluation cannot be rescheduled");
        }

        if (submission.getCurrentStatus() != SubmissionStatus.L1_PENDING
                && submission.getCurrentStatus()
                        != SubmissionStatus.L1_SCHEDULED) {
            throw new IllegalArgumentException(
                    "The submission is not in an L1 scheduling stage");
        }

        evaluation.setScheduledAt(request.scheduledAt());
        evaluation.setDurationMinutes(request.durationMinutes());
        evaluation.setMeetingLink(request.meetingLink());

        L1Evaluation saved = evaluationRepository.save(evaluation);

        if (submission.getCurrentStatus() == SubmissionStatus.L1_PENDING) {
            changeSubmissionStatus(
                    submission.getId(),
                    SubmissionStatus.L1_SCHEDULED,
                    "L1 evaluation scheduled",
                    "L1 evaluation ID: " + saved.getId(),
                    principal);
        }

        return toResponse(saved);
    }

    @Transactional
    public L1EvaluationDTO.Response updateEvaluation(
            Long id,
            L1EvaluationDTO.UpdateRequest request,
            UserPrincipal principal) {

        requireReviewerOrAdmin(principal);

        L1Evaluation evaluation = findEvaluation(id);

        if (evaluation.getResult() != null) {
            throw new IllegalArgumentException(
                    "A completed L1 evaluation cannot be edited");
        }

        if (request.scheduledAt() != null) {
            evaluation.setScheduledAt(request.scheduledAt());
        }

        if (request.durationMinutes() != null) {
            evaluation.setDurationMinutes(request.durationMinutes());
        }

        if (request.meetingLink() != null) {
            evaluation.setMeetingLink(request.meetingLink());
        }

        if (request.conductedAt() != null) {
            evaluation.setConductedAt(request.conductedAt());
        }

        if (request.technicalComments() != null) {
            evaluation.setTechnicalComments(request.technicalComments());
        }

        if (request.overallComments() != null) {
            evaluation.setOverallComments(request.overallComments());
        }

        if (request.recommendation() != null) {
            evaluation.setRecommendation(request.recommendation());
        }

        return toResponse(evaluationRepository.save(evaluation));
    }

    @Transactional
    public L1EvaluationDTO.Response recordResult(
            Long id,
            L1EvaluationDTO.ResultRequest request,
            UserPrincipal principal) {

        requireReviewerOrAdmin(principal);

        L1Evaluation evaluation = findEvaluation(id);
        CandidateSubmission submission = evaluation.getSubmission();

        if (evaluation.getResult() != null) {
            throw new IllegalArgumentException(
                    "An L1 result has already been recorded");
        }

        if (submission.getCurrentStatus()
                != SubmissionStatus.L1_SCHEDULED) {
            throw new IllegalArgumentException(
                    "The submission must be L1_SCHEDULED before recording "
                    + "the L1 result");
        }

        if (request.conductedAt() == null) {
            throw new IllegalArgumentException(
                    "conductedAt is required when recording an L1 result");
        }

        evaluation.setConductedAt(request.conductedAt());
        evaluation.setResult(request.result());
        evaluation.setTechnicalComments(request.technicalComments());
        evaluation.setOverallComments(request.overallComments());
        evaluation.setRecommendation(request.recommendation());

        L1Evaluation saved = evaluationRepository.save(evaluation);

        SubmissionStatus newStatus = request.result() == Result.CLEARED
                ? SubmissionStatus.L1_CLEARED
                : SubmissionStatus.L1_REJECTED;

        changeSubmissionStatus(
                submission.getId(),
                newStatus,
                "L1 evaluation result recorded",
                "L1 evaluation ID: " + saved.getId(),
                principal);

        return toResponse(saved);
    }

    private void changeSubmissionStatus(
            Long submissionId,
            SubmissionStatus status,
            String reason,
            String comments,
            UserPrincipal principal) {

        CandidateSubmissionDTO.StatusUpdateRequest statusRequest =
                new CandidateSubmissionDTO.StatusUpdateRequest(
                        status, reason, comments);

        submissionService.changeSubmissionStatus(
                submissionId, statusRequest, principal);
    }

    private L1Evaluation findEvaluation(Long id) {
        return evaluationRepository.findById(id)
                .orElseThrow(() -> new L1EvaluationNotFoundException(id));
    }

    private void requireReviewerOrAdmin(UserPrincipal principal) {
        if (principal == null || principal.getRole() == null) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Authentication is required");
        }

        String role = principal.getRole().name();

        if (!role.equals("ADMIN") && !role.equals("REVIEWER")) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Only ADMIN or REVIEWER can manage L1 evaluations");
        }
    }

    private L1EvaluationDTO.Response toResponse(L1Evaluation evaluation) {
        return new L1EvaluationDTO.Response(
                evaluation.getId(),
                evaluation.getSubmission().getId(),
                evaluation.getInterviewer().getId(),
                evaluation.getScheduledAt(),
                evaluation.getDurationMinutes(),
                evaluation.getMeetingLink(),
                evaluation.getConductedAt(),
                evaluation.getResult(),
                evaluation.getTechnicalComments(),
                evaluation.getOverallComments(),
                evaluation.getRecommendation(),
                evaluation.getCreatedAt(),
                evaluation.getUpdatedAt());
    }
}
