package com.acepero13.research.profilesimilarity.exceptions;


/**
 * An exception class that is thrown when an error occurs while working with a VectorizableProxy object.
 */
public class VectorizableProxyException extends RuntimeException {

    /**
     * Constructs a new VectorizableProxyException with the specified detail message.
     *
     * @param msg the detail message
     */
    public VectorizableProxyException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new VectorizableProxyException with the specified detail message and cause.
     *
     * @param msg the detail message
     * @param e   the cause of the exception
     */
    public VectorizableProxyException(String msg, Throwable e) {
        super(msg, e);
    }
}
