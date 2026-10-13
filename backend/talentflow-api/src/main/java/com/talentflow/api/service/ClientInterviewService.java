package com.talentflow.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentflow.api.config.UserPrincipal;
import com.talentflow.api.dto.CandidateSubmissionDTO;
import com.talentflow.api.dto.ClientInterviewDTO;
import com.talentflow.api.entity.CandidateSubmission;
import com.talentflow.api.entity.ClientInterview;
import com.talentflow.api.entity.ClientInterview.Result;
import com.talentflow.api.exception.AccessDeniedException;
import com.talentflow.api.exception.CandidateSubmissionNotFoundException;
import com.talentflow.api.exception.ClientInterviewNotFoundException;
import com.talentflow.api.repository.CandidateSubmissionRepository;
import com.talentflow.api.repository.ClientInterviewRepository;

@Service 
public class ClientInterviewService {
    private final ClientInterviewRepository clientInterviewRepository;
    private final CandidateSubmissionRepository candidateSubmissionRepository;
    private final CandidateSubmissionService candidateSubmissionService;

    public ClientInterviewService(
            ClientInterviewRepository clientInterviewRepository,
            CandidateSubmissionRepository candidateSubmissionRepository,
            CandidateSubmissionService candidateSubmissionService) {
        this.clientInterviewRepository = clientInterviewRepository;
        this.candidateSubmissionRepository = candidateSubmissionRepository;
        this.candidateSubmissionService = candidateSubmissionService;
    }

