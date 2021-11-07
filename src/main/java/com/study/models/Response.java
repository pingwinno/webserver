package com.study.models;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Response {

    private Map<String, String> headers;
    private byte[] body;

}
