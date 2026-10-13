package com.talentflow.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "candidate_submission",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_candidate_requirement",
            columnNames = {"candidate_id", "requirement_id"}
        )
    },
    indexes = {
        @Index(name = "idx_submission_candidate_id", columnList = "candidate_id"),
        @Index(name = "idx_submission_requirement_id", columnList = "requirement_id"),
        @Index(name = "idx_submission_current_status", columnList = "current_status"),
        @Index(name = "idx_submission_assigned_to", columnList = "assigned_to"),
        @Index(name = "idx_submission_created_at", columnList = "created_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "candidate_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_submission_candidate")
    )
    private Candidate candidate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "requirement_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_submission_requirement")
    )
    private Requirement requirement;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "current_status",
        nullable = false,
        length = 30
    )
    @Builder.Default
    private SubmissionStatus currentStatus = SubmissionStatus.PROFILE_RECEIVED;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "submitted_by",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_submission_submitted_by")
    )
    private User submittedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "assigned_to",
        foreignKey = @ForeignKey(name = "fk_submission_assigned_to")
    )
    private User assignedTo;

    @Column(name = "submission_notes", columnDefinition = "TEXT")
    private String submissionNotes;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(
        name = "created_at",
        nullable = false,
        updatable = false,
        insertable = false
    )
    private LocalDateTime createdAt;

    @Column(
        name = "updated_at",
        nullable = false,
        insertable = false,
        updatable = false
    )
    private LocalDateTime updatedAt;

    public enum SubmissionStatus {
        PROFILE_RECEIVED,
        L1_PENDING,
        L1_SCHEDULED,
        L1_CLEARED,
        L1_REJECTED,
        CI_PENDING,
        CI_SCHEDULED,
        CI_ACCEPTED,
        CI_REJECTED,
        WITHDRAWN
    }
}