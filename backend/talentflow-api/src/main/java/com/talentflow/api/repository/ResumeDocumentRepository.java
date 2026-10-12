package com.talentflow.api.repository;

import com.talentflow.api.entity.ResumeDocument;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeDocumentRepository
        extends JpaRepository<ResumeDocument, Long> {
}