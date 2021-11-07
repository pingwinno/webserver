package com.study.processing;

import com.study.exceptions.InternalServerErrorException;
import com.study.exceptions.ResourceNotFoundException;
import com.study.models.Request;
import com.study.models.Response;
import com.study.readers.ResourceReader;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResponseWriterTest {
    private static final String NOT_FOUND_MESSAGE = "HTTP/1.1 404 Not Found";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "HTTP/1.1 500 Internal Server Error";
    private static final byte[] FILE = "SomeFile".getBytes(StandardCharsets.UTF_8);
    private static final String EXPECTED_HTTP_MESSAGE = """
            HTTP/1.1 200 OK
            Content-Type: someType
            Content-Length: 1
            \r
            SomeFile""";
    private final ResourceReader reader = mock(ResourceReader.class);

    @Test
    void should_writeOkAndFile_when_passValidRequestObject() throws IOException {
        var outputStream = new ByteArrayOutputStream();
        var request = new Request();
        request.setPath("path");
        when(reader.getResponseBody(request))
                .thenReturn(Response.builder()
                        .body(FILE).headers(Map.of("Content-Type", "someType", "Content-Length", "1"))
                        .build());
        request.setPath("path");
        ResponseWriter.writeResponse(request, reader, outputStream);
        outputStream.flush();
        assertEquals(EXPECTED_HTTP_MESSAGE, outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    void should_writeNotFoundMessage_when_passNotFoundException() throws IOException {
        var outputStream = new ByteArrayOutputStream();
        ResponseWriter.writeError(new ResourceNotFoundException(), outputStream);
        assertEquals(NOT_FOUND_MESSAGE, outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    void should_writeInternalServerError_when_passInternalServerErrorException() throws IOException {
        var outputStream = new ByteArrayOutputStream();
        ResponseWriter.writeError(new InternalServerErrorException(), outputStream);
        assertEquals(INTERNAL_SERVER_ERROR_MESSAGE, outputStream.toString(StandardCharsets.UTF_8));
    }
}