package com.talentflow.api.dto;

public class ResumeDocumentDTO {

    public record CreateResumeDocumentRequest(

            Long candidateId,

            String originalFileName,

            String storedFileName,

            String storagePath,

            String contentType,

            Long fileSizeBytes,

            String fileHash,

            String extractedText,

            Long uploadedBy

    ) {}

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
