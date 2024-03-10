package com.lzl.core.exception;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 6761215662681126741L;
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    public ResourceNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

}
