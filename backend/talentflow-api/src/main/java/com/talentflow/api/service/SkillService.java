package com.talentflow.api.service;

import com.talentflow.api.dto.SkillDTO;
import com.talentflow.api.entity.Skill;
import com.talentflow.api.exception.SkillNotFoundException;
import com.talentflow.api.repository.SkillRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(
            SkillRepository skillRepository
    ) {
        this.skillRepository = skillRepository;
    }

    public Skill createSkill(
            SkillDTO.CreateSkillRequest request
    ) {

        if (
            skillRepository.existsByNormalizedName(
                    request.normalizedName()
            )
        ) {

            throw new IllegalArgumentException(
                    "Skill already exists"
            );
        }

        Skill skill = new Skill();

        skill.setName(
                request.name()
        );

        skill.setNormalizedName(
                request.normalizedName()
        );

        skill.setCategory(
                request.category()
        );

        return skillRepository.save(skill);
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Skill getSkillById(
            Long id
    ) {

        return skillRepository
                .findById(id)
                .orElseThrow(() ->
                        new SkillNotFoundException(
                                "Skill not found with id: " + id
                        ));
    }

    public Skill updateSkill(
            Long id,
            SkillDTO.UpdateSkillRequest request
    ) {

        Skill skill = getSkillById(id);

        if(request.name() != null){
            skill.setName(request.name());
        }

        if(request.normalizedName() != null){
            skill.setNormalizedName(
                    request.normalizedName()
            );
        }

        if(request.category() != null){
            skill.setCategory(
                    request.category()
            );
        }

        return skillRepository.save(skill);
    }

    public void deleteSkill(
            Long id
    ) {

        Skill skill = getSkillById(id);

        skillRepository.delete(skill);
    }
}