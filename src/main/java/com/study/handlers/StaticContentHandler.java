package com.study.handlers;

import com.study.enums.HttpMethod;
import com.study.exceptions.MethodNotAllowedException;
import com.study.models.Request;
import com.study.models.Response;
import com.study.readers.ResourceReader;

public class StaticContentHandler implements Handler {
    private final ResourceReader reader;

    public StaticContentHandler(ResourceReader reader) {
        this.reader = reader;
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getHttpMethod() != HttpMethod.GET) {
            throw new MethodNotAllowedException();
        }
        return reader.getResponseBody(request);
    }
}
