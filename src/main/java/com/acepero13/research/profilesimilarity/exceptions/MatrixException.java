package com.acepero13.research.profilesimilarity.exceptions;

import java.util.Objects;

/**
 * An exception that is thrown when a matrix operation fails.
 */
public class MatrixException extends RuntimeException {

    /**
     * Constructs a new MatrixException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public MatrixException(String message) {
        super(Objects.requireNonNull(message));
    }
}