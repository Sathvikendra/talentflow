
package com.talentflow.api.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger logger =
        LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Standard error response builder
    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        Map<String, Object> error = new HashMap<>();

        error.put("success", false);
        error.put("message", message);
        error.put("timestamp", LocalDateTime.now());
        error.put("path", request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }

    // Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<Map<String, String>> errors =
                ex.getBindingResult().getFieldErrors()
                        .stream()
                        .map(error -> Map.of(
                                "field", error.getField(),
                                "message", error.getDefaultMessage() != null
                                        ? error.getDefaultMessage()
                                        : "Invalid value"))
                        .toList();

        Map<String, Object> response = new HashMap<>();

        response.put("success", false);
        response.put("message", "Validation failed");
        response.put("timestamp", LocalDateTime.now());
        response.put("path", request.getRequestURI());
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }

    // Not found exceptions
    @ExceptionHandler({
            ClientNotFoundException.class,
            UserNotFoundException.class,
            RequirementNotFoundException.class,
            RequirementSkillNotFoundException.class,
            SkillNotFoundException.class,
            CandidateNotFoundException.class,
            CandidateSubmissionNotFoundException.class,
            StatusHistoryNotFoundException.class,
            ResumeDocumentNotFoundException.class,
            CandidateSkillNotFoundException.class,
            NotificationNotFoundException.class,
            ClientInterviewNotFoundException.class,
                L1EvaluationNotFoundException.class,
                AIAssessmentNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFound(
            RuntimeException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request);
    }

    // Duplicate resource
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateResource(
            DuplicateResourceException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request);
    }

    // Invalid request or business rule violation
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request);
    }

    // Operation conflicts with the current resource state
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(
            IllegalStateException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request);
    }

    // Authenticated user lacks permission
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.FORBIDDEN,
                "You do not have permission to perform this action",
                request);
    }

    // Unexpected exceptions
    @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, Object>> handleGeneralExceptions(
                Exception ex,
                HttpServletRequest request) {

        logger.error(
                "Unexpected error at {}: {}",
                request.getRequestURI(),
                ex.getMessage(),
                ex
        );

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected internal server error occurred",
                request);
        }

        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<Map<String, Object>> handleNoResourceFound(
                NoResourceFoundException ex,
                HttpServletRequest request) {

                return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "API endpoint not found",
                request
                );
        }

        // Authentication failures
        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<Map<String, Object>> handleAuthenticationException(
                AuthenticationException ex,
                HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Invalid username or password",
                request);
        }
        
}
