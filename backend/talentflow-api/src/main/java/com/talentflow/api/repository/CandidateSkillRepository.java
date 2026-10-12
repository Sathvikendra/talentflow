package com.talentflow.api.repository;

import com.talentflow.api.entity.CandidateSkill;
import com.talentflow.api.entity.CandidateSkillId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateSkillRepository
        extends JpaRepository<
        CandidateSkill,
        CandidateSkillId
        > {
}