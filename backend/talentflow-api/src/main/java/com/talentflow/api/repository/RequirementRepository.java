package com.talentflow.api.repository;

import com.talentflow.api.entity.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RequirementRepository
        extends JpaRepository<Requirement, Long> {

    Optional<Requirement> findByRrNumber(String rrNumber);

    boolean existsByRrNumber(String rrNumber);

    long countByStatus(String status);

    @Query(
        "SELECT COALESCE(SUM(r.positionsCount), 0) FROM Requirement r"
    )
    Long getTotalPositions();
}