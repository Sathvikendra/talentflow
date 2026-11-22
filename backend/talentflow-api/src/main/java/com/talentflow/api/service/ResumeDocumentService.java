package com.talentflow.api.service;

import com.talentflow.api.dto.ResumeDocumentDTO;
import com.talentflow.api.entity.Candidate;
import com.talentflow.api.entity.ResumeDocument;
import com.talentflow.api.entity.User;
import com.talentflow.api.exception.ResumeDocumentNotFoundException;
import com.talentflow.api.repository.CandidateRepository;
import com.talentflow.api.repository.ResumeDocumentRepository;
import com.talentflow.api.repository.UserRepository;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Service
public class ResumeDocumentService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private final ResumeDocumentRepository resumeRepository;
    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;
    private final ResumeStorageService resumeStorageService;

    public ResumeDocumentService(
            ResumeDocumentRepository resumeRepository,
            CandidateRepository candidateRepository,
            UserRepository userRepository,
            ResumeStorageService resumeStorageService
    ) {
        this.resumeRepository = resumeRepository;
        this.candidateRepository = candidateRepository;
        this.userRepository = userRepository;
        this.resumeStorageService = resumeStorageService;
    }

    public ResumeDocumentDTO.ResumeDocumentResponse uploadResume(
            Long candidateId,
            Long uploadedBy,
            MultipartFile file
    ) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please select a PDF file");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size must not exceed 10 MB");
        }

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null
                || !originalFileName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        if (file.getContentType() != null
                && !file.getContentType().equalsIgnoreCase("application/pdf")) {
            throw new IllegalArgumentException("Invalid PDF content type");
        }

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));

        User user = userRepository.findById(uploadedBy)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String storedFileName = UUID.randomUUID() + ".pdf";
        Path storedFilePath = null;

        try {
            byte[] fileBytes = file.getBytes();

            String fileHash = calculateSha256(fileBytes);
            String extractedText = extractPdfText(fileBytes);

            storedFilePath = resumeStorageService.saveFile(
                    storedFileName,
                    fileBytes
            );

            ResumeDocument resume = new ResumeDocument();
            resume.setCandidate(candidate);
            resume.setOriginalFileName(Path.of(originalFileName).getFileName().toString());
            resume.setStoredFileName(storedFileName);
            resume.setStoragePath(storedFilePath.toString());
            resume.setContentType("application/pdf");
            resume.setFileSizeBytes(file.getSize());
            resume.setFileHash(fileHash);
            resume.setExtractedText(extractedText);
            resume.setParsingStatus("COMPLETED");
            resume.setUploadedBy(user);

            ResumeDocument savedResume = resumeRepository.save(resume);

            return new ResumeDocumentDTO.ResumeDocumentResponse(
                    savedResume.getId(),
                    candidate.getId(),
                    savedResume.getOriginalFileName(),
                    savedResume.getStoredFileName(),
                    savedResume.getContentType(),
                    savedResume.getFileSizeBytes(),
                    savedResume.getParsingStatus()
            );

        } catch (IOException exception) {
            deleteStoredFile(storedFilePath);
            throw new RuntimeException("Could not process the uploaded PDF", exception);
        } catch (RuntimeException exception) {
            deleteStoredFile(storedFilePath);
            throw exception;
        }
    }

    private String calculateSha256(byte[] fileBytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(fileBytes));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 algorithm is unavailable", exception);
        }
    }

    private String extractPdfText(byte[] fileBytes) throws IOException {
        try (var document = Loader.loadPDF(fileBytes)) {
            return new PDFTextStripper().getText(document);
        }
    }

    private void deleteStoredFile(Path path) {
        if (path == null) {
            return;
        }

        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
            // Preserve the original upload failure.
        }
    }

    private ResumeDocumentDTO.ResumeDocumentResponse toResponse(
        ResumeDocument resume
        ) {
        return new ResumeDocumentDTO.ResumeDocumentResponse(
                resume.getId(),
                resume.getCandidate().getId(),
                resume.getOriginalFileName(),
                resume.getStoredFileName(),
                resume.getContentType(),
                resume.getFileSizeBytes(),
                resume.getParsingStatus()
        );
        }

    public List<ResumeDocumentDTO.ResumeDocumentResponse> getAllResumes() {
        return resumeRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public ResumeDocumentDTO.ResumeDocumentResponse getResumeById(Long id) {
        ResumeDocument resume = resumeRepository.findById(id)
            .orElseThrow(() -> new ResumeDocumentNotFoundException(
                    "Resume not found with id: " + id
            ));

        return toResponse(resume);  
    }
}
