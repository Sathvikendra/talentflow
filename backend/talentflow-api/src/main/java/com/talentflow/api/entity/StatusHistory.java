package com.talentflow.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "status_history",
    indexes = {
        @Index(
            name = "idx_status_history_submission_changed_at",
            columnList = "submission_id, changed_at"
        ),
        @Index(
            name = "idx_status_history_changed_by",
            columnList = "changed_by"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "submission_id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_status_history_submission"
        )
    )
    private CandidateSubmission submission;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", length = 30)
    private CandidateSubmission.SubmissionStatus oldStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 30)
    private CandidateSubmission.SubmissionStatus newStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "changed_by",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_status_history_changed_by"
        )
    )
    private User changedBy;

    @Column(name = "transition_reason", length = 255)
    private String transitionReason;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(
        name = "changed_at",
        nullable = false,
        insertable = false,
        updatable = false
    )
    private LocalDateTime changedAt;
}
