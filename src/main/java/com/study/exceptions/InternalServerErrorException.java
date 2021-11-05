package com.study.exceptions;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(Throwable cause) {
        super(cause);
    }

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
