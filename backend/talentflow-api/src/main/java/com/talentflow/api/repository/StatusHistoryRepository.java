package com.talentflow.api.repository;

import com.talentflow.api.entity.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusHistoryRepository
        extends JpaRepository<StatusHistory, Long> {

    List<StatusHistory>
    findBySubmissionIdOrderByChangedAtAscIdAsc(Long submissionId);

    List<StatusHistory>
    findAllByOrderByChangedAtAscIdAsc();

    List<StatusHistory>
    findByChangedByIdOrderByChangedAtAsc(Long changedById);

    boolean existsBySubmissionId(Long submissionId);
}
