package com.talentflow.api.exception;

public class AIAssessmentNotFoundException extends RuntimeException {
    public AIAssessmentNotFoundException(Long id) {
        super("AI assessment not found with ID: " + id);
    }
}
