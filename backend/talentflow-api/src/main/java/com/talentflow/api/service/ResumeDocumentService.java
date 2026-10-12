package com.talentflow.api.service;

import com.talentflow.api.dto.ResumeDocumentDTO;
import com.talentflow.api.entity.Candidate;
import com.talentflow.api.entity.ResumeDocument;
import com.talentflow.api.entity.User;
import com.talentflow.api.exception.ResumeDocumentNotFoundException;
import com.talentflow.api.repository.CandidateRepository;
import com.talentflow.api.repository.ResumeDocumentRepository;
import com.talentflow.api.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeDocumentService {

    private final ResumeDocumentRepository resumeRepository;
    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;

    public ResumeDocumentService(
            ResumeDocumentRepository resumeRepository,
            CandidateRepository candidateRepository,
            UserRepository userRepository
    ) {
        this.resumeRepository = resumeRepository;
        this.candidateRepository = candidateRepository;
        this.userRepository = userRepository;
    }

    public ResumeDocument createResume(
            ResumeDocumentDTO.CreateResumeDocumentRequest request
    ) {

        Candidate candidate =
                candidateRepository
                        .findById(request.candidateId())
                        .orElseThrow();

        User user =
                userRepository
                        .findById(request.uploadedBy())
                        .orElseThrow();

        ResumeDocument resume =
                new ResumeDocument();

        resume.setCandidate(candidate);
        resume.setOriginalFileName(
                request.originalFileName()
        );

        resume.setStoredFileName(
                request.storedFileName()
        );

        resume.setStoragePath(
                request.storagePath()
        );

        resume.setContentType(
                request.contentType()
        );

        resume.setFileSizeBytes(
                request.fileSizeBytes()
        );

        resume.setFileHash(
                request.fileHash()
        );

        resume.setExtractedText(
                request.extractedText()
        );

        resume.setUploadedBy(user);

        return resumeRepository.save(
                resume
        );
    }

    public List<ResumeDocument> getAllResumes() {

        return resumeRepository.findAll();
    }

    public ResumeDocument getResumeById(
            Long id
    ) {

        return resumeRepository.findById(id)
                .orElseThrow(() ->
                        new ResumeDocumentNotFoundException(
                                "Resume not found with id: "
                                        + id
                        ));
    }
}