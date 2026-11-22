package com.talentflow.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.talentflow.api.entity.ClientInterview;
import com.talentflow.api.entity.ClientInterview.Result;

@Repository
public interface ClientInterviewRepository extends JpaRepository<ClientInterview, Long> {
    List<ClientInterview> findAllByScheduledAtIsNotNullOrderByScheduledAtDesc(
        Pageable pageable
    );

    List<ClientInterview> findAllByConductedAtIsNotNullOrderByConductedAtDesc(
            Pageable pageable
    );

    List<ClientInterview> findBySubmissionId(Long submissionId);

    List<ClientInterview> findByResult(Result result);

    long countByResult(Result result);

    long countByResultIsNull();

    @Query("""
        SELECT COUNT(i)
        FROM ClientInterview i
        WHERE i.scheduledAt >= :now
          AND i.result IS NULL
    """)
    long countUpcomingInterviews(@Param("now") LocalDateTime now);
}
