package com.lzl.core.serialization;

public class SerializationException extends RuntimeException {

    private static final long serialVersionUID = 6761215662681126741L;

    public SerializationException() {
        super();
    }

    public SerializationException(String msg) {
        super(msg);
    }

    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }
}
