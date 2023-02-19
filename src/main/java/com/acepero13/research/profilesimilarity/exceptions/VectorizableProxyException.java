package com.acepero13.research.profilesimilarity.exceptions;


public class VectorizableProxyException extends RuntimeException {
    public VectorizableProxyException(String msg) {
        super(msg);
    }

    public VectorizableProxyException(String msg, Throwable e) {
        super(msg, e);
    }
}
