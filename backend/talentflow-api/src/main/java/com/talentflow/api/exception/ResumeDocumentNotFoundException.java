package com.talentflow.api.exception;

public class ResumeDocumentNotFoundException
        extends RuntimeException {

    public ResumeDocumentNotFoundException(
            String message
    ) {
        super(message);
    }
}
