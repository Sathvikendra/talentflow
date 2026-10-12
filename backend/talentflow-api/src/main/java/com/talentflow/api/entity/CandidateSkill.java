package com.talentflow.api.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidate_skill")
public class CandidateSkill {

    @EmbeddedId
    private CandidateSkillId id;

    @ManyToOne
    @MapsId("candidateId")
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private String proficiency;

    @Column(
            name = "years_experience",
            precision = 4,
            scale = 1
    )
    private BigDecimal yearsExperience;

    @Column(nullable = false)
    private String source;

    @Column(
            name = "created_at",
            insertable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            insertable = false,
            updatable = false
    )
    private LocalDateTime updatedAt;

    public CandidateSkillId getId() {
        return id;
    }

    public void setId(CandidateSkillId id) {
        this.id = id;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getProficiency() {
        return proficiency;
    }

    public void setProficiency(String proficiency) {
        this.proficiency = proficiency;
    }

    public BigDecimal getYearsExperience() {
        return yearsExperience;
    }

    public void setYearsExperience(BigDecimal yearsExperience) {
        this.yearsExperience = yearsExperience;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}