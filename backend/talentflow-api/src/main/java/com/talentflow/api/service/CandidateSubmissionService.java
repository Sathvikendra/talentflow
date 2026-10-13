package com.talentflow.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentflow.api.config.UserPrincipal;
import com.talentflow.api.dto.CandidateSubmissionDTO;
import com.talentflow.api.entity.Candidate;
import com.talentflow.api.entity.CandidateSubmission;
import com.talentflow.api.entity.CandidateSubmission.SubmissionStatus;
import com.talentflow.api.entity.Requirement;
import com.talentflow.api.entity.User;
import com.talentflow.api.exception.CandidateNotFoundException;
import com.talentflow.api.exception.CandidateSubmissionNotFoundException;
import com.talentflow.api.exception.DuplicateResourceException;
import com.talentflow.api.exception.RequirementNotFoundException;
import com.talentflow.api.exception.UserNotFoundException;
import com.talentflow.api.repository.CandidateRepository;
import com.talentflow.api.repository.CandidateSubmissionRepository;
import com.talentflow.api.repository.RequirementRepository;
import com.talentflow.api.repository.UserRepository;

@Service 
public class CandidateSubmissionService {
    private final CandidateSubmissionRepository submissionRepository;
    private final CandidateRepository candidateRepository;
    private final RequirementRepository requirementRepository;
    private final UserRepository userRepository;
    private final StatusHistoryService statusHistoryService;

    public CandidateSubmissionService(
            CandidateSubmissionRepository submissionRepository,
            CandidateRepository candidateRepository,
            RequirementRepository requirementRepository,
            UserRepository userRepository,
            StatusHistoryService statusHistoryService) {
        this.submissionRepository = submissionRepository;
        this.candidateRepository = candidateRepository;
        this.requirementRepository = requirementRepository;
        this.userRepository = userRepository;
        this.statusHistoryService = statusHistoryService;
    }

    @Transactional
    public CandidateSubmissionDTO.Response createSubmission(
            CandidateSubmissionDTO.CreateRequest request,
            UserPrincipal principal) {

        if (submissionRepository.existsByCandidateIdAndRequirementId(
                request.candidateId(), request.requirementId())) {
            throw new DuplicateResourceException(
                    "Candidate is already submitted for this requirement");
        }

        Candidate candidate = candidateRepository.findById(request.candidateId())
                .orElseThrow(() -> new CandidateNotFoundException(
                        "Candidate not found with id: " + request.candidateId()));

        Requirement requirement = requirementRepository.findById(request.requirementId())
                .orElseThrow(() -> new RequirementNotFoundException(
                        "Requirement not found with id: " + request.requirementId()));

        User submittedBy = userRepository.findById(principal.getId())
                .orElseThrow(() -> new UserNotFoundException(
                        "Submitting user not found with id: " + principal.getId()));

        CandidateSubmission submission = CandidateSubmission.builder()
                .candidate(candidate)
                .requirement(requirement)
                .submittedBy(submittedBy)
                .currentStatus(SubmissionStatus.PROFILE_RECEIVED)
                .submissionNotes(request.submissionNotes())
                .build();

        CandidateSubmission savedSubmission = submissionRepository.save(submission);

        statusHistoryService.recordStatusChange(
            savedSubmission,
            null,
            savedSubmission.getCurrentStatus(),
            submittedBy.getId(),
            "Submission created",
            request.submissionNotes()
        );

        return toResponse(savedSubmission);
    }

    @Transactional(readOnly = true)
    public CandidateSubmissionDTO.Response getSubmissionById(Long id) {
        return toResponse(findSubmission(id));
    }

