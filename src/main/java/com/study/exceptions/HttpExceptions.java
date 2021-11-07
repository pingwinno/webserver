package com.study.exceptions;

public class HttpExceptions extends RuntimeException {
    public HttpExceptions() {
    }

    public HttpExceptions(String message) {
        super(message);
    }

    public HttpExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpExceptions(Throwable cause) {
        super(cause);
    }

    public HttpExceptions(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
