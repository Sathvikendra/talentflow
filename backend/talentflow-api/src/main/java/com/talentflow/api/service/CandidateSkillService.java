package com.talentflow.api.service;

import com.talentflow.api.dto.CandidateSkillDTO;
import com.talentflow.api.entity.*;

import com.talentflow.api.repository.CandidateRepository;
import com.talentflow.api.repository.CandidateSkillRepository;
import com.talentflow.api.repository.SkillRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateSkillService {
    private final CandidateSkillRepository candidateSkillRepository;
    private final CandidateRepository candidateRepository;
    private final SkillRepository skillRepository;

    public CandidateSkillService(
            CandidateSkillRepository candidateSkillRepository,
            CandidateRepository candidateRepository,
            SkillRepository skillRepository
    ) {
        this.candidateSkillRepository = candidateSkillRepository;
        this.candidateRepository = candidateRepository;
        this.skillRepository = skillRepository;
    }

    public CandidateSkill createCandidateSkill(
            CandidateSkillDTO.CreateCandidateSkillRequest request
    ) {

        Candidate candidate =
                candidateRepository.findById(
                        request.candidateId()
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Candidate not found"
                        ));

        Skill skill =
                skillRepository.findById(
                        request.skillId()
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Skill not found"
                        ));

        CandidateSkill candidateSkill =
                new CandidateSkill();
        candidateSkill.setId(
                new CandidateSkillId(
                        request.candidateId(),
                        request.skillId()
                )
        );

        candidateSkill.setCandidate(candidate);
        candidateSkill.setSkill(skill);

        candidateSkill.setProficiency(
                request.proficiency()
        );

        candidateSkill.setYearsExperience(
                request.yearsExperience()
        );

        candidateSkill.setSource(
                request.source()
        );

        return candidateSkillRepository.save(candidateSkill);
    }

    public List<CandidateSkill> getAllCandidateSkills() {
        return candidateSkillRepository.findAll();
    }
}