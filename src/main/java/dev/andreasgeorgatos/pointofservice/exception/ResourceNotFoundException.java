package dev.andreasgeorgatos.pointofservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource cannot be found.
 * Automatically maps to HTTP 404 Not Found response.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with a detailed message.
     *
     * @param resourceName The type of resource that was not found
     * @param fieldName The name of the field used for searching
     * @param fieldValue The value that was searched for
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }

    /**
     * Constructs a new ResourceNotFoundException with a simple resource and ID.
     *
     * @param resourceName The type of resource that was not found
     * @param id The ID that was searched for
     */
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s with id %d not found", resourceName, id));
    }

    /**
     * Constructs a new ResourceNotFoundException with a custom message.
     *
     * @param message The detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}