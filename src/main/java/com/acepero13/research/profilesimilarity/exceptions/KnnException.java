package com.acepero13.research.profilesimilarity.exceptions;

/**
 * Exception class used to indicate errors related to k-nearest neighbors algorithm.
 */
public class KnnException extends RuntimeException {

    /**
     * Constructor that creates a new KnnException with the specified message.
     *
     * @param msg the message to be associated with the exception.
     */
    public KnnException(String msg) {
        super(msg);
    }
}