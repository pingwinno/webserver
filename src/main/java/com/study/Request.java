package com.study;

import lombok.Data;

import java.util.Map;

@Data
public class Request {
    private String path;
    private HttpMethod httpMethod;
    private Map<String, String> headers = Map.of();

}
