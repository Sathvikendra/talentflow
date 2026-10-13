package com.talentflow.api.exception;

public class ClientInterviewNotFoundException extends RuntimeException {
    public ClientInterviewNotFoundException(Long id) {
        super("Client interview not found with ID: " + id);
    }
}
