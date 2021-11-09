package com.study.handlers;

import com.study.enums.HttpMethod;
import com.study.exceptions.MethodNotAllowedException;
import com.study.models.Request;
import com.study.models.Response;
import com.study.readers.ResourceReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class StaticContentHandlerTest {

    private final ResourceReader reader = mock(ResourceReader.class);
    private StaticContentHandler handler = new StaticContentHandler(reader);

    @Test
    void should_returnResponse_when_passValidRequest() {
        var request = new Request();
        request.setHttpMethod(HttpMethod.GET);
        var expectedResponse = Response.builder().build();
        when(reader.getResponseBody(request)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, handler.handleRequest(request));
        verify(reader).getResponseBody(request);
    }

    @Test
    void should_throwMethodNotAllowedException_when_PassInvalidRequest() {
        var request = new Request();
        request.setHttpMethod(HttpMethod.POST);
        assertThrows(MethodNotAllowedException.class, () -> handler.handleRequest(request));
        verifyNoInteractions(reader);
    }
}