package com.study.enums;

public enum HttpStatusCode {
    OK("200 OK", 200),
    NOT_FOUND("404 Not Found", 404),
    INTERNAL_SERVER_ERROR("500 Internal Server Error", 500);

    private final String message;
    private final int code;


    HttpStatusCode(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public static String getMessage(HttpStatusCode name) {
        return name.message;
    }

    public static int getCode(HttpStatusCode name) {
        return name.code;
    }
}
