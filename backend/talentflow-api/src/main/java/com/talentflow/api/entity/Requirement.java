package com.talentflow.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "requirement")
public class Requirement {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(name = "rr_number", nullable = false, unique = true)
private String rrNumber;

@ManyToOne
@JoinColumn(name = "client_id", nullable = false)
private Client client;

@Column(nullable = false)
private String title;

@Column(nullable = false, columnDefinition = "TEXT")
private String description;

@Column(name = "employment_type")
private String employmentType;

private String location;

@Column(name = "work_mode")
private String workMode;

@Column(name = "onshore_or_offshore")
private String onshoreOrOffshore;

@Column(name = "min_experience_years", precision = 4, scale = 1)
private BigDecimal minExperienceYears;

@Column(name = "max_experience_years", precision = 4, scale = 1)
private BigDecimal maxExperienceYears;

@Column(name = "positions_count")
private Integer positionsCount;

private String priority;

private String status = "OPEN";

@ManyToOne
@JoinColumn(name = "created_by")
private User createdBy;

@Column(name = "opened_at", insertable = false, updatable = false)
private LocalDateTime openedAt;

@Column(name = "target_fill_date")
private LocalDate targetFillDate;

@Column(name = "closed_at")
private LocalDateTime closedAt;

@Column(name = "created_at", insertable = false, updatable = false)
private LocalDateTime createdAt;

@Column(name = "updated_at", insertable = false, updatable = false)
private LocalDateTime updatedAt;


// Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRrNumber() {
        return rrNumber;
    }

    public void setRrNumber(String rrNumber) {
        this.rrNumber = rrNumber;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public String getOnshoreOrOffshore() {
        return onshoreOrOffshore;
    }

    public void setOnshoreOrOffshore(String onshoreOrOffshore) {
        this.onshoreOrOffshore = onshoreOrOffshore;
    }

    public BigDecimal getMinExperienceYears() {
        return minExperienceYears;
    }

    public void setMinExperienceYears(BigDecimal minExperienceYears) {
        this.minExperienceYears = minExperienceYears;
    }

    public BigDecimal getMaxExperienceYears() {
        return maxExperienceYears;
    }

    public void setMaxExperienceYears(BigDecimal maxExperienceYears) {
        this.maxExperienceYears = maxExperienceYears;
    }

    public Integer getPositionsCount() {
        return positionsCount;
    }

    public void setPositionsCount(Integer positionsCount) {
        this.positionsCount = positionsCount;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(LocalDateTime openedAt) {
        this.openedAt = openedAt;
    }

    public LocalDate getTargetFillDate() {
        return targetFillDate;
    }

    public void setTargetFillDate(LocalDate targetFillDate) {
        this.targetFillDate = targetFillDate;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}