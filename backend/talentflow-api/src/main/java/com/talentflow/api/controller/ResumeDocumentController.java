package com.talentflow.api.controller;

import com.talentflow.api.dto.ResumeDocumentDTO;
import com.talentflow.api.entity.ResumeDocument;
import com.talentflow.api.service.ResumeDocumentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resumes")
public class ResumeDocumentController {

    private final ResumeDocumentService resumeDocumentService;

    public ResumeDocumentController(
            ResumeDocumentService resumeDocumentService
    ) {
        this.resumeDocumentService =
                resumeDocumentService;
    }

    @PostMapping
    public ResponseEntity<ResumeDocument>
    createResume(
            @Valid
            @RequestBody
            ResumeDocumentDTO.CreateResumeDocumentRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        resumeDocumentService
                                .createResume(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<ResumeDocument>>
    getAllResumes() {

        return ResponseEntity.ok(
                resumeDocumentService
                        .getAllResumes()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeDocument>
    getResumeById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                resumeDocumentService
                        .getResumeById(id)
        );
    }
}