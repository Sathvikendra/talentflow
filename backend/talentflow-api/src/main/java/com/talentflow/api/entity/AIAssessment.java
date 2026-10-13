package com.talentflow.api.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import tools.jackson.databind.JsonNode;

@Entity
@Table(
    name = "ai_assessment",
    indexes = {
        @Index(name = "idx_ai_assessment_submission", columnList = "submission_id"),
        @Index(name = "idx_ai_assessment_status", columnList = "assessment_status"),
        @Index(name = "idx_ai_assessment_created", columnList = "created_at")
    }
)
public class AIAssessment {
    public enum AssessmentStatus {
        PENDING,
        COMPLETED,
        FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submission_id", nullable = false)
    private CandidateSubmission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_document_id")
    private ResumeDocument resumeDocument;

    @Column(name = "model_name", length = 255)
    private String modelName;

    @Column(name = "prompt_version", length = 100)
    private String promptVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "assessment_status", nullable = false, length = 30)
    private AssessmentStatus assessmentStatus = AssessmentStatus.PENDING;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "matched_skills", columnDefinition = "json")
    private JsonNode matchedSkills;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "missing_skills", columnDefinition = "json")
    private JsonNode missingSkills;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "evidence", columnDefinition = "json")
    private JsonNode evidence;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "interview_questions", columnDefinition = "json")
    private JsonNode interviewQuestions;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "raw_response", columnDefinition = "json")
    private JsonNode rawResponse;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public AIAssessment() {
    }

    public Long getId() {
        return id;
    }

    public CandidateSubmission getSubmission() {
        return submission;
    }

    public void setSubmission(CandidateSubmission submission) {
        this.submission = submission;
    }

    public ResumeDocument getResumeDocument() {
        return resumeDocument;
    }

    public void setResumeDocument(ResumeDocument resumeDocument) {
        this.resumeDocument = resumeDocument;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getPromptVersion() {
        return promptVersion;
    }

    public void setPromptVersion(String promptVersion) {
        this.promptVersion = promptVersion;
    }

    public AssessmentStatus getAssessmentStatus() {
        return assessmentStatus;
    }

    public void setAssessmentStatus(AssessmentStatus assessmentStatus) {
        this.assessmentStatus = assessmentStatus;
    }

    public JsonNode getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(JsonNode matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public JsonNode getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(JsonNode missingSkills) {
        this.missingSkills = missingSkills;
    }

    public JsonNode getEvidence() {
        return evidence;
    }

    public void setEvidence(JsonNode evidence) {
        this.evidence = evidence;
    }

    public JsonNode getInterviewQuestions() {
        return interviewQuestions;
    }

    public void setInterviewQuestions(JsonNode interviewQuestions) {
        this.interviewQuestions = interviewQuestions;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public JsonNode getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(JsonNode rawResponse) {
        this.rawResponse = rawResponse;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
