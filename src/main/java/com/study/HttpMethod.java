package com.study;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH;


    public static HttpMethod getByName(String name) {
        for (HttpMethod httpMethod : values()) {
            if (httpMethod.name().equalsIgnoreCase(name)) {
                return httpMethod;
            }
        }
        return null;
    }
}
