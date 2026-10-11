package com.talentflow.api.service;

import com.talentflow.api.dto.RequirementSkillDTO;
import com.talentflow.api.entity.Requirement;
import com.talentflow.api.entity.RequirementSkill;
import com.talentflow.api.entity.RequirementSkillId;
import com.talentflow.api.entity.Skill;
import com.talentflow.api.repository.RequirementRepository;
import com.talentflow.api.repository.RequirementSkillRepository;
import com.talentflow.api.repository.SkillRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequirementSkillService {

    private final RequirementSkillRepository requirementSkillRepository;
    private final RequirementRepository requirementRepository;
    private final SkillRepository skillRepository;

    public RequirementSkillService(
            RequirementSkillRepository requirementSkillRepository,
            RequirementRepository requirementRepository,
            SkillRepository skillRepository
    ) {
        this.requirementSkillRepository = requirementSkillRepository;
        this.requirementRepository = requirementRepository;
        this.skillRepository = skillRepository;
    }

    public RequirementSkill createRequirementSkill(
            RequirementSkillDTO.CreateRequirementSkillRequest request
    ) {

      Requirement requirement =
        requirementRepository.findById(
                request.requirementId()
        )
        .orElseThrow(() ->
                new IllegalArgumentException(
                        "Requirement not found"
                ));

Skill skill =
        skillRepository.findById(
                request.skillId()
        )
        .orElseThrow(() ->
                new IllegalArgumentException(
                        "Skill not found"
                ));

        RequirementSkill rs = new RequirementSkill();

        rs.setId(
                new RequirementSkillId(
                        request.requirementId(),
                        request.skillId()
                )
        );

        rs.setRequirement(requirement);
        rs.setSkill(skill);

        rs.setImportance(
                request.importance()
        );

        rs.setMinimumYears(
                request.minimumYears()
        );

        return requirementSkillRepository.save(rs);
    }

    public List<RequirementSkill> getAllRequirementSkills() {
        return requirementSkillRepository.findAll();
    }
}