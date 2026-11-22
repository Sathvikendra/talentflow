package com.talentflow.api.repository;

import com.talentflow.api.entity.CandidateSubmission;
import com.talentflow.api.entity.CandidateSubmission.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CandidateSubmissionRepository
        extends JpaRepository<CandidateSubmission, Long> {

    boolean existsByCandidateIdAndRequirementId(
        Long candidateId,
        Long requirementId
    );

    Optional<CandidateSubmission> findByCandidateIdAndRequirementId(
        Long candidateId,
        Long requirementId
    );

    List<CandidateSubmission> findByCandidateId(Long candidateId);

    List<CandidateSubmission> findByRequirementId(Long requirementId);

    List<CandidateSubmission> findByCurrentStatus(
        SubmissionStatus currentStatus
    );

    List<CandidateSubmission> findByAssignedToId(Long assignedToId);

    long countByCurrentStatus(SubmissionStatus currentStatus);

    long countByAssignedToIsNotNull();

    long countByAssignedToIsNull();

    @Query("SELECT s.currentStatus, COUNT(s) " +
           "FROM CandidateSubmission s " +
           "GROUP BY s.currentStatus")
    List<Object[]> countSubmissionsByStatus();
}