    @Transactional(readOnly = true)
    public List<CandidateSubmissionDTO.Response> getAllSubmissions() {
        return submissionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CandidateSubmissionDTO.Response> getSubmissionsByCandidate(
            Long candidateId) {

        if (!candidateRepository.existsById(candidateId)) {
            throw new CandidateNotFoundException(
                    "Candidate not found with id: " + candidateId);
        }

        return submissionRepository.findByCandidateId(candidateId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CandidateSubmissionDTO.Response> getSubmissionsByRequirement(
            Long requirementId) {

        if (!requirementRepository.existsById(requirementId)) {
            throw new RequirementNotFoundException(
                    "Requirement not found with id: " + requirementId);
        }

        return submissionRepository.findByRequirementId(requirementId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CandidateSubmissionDTO.Response> getSubmissionsByStatus(
            SubmissionStatus status) {

        return submissionRepository.findByCurrentStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CandidateSubmissionDTO.Response updateSubmission(
            Long id,
            CandidateSubmissionDTO.UpdateRequest request) {

        CandidateSubmission submission = findSubmission(id);

        // Status changes must use the dedicated audited workflow operation.
        if (request.currentStatus() != null
                && request.currentStatus() != submission.getCurrentStatus()) {
            throw new IllegalArgumentException(
                    "Use the status-change endpoint to change submission status");
        }

        if (request.submissionNotes() != null) {
            submission.setSubmissionNotes(request.submissionNotes());
        }

        if (request.assignedTo() != null) {
            assignToUser(submission, request.assignedTo());
        }

        return toResponse(submissionRepository.save(submission));
    }

    @Transactional
    public CandidateSubmissionDTO.Response changeSubmissionStatus(
            Long id,
            CandidateSubmissionDTO.StatusUpdateRequest request,
            UserPrincipal principal) {

        CandidateSubmission submission = findSubmission(id);

        SubmissionStatus oldStatus = submission.getCurrentStatus();
        SubmissionStatus newStatus = request.status();

        validateStatusTransition(oldStatus, newStatus);

        User changedBy = userRepository.findById(principal.getId())
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with id: " + principal.getId()));

        submission.setCurrentStatus(newStatus);

        CandidateSubmission savedSubmission =
                submissionRepository.save(submission);

        statusHistoryService.recordStatusChange(
                savedSubmission,
                oldStatus,
                newStatus,
                changedBy.getId(),
                request.reason(),
                request.comments()
        );

        return toResponse(savedSubmission);
    }

    @Transactional
    public CandidateSubmissionDTO.Response assignSubmission(
            Long id,
            CandidateSubmissionDTO.AssignRequest request) {

        CandidateSubmission submission = findSubmission(id);
        assignToUser(submission, request.assignedTo());

        return toResponse(submissionRepository.save(submission));
    }

    
    private void validateStatusTransition(
            SubmissionStatus currentStatus,
            SubmissionStatus newStatus) {

        if (currentStatus == null || newStatus == null) {
            throw new IllegalArgumentException(
                    "Current status and new status are required");
        }

        boolean allowed = switch (currentStatus) {
            case PROFILE_RECEIVED ->
                    newStatus == SubmissionStatus.L1_PENDING
                    || newStatus == SubmissionStatus.WITHDRAWN;

            case L1_PENDING ->
                    newStatus == SubmissionStatus.L1_SCHEDULED
                    || newStatus == SubmissionStatus.WITHDRAWN;

            case L1_SCHEDULED ->
                    newStatus == SubmissionStatus.L1_CLEARED
                    || newStatus == SubmissionStatus.L1_REJECTED
                    || newStatus == SubmissionStatus.WITHDRAWN;

            case L1_CLEARED ->
                    newStatus == SubmissionStatus.CI_PENDING
                    || newStatus == SubmissionStatus.WITHDRAWN;

            case CI_PENDING ->
                    newStatus == SubmissionStatus.CI_SCHEDULED
                    || newStatus == SubmissionStatus.WITHDRAWN;

            case CI_SCHEDULED ->
                    newStatus == SubmissionStatus.CI_ACCEPTED
                    || newStatus == SubmissionStatus.CI_REJECTED
                    || newStatus == SubmissionStatus.WITHDRAWN;

            case L1_REJECTED, CI_REJECTED, CI_ACCEPTED, WITHDRAWN ->
                    false;
        };

        if (!allowed) {
            throw new IllegalArgumentException(
                    "Invalid status transition from "
                    + currentStatus + " to " + newStatus);
        }
    }

    private void assignToUser(
            CandidateSubmission submission,
            Long assignedToId) {

        User assignedTo = userRepository.findById(assignedToId)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with id: " + assignedToId));

        submission.setAssignedTo(assignedTo);
        submission.setAssignedAt(LocalDateTime.now());
    }

    private CandidateSubmission findSubmission(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new CandidateSubmissionNotFoundException(
                        "Candidate submission not found with id: " + id));
    }

    private CandidateSubmissionDTO.Response toResponse(
            CandidateSubmission submission) {

        return new CandidateSubmissionDTO.Response(
                submission.getId(),
                submission.getCandidate().getId(),
                submission.getRequirement().getId(),
                submission.getCurrentStatus(),
                submission.getSubmittedBy().getId(),
                submission.getAssignedTo() == null
                        ? null
                        : submission.getAssignedTo().getId(),
                submission.getSubmissionNotes(),
                submission.getAssignedAt(),
                submission.getCreatedAt(),
                submission.getUpdatedAt()
        );
    }
}
