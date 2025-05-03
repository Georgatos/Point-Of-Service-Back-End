package dev.andreasgeorgatos.pointofservice.exception;

import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Global exception handler for centralizing error handling across the application.
 * Provides consistent error responses for various types of exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles validation exceptions thrown when @Valid annotation is used.
     *
     * @param ex The validation exception
     * @return A response entity with BAD_REQUEST status and a list of validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("Validation error occurred", ex);
        List<String> errors = ValidationUtils.getValidationErrors(ex.getBindingResult());
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles exceptions when an entity is not found.
     *
     * @param ex The entity not found exception
     * @return A response entity with NOT_FOUND status and the exception message
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.error("Entity not found", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Fallback handler for any unhandled exceptions.
     *
     * @param ex The exception
     * @return A response entity with INTERNAL_SERVER_ERROR status and generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("Unhandled exception occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred. Please try again later.");
    }
}