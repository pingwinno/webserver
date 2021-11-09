package com.study.handlers;

import com.study.models.Request;
import com.study.models.Response;

public interface Handler {
    Response handleRequest(Request request);
}
