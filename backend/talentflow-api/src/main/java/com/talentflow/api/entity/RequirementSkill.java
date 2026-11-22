package com.talentflow.api.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "requirement_skill")
public class RequirementSkill {

    @EmbeddedId
    private RequirementSkillId id;

    @ManyToOne(optional = false)
    @MapsId("requirementId")
    @JoinColumn(name = "requirement_id", nullable = false)
    private Requirement requirement;

    @ManyToOne(optional = false)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "importance", nullable = false, length = 20)
    private String importance;

    @Column(
            name = "minimum_years",
            precision = 4,
            scale = 1
    )
    private BigDecimal minimumYears;

    @Column(
            name = "created_at",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    public RequirementSkillId getId() {
        return id;
    }

    public void setId(RequirementSkillId id) {
        this.id = id;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public BigDecimal getMinimumYears() {
        return minimumYears;
    }

    public void setMinimumYears(BigDecimal minimumYears) {
        this.minimumYears = minimumYears;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}