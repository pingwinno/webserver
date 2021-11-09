package com.study.processing;

import com.study.enums.HttpStatusCode;
import com.study.exceptions.BadRequestException;
import com.study.exceptions.HttpException;
import com.study.exceptions.InternalServerErrorException;
import com.study.exceptions.MethodNotAllowedException;
import com.study.exceptions.NotFoundException;
import com.study.models.Response;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.study.enums.HttpStatusCode.BAD_REQUEST;
import static com.study.enums.HttpStatusCode.INTERNAL_SERVER_ERROR;
import static com.study.enums.HttpStatusCode.METHOD_NOT_ALLOWED;
import static com.study.enums.HttpStatusCode.NOT_FOUND;
import static com.study.enums.HttpStatusCode.OK;
import static com.study.enums.HttpStatusCode.getMessage;

public class ResponseWriter {
    private static final String HTTP_PROTOCOL_TEMPLATE = "HTTP/1.1 %s";
    private static final String HEADER_TEMPLATE = "%s: %s";
    private static final Map<Class<? extends HttpException>, HttpStatusCode> ERROR_CODES =
            Map.of(BadRequestException.class, BAD_REQUEST,
                    NotFoundException.class, NOT_FOUND,
                    MethodNotAllowedException.class,METHOD_NOT_ALLOWED,
                    InternalServerErrorException.class, INTERNAL_SERVER_ERROR);

    public static void writeResponse(Response response, OutputStream outputStream) throws IOException {
        outputStream = new BufferedOutputStream(outputStream);
        outputStream.write(buildHttpMessage(response.getHeaders()).getBytes(StandardCharsets.UTF_8));
        outputStream.write(response.getBody());
        outputStream.flush();
    }

    public static void writeError(HttpException e, OutputStream outputStream) throws IOException {
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
