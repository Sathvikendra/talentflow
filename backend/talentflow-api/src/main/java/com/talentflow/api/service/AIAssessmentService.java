package com.talentflow.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentflow.api.config.UserPrincipal;
import com.talentflow.api.dto.AIAssessmentDTO;
import com.talentflow.api.entity.AIAssessment;
import com.talentflow.api.entity.AIAssessment.AssessmentStatus;
import com.talentflow.api.entity.CandidateSubmission;
import com.talentflow.api.entity.ResumeDocument;
import com.talentflow.api.exception.AIAssessmentNotFoundException;
import com.talentflow.api.exception.AccessDeniedException;
import com.talentflow.api.exception.CandidateSubmissionNotFoundException;
import com.talentflow.api.repository.AIAssessmentRepository;
import com.talentflow.api.repository.CandidateSubmissionRepository;
import com.talentflow.api.repository.ResumeDocumentRepository;

@Service 
public class AIAssessmentService {
    private final AIAssessmentRepository aiAssessmentRepository;
    private final CandidateSubmissionRepository candidateSubmissionRepository;
    private final ResumeDocumentRepository resumeDocumentRepository;

    public AIAssessmentService(
            AIAssessmentRepository aiAssessmentRepository,
            CandidateSubmissionRepository candidateSubmissionRepository,
            ResumeDocumentRepository resumeDocumentRepository) {

        this.aiAssessmentRepository = aiAssessmentRepository;
        this.candidateSubmissionRepository = candidateSubmissionRepository;
        this.resumeDocumentRepository = resumeDocumentRepository;
    }

    @Transactional
    public AIAssessmentDTO.Response createAssessment(
            AIAssessmentDTO.CreateRequest request,
            UserPrincipal principal) {

        requireReviewerOrAdmin(principal);

        CandidateSubmission submission =
                candidateSubmissionRepository.findById(request.submissionId())
                        .orElseThrow(() ->
                                new CandidateSubmissionNotFoundException(
                                        request.submissionId()));

        ResumeDocument resumeDocument = null;

        if (request.resumeDocumentId() != null) {
            resumeDocument = resumeDocumentRepository
                    .findById(request.resumeDocumentId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Resume document not found with ID: "
                                    + request.resumeDocumentId()));
        }

        AIAssessment assessment = new AIAssessment();
        assessment.setSubmission(submission);
        assessment.setResumeDocument(resumeDocument);
        assessment.setModelName(request.modelName());
        assessment.setPromptVersion(request.promptVersion());
        assessment.setAssessmentStatus(AssessmentStatus.PENDING);

        return toResponse(aiAssessmentRepository.save(assessment));
    }

    @Transactional(readOnly = true)
    public AIAssessmentDTO.Response getAssessmentById(Long id) {
        return toResponse(findAssessment(id));
    }

    @Transactional(readOnly = true)
    public List<AIAssessmentDTO.Response> getAllAssessments() {
        return aiAssessmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AIAssessmentDTO.Response> getAssessmentsBySubmission(
            Long submissionId) {

        if (!candidateSubmissionRepository.existsById(submissionId)) {
            throw new CandidateSubmissionNotFoundException(submissionId);
        }

        return aiAssessmentRepository
                .findBySubmissionIdOrderByCreatedAtDesc(submissionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AIAssessmentDTO.Response> getAssessmentsByStatus(
            AssessmentStatus status) {

        return aiAssessmentRepository.findByAssessmentStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AIAssessmentDTO.Response completeAssessment(
            Long id,
            AIAssessmentDTO.CompleteRequest request,
            UserPrincipal principal) {

        requireReviewerOrAdmin(principal);

        AIAssessment assessment = findAssessment(id);

        if (assessment.getAssessmentStatus() != AssessmentStatus.PENDING) {
            throw new IllegalStateException(
                    "Only a PENDING assessment can be completed");
        }

        assessment.setMatchedSkills(request.matchedSkills());
        assessment.setMissingSkills(request.missingSkills());
        assessment.setEvidence(request.evidence());
        assessment.setInterviewQuestions(request.interviewQuestions());
        assessment.setSummary(request.summary());
        assessment.setRawResponse(request.rawResponse());
        assessment.setErrorMessage(null);
        assessment.setAssessmentStatus(AssessmentStatus.COMPLETED);
        assessment.setCompletedAt(LocalDateTime.now());

        return toResponse(aiAssessmentRepository.save(assessment));
    }

    @Transactional
    public AIAssessmentDTO.Response failAssessment(
            Long id,
            AIAssessmentDTO.FailRequest request,
            UserPrincipal principal) {

        requireReviewerOrAdmin(principal);

        AIAssessment assessment = findAssessment(id);

        if (assessment.getAssessmentStatus() != AssessmentStatus.PENDING) {
            throw new IllegalStateException(
                    "Only a PENDING assessment can be marked FAILED");
        }

        assessment.setErrorMessage(request.errorMessage());
        assessment.setAssessmentStatus(AssessmentStatus.FAILED);
        assessment.setCompletedAt(LocalDateTime.now());

        return toResponse(aiAssessmentRepository.save(assessment));
    }

    private AIAssessment findAssessment(Long id) {
        return aiAssessmentRepository.findById(id)
                .orElseThrow(() -> new AIAssessmentNotFoundException(id));
    }

    private void requireReviewerOrAdmin(UserPrincipal principal) {
        if (principal == null
                || principal.getRole() == null
                || (!"ADMIN".equals(principal.getRole().name())
                && !"REVIEWER".equals(principal.getRole().name()))) {
            throw new AccessDeniedException(
                    "Only ADMIN or REVIEWER can manage AI assessments");
        }
    }

    private AIAssessmentDTO.Response toResponse(
            AIAssessment assessment) {

        return new AIAssessmentDTO.Response(
                assessment.getId(),
                assessment.getSubmission().getId(),
                assessment.getResumeDocument() != null
                        ? assessment.getResumeDocument().getId()
                        : null,
                assessment.getModelName(),
                assessment.getPromptVersion(),
                assessment.getAssessmentStatus(),
                assessment.getMatchedSkills(),
                assessment.getMissingSkills(),
                assessment.getEvidence(),
                assessment.getInterviewQuestions(),
                assessment.getSummary(),
                assessment.getRawResponse(),
                assessment.getErrorMessage(),
                assessment.getCreatedAt(),
                assessment.getCompletedAt());
    }
}
