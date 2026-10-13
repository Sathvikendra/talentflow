package com.talentflow.api.exception;

public class L1EvaluationNotFoundException extends RuntimeException {
    public L1EvaluationNotFoundException(Long id) {
        super("L1 evaluation not found with ID: " + id);
    }
}
