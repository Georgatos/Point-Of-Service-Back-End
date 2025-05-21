package dev.andreasgeorgatos.pointofservice.utils;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.List;

public final class ValidationUtils { // Added final

    private ValidationUtils() { // Added private constructor
    }

    /**
     * Extracts error messages from BindingResult.
     *
     * @param bindingResult The binding result containing validation errors
     * @return A list of error messages
     */
    public static List<String> getValidationErrors(BindingResult bindingResult) {
        return bindingResult
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList(); // Assumes Java 16+ for .toList()
    }
}
