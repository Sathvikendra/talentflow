package com.talentflow.api.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;

@Entity
@Table(
    name = "l1_evaluation",
    indexes = {
        @Index(name = "idx_l1_submission_id", columnList = "submission_id"),
        @Index(name = "idx_l1_interviewer_id", columnList = "interviewer_id"),
        @Index(name = "idx_l1_scheduled_at", columnList = "scheduled_at"),
        @Index(name = "idx_l1_result", columnList = "result")
    },
    check = {
        @CheckConstraint(
            name = "chk_l1_duration",
            constraint = "duration_minutes IS NULL OR duration_minutes > 0"
        ),
        @CheckConstraint(
            name = "chk_l1_result",
            constraint = "result IS NULL OR result IN ('CLEARED', 'REJECTED')"
        )
    }
)
public class L1Evaluation {
    public enum Result {
        CLEARED,
        REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submission_id", nullable = false)
    private CandidateSubmission submission;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "interviewer_id", nullable = false)
    private User interviewer;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "meeting_link", length = 1000)
    private String meetingLink;

    @Column(name = "conducted_at")
    private LocalDateTime conductedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", length = 30)
    private Result result;

    @Column(name = "technical_comments", columnDefinition = "TEXT")
    private String technicalComments;

    @Column(name = "overall_comments", columnDefinition = "TEXT")
    private String overallComments;

    @Column(name = "recommendation", length = 30)
    private String recommendation;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public L1Evaluation() {
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

    public User getInterviewer() {
        return interviewer;
    }

    public void setInterviewer(User interviewer) {
        this.interviewer = interviewer;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public LocalDateTime getConductedAt() {
        return conductedAt;
    }

    public void setConductedAt(LocalDateTime conductedAt) {
        this.conductedAt = conductedAt;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getTechnicalComments() {
        return technicalComments;
    }

    public void setTechnicalComments(String technicalComments) {
        this.technicalComments = technicalComments;
    }

    public String getOverallComments() {
        return overallComments;
    }

    public void setOverallComments(String overallComments) {
        this.overallComments = overallComments;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
