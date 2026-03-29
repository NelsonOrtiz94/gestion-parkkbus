package com.corhuila.parkkbus.shared.exception;

/**
 * Thrown when a requested resource is not found in the domain.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

