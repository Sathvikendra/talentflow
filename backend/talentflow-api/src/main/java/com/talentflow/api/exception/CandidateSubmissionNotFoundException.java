package com.talentflow.api.exception;

public class CandidateSubmissionNotFoundException
        extends RuntimeException {

    public CandidateSubmissionNotFoundException(String message) {
        super(message);
    }

    public CandidateSubmissionNotFoundException(Long id) {
        super("Candidate submission not found with ID: " + id);
    }
}
