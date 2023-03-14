package com.acepero13.research.profilesimilarity.exceptions;

import java.util.Objects;

/**
 * Exception class used to indicate errors related to k-nearest neighbors algorithm.
 */
public class PredictionException extends RuntimeException {

    /**
     * Constructor that creates a new KnnException with the specified message.
     *
     * @param msg the message to be associated with the exception.
     */
    public PredictionException(String msg) {
        super(Objects.requireNonNull(msg));
    }
}