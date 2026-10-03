package com.talentflow.api.exception;

public class RequirementNotFoundException
        extends RuntimeException {

    public RequirementNotFoundException(
            String message
    ) {
        super(message);
    }
}
