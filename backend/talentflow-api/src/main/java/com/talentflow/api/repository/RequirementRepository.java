package com.talentflow.api.repository;

import com.talentflow.api.entity.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequirementRepository
        extends JpaRepository<Requirement, Long> {

    Optional<Requirement> findByRrNumber(String rrNumber);

    boolean existsByRrNumber(String rrNumber);
}