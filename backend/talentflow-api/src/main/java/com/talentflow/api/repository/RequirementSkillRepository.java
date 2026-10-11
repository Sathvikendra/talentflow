package com.talentflow.api.repository;

import com.talentflow.api.entity.RequirementSkill;
import com.talentflow.api.entity.RequirementSkillId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequirementSkillRepository
        extends JpaRepository<
        RequirementSkill,
        RequirementSkillId
        > {
}