package com.study.readers;

import com.study.models.Request;
import com.study.models.Response;

public interface ResourceReader {
    Response getResponseBody(Request request);
}
