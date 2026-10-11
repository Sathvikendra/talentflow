package com.talentflow.api.repository;

import com.talentflow.api.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository
        extends JpaRepository<Skill, Long> {

    Optional<Skill> findByNormalizedName(
            String normalizedName
    );

    boolean existsByNormalizedName(
            String normalizedName
    );
}