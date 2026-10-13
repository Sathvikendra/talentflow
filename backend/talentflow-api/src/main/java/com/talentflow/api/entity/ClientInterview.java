package com.talentflow.api.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;

@Entity
@Table(
    name = "client_interview",
    indexes = {
        @Index(name = "idx_client_interview_submission", columnList = "submission_id"),
        @Index(name = "idx_client_interview_scheduled", columnList = "scheduled_at"),
        @Index(name = "idx_client_interview_result", columnList = "result")
    }
)
public class ClientInterview {
    public enum Result {
        ACCEPTED,
        REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submission_id", nullable = false)
    private CandidateSubmission submission;

    @Column(name = "client_interviewer_name", length = 255)
    private String clientInterviewerName;

    @Column(name = "client_interviewer_email", length = 255)
    private String clientInterviewerEmail;

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

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ClientInterview() {
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

    public String getClientInterviewerName() {
        return clientInterviewerName;
    }

    public void setClientInterviewerName(String clientInterviewerName) {
        this.clientInterviewerName = clientInterviewerName;
    }

    public String getClientInterviewerEmail() {
        return clientInterviewerEmail;
    }

    public void setClientInterviewerEmail(String clientInterviewerEmail) {
        this.clientInterviewerEmail = clientInterviewerEmail;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
