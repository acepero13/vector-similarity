package com.acepero13.research.profilesimilarity.exceptions;

/**
 * The ArgumentException class is a subclass of RuntimeException that represents an
 * <p>
 * exception thrown when an invalid argument is passed to a method or constructor.
 */
public class ArgumentException extends RuntimeException {

    /**
     * Constructs a new ArgumentException with the specified error message.
     *
     * @param msg the error message associated with the exception
     */
    public ArgumentException(String msg) {
        super(msg);
    }
}