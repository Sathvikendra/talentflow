package com.talentflow.api.dto;

public class ResumeDocumentDTO {

    public record ResumeDocumentResponse(
            Long id,
            Long candidateId,
            String originalFileName,
            String storedFileName,
            String contentType,
            Long fileSizeBytes,
            String parsingStatus
    ) {}
}