    @Transactional
    public ClientInterviewDTO.Response createInterview(
            ClientInterviewDTO.CreateRequest request,
            UserPrincipal principal) {

        requireReviewerOrAdmin(principal);

        CandidateSubmission submission = candidateSubmissionRepository
                .findById(request.submissionId())
                .orElseThrow(() ->
                        new CandidateSubmissionNotFoundException(
                                request.submissionId()));

        if (submission.getCurrentStatus()
                != CandidateSubmission.SubmissionStatus.CI_PENDING
                && submission.getCurrentStatus()
                != CandidateSubmission.SubmissionStatus.CI_SCHEDULED) {
            throw new IllegalStateException(
                    "A client interview can only be created when the "
                    + "submission is CI_PENDING or CI_SCHEDULED");
        }

        validateDuration(request.durationMinutes());

        ClientInterview interview = new ClientInterview();
        interview.setSubmission(submission);
        interview.setClientInterviewerName(request.clientInterviewerName());
        interview.setClientInterviewerEmail(request.clientInterviewerEmail());
        interview.setScheduledAt(request.scheduledAt());
        interview.setDurationMinutes(request.durationMinutes());
        interview.setMeetingLink(request.meetingLink());

        ClientInterview saved = clientInterviewRepository.save(interview);

        if (request.scheduledAt() != null
                && submission.getCurrentStatus()
                == CandidateSubmission.SubmissionStatus.CI_PENDING) {
            changeSubmissionStatus(
                    submission.getId(),
                    CandidateSubmission.SubmissionStatus.CI_SCHEDULED,
                    "Client interview scheduled",
                    "Client interview scheduled",
                    principal);
        }

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ClientInterviewDTO.Response getInterviewById(Long id) {
        return toResponse(findInterview(id));
    }

    @Transactional(readOnly = true)
    public List<ClientInterviewDTO.Response> getAllInterviews() {
        return clientInterviewRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClientInterviewDTO.Response> getInterviewsBySubmission(
            Long submissionId) {

        if (!candidateSubmissionRepository.existsById(submissionId)) {
            throw new CandidateSubmissionNotFoundException(submissionId);
        }

        return clientInterviewRepository.findBySubmissionId(submissionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ClientInterviewDTO.Response scheduleInterview(
            Long id,
            ClientInterviewDTO.ScheduleRequest request,
            UserPrincipal principal) {

        requireReviewerOrAdmin(principal);

        ClientInterview interview = findInterview(id);

        if (interview.getResult() != null) {
            throw new IllegalStateException(
                    "A completed client interview cannot be rescheduled");
        }

        CandidateSubmission submission = interview.getSubmission();

        if (submission.getCurrentStatus()
                != CandidateSubmission.SubmissionStatus.CI_PENDING
                && submission.getCurrentStatus()
                != CandidateSubmission.SubmissionStatus.CI_SCHEDULED) {
            throw new IllegalStateException(
                    "The submission must be CI_PENDING or CI_SCHEDULED "
                    + "to schedule a client interview");
        }

        validateDuration(request.durationMinutes());

        interview.setScheduledAt(request.scheduledAt());

        if (request.durationMinutes() != null) {
            interview.setDurationMinutes(request.durationMinutes());
        }

        if (request.meetingLink() != null) {
            interview.setMeetingLink(request.meetingLink());
        }

        ClientInterview saved = clientInterviewRepository.save(interview);

        if (submission.getCurrentStatus()
                == CandidateSubmission.SubmissionStatus.CI_PENDING) {
            changeSubmissionStatus(
                    submission.getId(),
                    CandidateSubmission.SubmissionStatus.CI_SCHEDULED,
                    "Client interview scheduled",
                    "Client interview scheduled",
                    principal);
        }

        return toResponse(saved);
    }

    @Transactional
    public ClientInterviewDTO.Response updateInterview(
            Long id,
            ClientInterviewDTO.UpdateRequest request,
            UserPrincipal principal) {

        requireReviewerOrAdmin(principal);

        ClientInterview interview = findInterview(id);

        if (interview.getResult() != null) {
            throw new IllegalStateException(
                    "A completed client interview cannot be updated");
        }

        validateDuration(request.durationMinutes());

        if (request.clientInterviewerName() != null) {
            interview.setClientInterviewerName(
                    request.clientInterviewerName());
        }

        if (request.clientInterviewerEmail() != null) {
            interview.setClientInterviewerEmail(
                    request.clientInterviewerEmail());
        }

        if (request.scheduledAt() != null) {
            interview.setScheduledAt(request.scheduledAt());
        }

        if (request.durationMinutes() != null) {
            interview.setDurationMinutes(request.durationMinutes());
        }

        if (request.meetingLink() != null) {
            interview.setMeetingLink(request.meetingLink());
        }

        if (request.comments() != null) {
            interview.setComments(request.comments());
        }

        ClientInterview saved = clientInterviewRepository.save(interview);

        // If the interview has now been scheduled, synchronize the workflow.
        if (saved.getScheduledAt() != null
                && saved.getSubmission().getCurrentStatus()
                == CandidateSubmission.SubmissionStatus.CI_PENDING) {
            changeSubmissionStatus(
                    saved.getSubmission().getId(),
                    CandidateSubmission.SubmissionStatus.CI_SCHEDULED,
                    "Client interview scheduled",
                    "Interview details updated with a schedule",
                    principal);
        }

        return toResponse(saved);
    }

    @Transactional
    public ClientInterviewDTO.Response recordResult(
            Long id,
            ClientInterviewDTO.ResultRequest request,
            UserPrincipal principal) {

        requireReviewerOrAdmin(principal);

        ClientInterview interview = findInterview(id);

        if (interview.getResult() != null) {
            throw new IllegalStateException(
                    "The result for this client interview has already been recorded");
        }

        if (request.conductedAt() == null) {
            throw new IllegalArgumentException(
                    "conductedAt is required when recording the interview result");
        }

        if (interview.getScheduledAt() == null) {
            throw new IllegalStateException(
                    "Schedule the client interview before recording its result");
        }

        if (interview.getSubmission().getCurrentStatus()
                != CandidateSubmission.SubmissionStatus.CI_SCHEDULED) {
            throw new IllegalStateException(
                    "The submission must be CI_SCHEDULED to record the result");
        }

        interview.setConductedAt(request.conductedAt());
        interview.setResult(request.result());
        interview.setComments(request.comments());

        ClientInterview saved = clientInterviewRepository.save(interview);

        CandidateSubmission.SubmissionStatus nextStatus =
                request.result() == Result.ACCEPTED
                ? CandidateSubmission.SubmissionStatus.CI_ACCEPTED
                : CandidateSubmission.SubmissionStatus.CI_REJECTED;

        changeSubmissionStatus(
                interview.getSubmission().getId(),
                nextStatus,
                "Client interview result recorded",
                request.comments(),
                principal);

        return toResponse(saved);
    }

    private ClientInterview findInterview(Long id) {
        return clientInterviewRepository.findById(id)
                .orElseThrow(() -> new ClientInterviewNotFoundException(id));
    }

    private void validateDuration(Integer durationMinutes) {
        if (durationMinutes != null && durationMinutes <= 0) {
            throw new IllegalArgumentException(
                    "durationMinutes must be greater than zero");
        }
    }

    private void requireReviewerOrAdmin(UserPrincipal principal) {
        if (principal == null
                || principal.getRole() == null
                || (!"ADMIN".equals(principal.getRole().name())
                && !"REVIEWER".equals(principal.getRole().name()))) {
            throw new AccessDeniedException(
                    "Only ADMIN or REVIEWER can manage client interviews");
        }
    }

    private void changeSubmissionStatus(
            Long submissionId,
            CandidateSubmission.SubmissionStatus status,
            String reason,
            String comments,
            UserPrincipal principal) {

        CandidateSubmissionDTO.StatusUpdateRequest statusRequest =
                new CandidateSubmissionDTO.StatusUpdateRequest(
                        status, reason, comments);

        candidateSubmissionService.changeSubmissionStatus(
                submissionId, statusRequest, principal);
    }

    private ClientInterviewDTO.Response toResponse(
            ClientInterview interview) {

        return new ClientInterviewDTO.Response(
                interview.getId(),
                interview.getSubmission().getId(),
                interview.getClientInterviewerName(),
                interview.getClientInterviewerEmail(),
                interview.getScheduledAt(),
                interview.getDurationMinutes(),
                interview.getMeetingLink(),
                interview.getConductedAt(),
                interview.getResult(),
                interview.getComments(),
                interview.getCreatedAt(),
                interview.getUpdatedAt());
    }
}
