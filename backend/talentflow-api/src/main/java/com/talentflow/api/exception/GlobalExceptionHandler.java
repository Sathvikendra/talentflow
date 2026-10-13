package com.talentflow.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
                );
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ClientNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleClientNotFound(ClientNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

    
    // Handle all other exceptions
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, String>> handleGeneralExceptions(Exception ex) {
                Map<String, String> error = new HashMap<>();
                error.put("error", ex.getMessage());
                return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(
                RequirementNotFoundException.class
        )
        public ResponseEntity<Map<String,String>>
        handleRequirementNotFound(
                RequirementNotFoundException ex
        ) {

        Map<String,String> error =
                new HashMap<>();

        error.put(
                "error",
                ex.getMessage()
        );

        return new ResponseEntity<>(
                error,
                HttpStatus.NOT_FOUND
        );
        }

        @ExceptionHandler(RequirementSkillNotFoundException.class)
        public ResponseEntity<?> handleRequirementSkillNotFound(
                RequirementSkillNotFoundException ex
        ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
        }

        @ExceptionHandler(
                SkillNotFoundException.class
        )
        public ResponseEntity<?> handleSkillNotFound(
                SkillNotFoundException ex
        ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
        }

        @ExceptionHandler(CandidateNotFoundException.class)
        public ResponseEntity<?> handleCandidateNotFound(
                CandidateNotFoundException ex
        ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
        }


        @ExceptionHandler(CandidateSubmissionNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleCandidateSubmissionNotFound(
                CandidateSubmissionNotFoundException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(DuplicateResourceException.class)
        public ResponseEntity<Map<String, String>> handleDuplicateResource(
                DuplicateResourceException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }

        
        @ExceptionHandler(StatusHistoryNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleStatusHistoryNotFound(
                StatusHistoryNotFoundException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(
                ResumeDocumentNotFoundException.class
        )
        public ResponseEntity<?> handleResumeNotFound(
                ResumeDocumentNotFoundException ex
        ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
        }

        @ExceptionHandler(CandidateSkillNotFoundException.class)
        public ResponseEntity<?> handleCandidateSkillNotFound(
                CandidateSkillNotFoundException ex
        ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
        }

        
        @ExceptionHandler(NotificationNotFoundException.class)
        public ResponseEntity<?> handleNotificationNotFound(
                NotificationNotFoundException exception,
                HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                "success", false,
                "message", exception.getMessage(),
                "timestamp", LocalDateTime.now(),
                "path", request.getRequestURI()
                )
        );
        }


}
