package com.talentflow.api.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RequirementSkillId implements Serializable {

    private Long requirementId;
    private Long skillId;

    public RequirementSkillId() {
    }

    public RequirementSkillId(Long requirementId, Long skillId) {
        this.requirementId = requirementId;
        this.skillId = skillId;
    }

    public Long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequirementSkillId)) return false;

        RequirementSkillId that = (RequirementSkillId) o;

        return Objects.equals(requirementId, that.requirementId)
                && Objects.equals(skillId, that.skillId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requirementId, skillId);
    }
}