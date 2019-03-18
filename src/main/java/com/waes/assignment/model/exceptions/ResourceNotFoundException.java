package com.waes.assignment.model.exceptions;

/**
 * Thrown when an operation is attempted on a non existent resource.
 */
public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message) {

        super(message);
    }
}
