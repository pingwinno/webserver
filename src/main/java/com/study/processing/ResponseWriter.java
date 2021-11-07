package com.study.processing;

import com.study.enums.HttpStatusCode;
import com.study.exceptions.HttpExceptions;
import com.study.exceptions.InternalServerErrorException;
import com.study.exceptions.ResourceNotFoundException;
import com.study.models.Request;
import com.study.readers.ResourceReader;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.study.enums.HttpStatusCode.INTERNAL_SERVER_ERROR;
import static com.study.enums.HttpStatusCode.NOT_FOUND;
import static com.study.enums.HttpStatusCode.OK;
import static com.study.enums.HttpStatusCode.getMessage;

public class ResponseWriter {
    private static final String HTTP_PROTOCOL_TEMPLATE = "HTTP/1.1 %s";
    private static final String HEADER_TEMPLATE = "%s: %s";
    private static final Map<Class<? extends HttpExceptions>, HttpStatusCode> ERROR_CODES =
            Map.of(ResourceNotFoundException.class, NOT_FOUND,
                    InternalServerErrorException.class, INTERNAL_SERVER_ERROR);

    public static void writeResponse(Request request, ResourceReader reader, OutputStream outputStream) throws IOException {
        outputStream = new BufferedOutputStream(outputStream);
        var response = reader.getResponseBody(request);
        outputStream.write(buildHttpMessage(response.getHeaders()).getBytes(StandardCharsets.UTF_8));
        outputStream.write(response.getBody());
        outputStream.flush();
    }

    public static void writeError(HttpExceptions e, OutputStream outputStream) throws IOException {
        outputStream = new BufferedOutputStream(outputStream);
        outputStream.write(String.format(HTTP_PROTOCOL_TEMPLATE,
                        getMessage(ERROR_CODES.get(e.getClass())))
                .getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }

    private static String buildHttpMessage(Map<String, String> headers) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(HTTP_PROTOCOL_TEMPLATE, getMessage(OK)));
        stringBuilder.append("\n");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            stringBuilder.append(String.format(HEADER_TEMPLATE, header.getKey(), header.getValue()));
            stringBuilder.append("\n");
        }
        stringBuilder.append("\r\n");
        return stringBuilder.toString();
    }
}
