package com.talentflow.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(
            name = "normalized_name",
            nullable = false,
            unique = true, length = 100
    )
    private String normalizedName;

    @Column (name = "category", length = 100)
    private String category;

    @Column(
            name = "created_at",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   
    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}