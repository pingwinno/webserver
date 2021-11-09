package com.study.processing;

import com.study.exceptions.BadRequestException;
import com.study.exceptions.InternalServerErrorException;
import com.study.exceptions.MethodNotAllowedException;
import com.study.exceptions.NotFoundException;
import com.study.models.Response;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseWriterTest {
    private static final String BAD_REQUEST = "HTTP/1.1 400 Bad Request";
    private static final String NOT_FOUND_MESSAGE = "HTTP/1.1 404 Not Found";
    private static final String METHOD_NOT_ALLOWED = "HTTP/1.1 405 Method Not Allowed";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "HTTP/1.1 500 Internal Server Error";
    private static final byte[] FILE = "SomeFile".getBytes(StandardCharsets.UTF_8);
    private static final String EXPECTED_HTTP_MESSAGE = """
            HTTP/1.1 200 OK
            Content-Type: someType
            Content-Length: 1
            \r
            SomeFile""";

    @Test
    void should_writeOkAndFile_when_passValidRequestObject() throws IOException {
        var outputStream = new ByteArrayOutputStream();
        var headers = new LinkedHashMap<String, String>();
        headers.put("Content-Type", "someType");
        headers.put("Content-Length", "1");
        ResponseWriter.writeResponse(Response.builder()
                .body(FILE)
                .headers(headers)
                .build(), outputStream);
        outputStream.flush();
        assertEquals(EXPECTED_HTTP_MESSAGE, outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    void should_writeNotFoundMessage_when_passNotFoundException() throws IOException {
        var outputStream = new ByteArrayOutputStream();
        ResponseWriter.writeError(new NotFoundException(), outputStream);
        assertEquals(NOT_FOUND_MESSAGE, outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    void should_writeInternalServerError_when_passInternalServerErrorException() throws IOException {
        var outputStream = new ByteArrayOutputStream();
        ResponseWriter.writeError(new InternalServerErrorException(), outputStream);
        assertEquals(INTERNAL_SERVER_ERROR_MESSAGE, outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    void should_writeMethodNotAllowedError_when_passMethodNotAllowedException() throws IOException {
        var outputStream = new ByteArrayOutputStream();
        ResponseWriter.writeError(new MethodNotAllowedException(), outputStream);
        assertEquals(METHOD_NOT_ALLOWED, outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    void should_writeBadRequestError_when_passBadRequestException() throws IOException {
        var outputStream = new ByteArrayOutputStream();
        ResponseWriter.writeError(new BadRequestException(), outputStream);
        assertEquals(BAD_REQUEST, outputStream.toString(StandardCharsets.UTF_8));
    }
}