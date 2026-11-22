package com.talentflow.api.controller;

import com.talentflow.api.dto.ResumeDocumentDTO;
import com.talentflow.api.service.ResumeDocumentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resumes")
public class ResumeDocumentController {

    private final ResumeDocumentService resumeDocumentService;

    public ResumeDocumentController(
            ResumeDocumentService resumeDocumentService
    ) {
        this.resumeDocumentService = resumeDocumentService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeDocumentDTO.ResumeDocumentResponse> uploadResume(
            @RequestParam Long candidateId,
            @RequestParam Long uploadedBy,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resumeDocumentService.uploadResume(
                        candidateId,
                        uploadedBy,
                        file
                ));
    }

    @GetMapping
    public ResponseEntity<List<ResumeDocumentDTO.ResumeDocumentResponse>>
    getAllResumes() {
        return ResponseEntity.ok(
                resumeDocumentService.getAllResumes()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeDocumentDTO.ResumeDocumentResponse>
    getResumeById(@PathVariable Long id) {
        return ResponseEntity.ok(
                resumeDocumentService.getResumeById(id)
        );
    }
}
