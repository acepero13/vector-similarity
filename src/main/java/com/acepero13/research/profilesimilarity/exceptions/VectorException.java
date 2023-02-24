package com.acepero13.research.profilesimilarity.exceptions;

/**
 * A {@code RuntimeException} that indicates an error occurred while performing an operation on a {@code Vector}.
 */
public class VectorException extends RuntimeException {

    /**
     * Constructs a new {@code VectorException} with the specified detail message.
     *
     * @param message The detail message.
     */
    public VectorException(String message) {
        super(message);
    }
}