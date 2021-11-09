package com.study.models;

import com.study.enums.HttpMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class Request {
    private String path;
    private HttpMethod httpMethod;
    private Map<String, String> headers;

}
