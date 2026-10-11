package com.talentflow.api.repository;

import com.talentflow.api.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository
        extends JpaRepository<Candidate, Long> {

    boolean existsByCandidateReference(
            String candidateReference
    );
}