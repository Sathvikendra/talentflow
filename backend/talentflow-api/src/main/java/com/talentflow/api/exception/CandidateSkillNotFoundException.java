package com.talentflow.api.exception;

public class CandidateSkillNotFoundException 
        extends RuntimeException {

    public CandidateSkillNotFoundException(
            String message
    ) {
        super(message);
    }
}