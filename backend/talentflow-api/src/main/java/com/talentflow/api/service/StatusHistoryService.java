package com.talentflow.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentflow.api.dto.StatusHistoryDTO;
import com.talentflow.api.entity.CandidateSubmission;
import com.talentflow.api.entity.CandidateSubmission.SubmissionStatus;
import com.talentflow.api.entity.StatusHistory;
import com.talentflow.api.entity.User;
import com.talentflow.api.exception.CandidateSubmissionNotFoundException;
import com.talentflow.api.exception.StatusHistoryNotFoundException;
import com.talentflow.api.exception.UserNotFoundException;
import com.talentflow.api.repository.CandidateSubmissionRepository;
import com.talentflow.api.repository.StatusHistoryRepository;
import com.talentflow.api.repository.UserRepository;

@Service 
public class StatusHistoryService {
    private final StatusHistoryRepository statusHistoryRepository;
    private final CandidateSubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    public StatusHistoryService(
            StatusHistoryRepository statusHistoryRepository,
            CandidateSubmissionRepository submissionRepository,
            UserRepository userRepository) {
        this.statusHistoryRepository = statusHistoryRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
    }

    // Used internally by the submission workflow.
    @Transactional
    public StatusHistoryDTO.Response recordStatusChange(
            CandidateSubmission submission,
            SubmissionStatus oldStatus,
            SubmissionStatus newStatus,
            Long changedById,
            String reason,
            String comments) {

        if (newStatus == null) {
            throw new IllegalArgumentException(
                    "New status is required");
        }

        User changedBy = userRepository.findById(changedById)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with id: " + changedById));

        StatusHistory history = StatusHistory.builder()
                .submission(submission)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedBy(changedBy)
                .transitionReason(reason)
                .comments(comments)
                .build();

        return toResponse(statusHistoryRepository.save(history));
    }

    @Transactional(readOnly = true)
    public StatusHistoryDTO.Response getHistoryById(Long id) {
        StatusHistory history = statusHistoryRepository.findById(id)
                .orElseThrow(() -> new StatusHistoryNotFoundException(
                        "Status history not found with id: " + id));

        return toResponse(history);
    }

    @Transactional(readOnly = true)
    public List<StatusHistoryDTO.Response> getAllHistory() {
        return statusHistoryRepository
                .findAllByOrderByChangedAtAscIdAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StatusHistoryDTO.Response> getHistoryBySubmission(
            Long submissionId) {

        if (!submissionRepository.existsById(submissionId)) {
            throw new CandidateSubmissionNotFoundException(
                    "Candidate submission not found with id: "
                            + submissionId);
        }

        return statusHistoryRepository
                .findBySubmissionIdOrderByChangedAtAscIdAsc(submissionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StatusHistoryDTO.Response> getHistoryByUser(
            Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(
                    "User not found with id: " + userId);
        }

        return statusHistoryRepository
                .findByChangedByIdOrderByChangedAtAsc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private StatusHistoryDTO.Response toResponse(
            StatusHistory history) {

        return new StatusHistoryDTO.Response(
                history.getId(),
                history.getSubmission().getId(),
                history.getOldStatus(),
                history.getNewStatus(),
                history.getChangedBy().getId(),
                history.getTransitionReason(),
                history.getComments(),
                history.getChangedAt()
        );
    }
}
