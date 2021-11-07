package com.study;

import com.study.exceptions.InternalServerErrorException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestParserTest {

    private static final String FULL_GET_REQUEST = """
            GET /home.html HTTP/1.1
            Host: developer.mozilla.org
            User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:50.0) Gecko/20100101 Firefox/50.0
            Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
            Accept-Language: en-US,en;q=0.5
            Accept-Encoding: gzip, deflate, br
            Referer: https://developer.mozilla.org/testpage.html
            Connection: keep-alive
            Upgrade-Insecure-Requests: 1
            If-Modified-Since: Mon, 18 Jul 2016 02:36:04 GMT
            If-None-Match: "c561c68d0ba92bbeb8b0fff2a9199f722e3a621a"
            """;
    private static final Map<String, String> HEADERS = Map.of("Host", "developer.mozilla.org",
            "User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:50.0) Gecko/20100101 Firefox/50.0",
            "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            "Accept-Language", "en-US,en;q=0.5",
            "Accept-Encoding", "gzip, deflate, br",
            "Referer", "https://developer.mozilla.org/testpage.html",
            "Connection", "keep-alive",
            "Upgrade-Insecure-Requests", "1",
            "If-Modified-Since", "Mon, 18 Jul 2016 02:36:04 GMT",
            "If-None-Match", "\"c561c68d0ba92bbeb8b0fff2a9199f722e3a621a\"");

    private static final String EXPECTED_PATH = "/home.html";
    private static final HttpMethod EXPECTED_GET_METHOD = HttpMethod.GET;
    private RequestParser requestParser = new RequestParser();

    @Test
    void should_returnAllData_when_parseFullHttpGetMessage() {
        var expectedRequest = new Request();
        expectedRequest.setPath(EXPECTED_PATH);
        expectedRequest.setHttpMethod(EXPECTED_GET_METHOD);
        expectedRequest.setHeaders(HEADERS);
        var actualRequest = requestParser.parse(
                new ByteArrayInputStream(FULL_GET_REQUEST.getBytes(StandardCharsets.UTF_8)));
        assertEquals(expectedRequest, actualRequest);
    }

    @Test
    void should_returnPostMethodWithPath_whenParsePostMethodAndRootPath() {
        var expectedRequest = new Request();
        expectedRequest.setHttpMethod(HttpMethod.POST);
        expectedRequest.setPath("/");
        var actualRequest = requestParser.parse(
                new ByteArrayInputStream("POST / HTTP/1.1\n".getBytes(StandardCharsets.UTF_8)));
        assertEquals(expectedRequest, actualRequest);
    }

    @Test
    void should_returnPostMethodWithPath_whenParsePostMethodAndLongPath() {
        var expectedRequest = new Request();
        expectedRequest.setHttpMethod(HttpMethod.POST);
        expectedRequest.setPath("/home/user/folder/index.html");
        var actualRequest = requestParser.parse(
                new ByteArrayInputStream("POST /home/user/folder/index.html HTTP/1.1\n".getBytes(StandardCharsets.UTF_8)));
        assertEquals(expectedRequest, actualRequest);
    }

    @Test
    void should_throwInternalServerErrorException_whenPathIsEmpty() {
        assertThrows(InternalServerErrorException.class, () -> requestParser.parse(
                new ByteArrayInputStream("POST  HTTP/1.1\n".getBytes(StandardCharsets.UTF_8))));
    }

    @Test
    void should_throwInternalServerErrorException_whenMethodIsEmpty() {
        assertThrows(InternalServerErrorException.class, () -> requestParser.parse(
                new ByteArrayInputStream("/ HTTP/1.1\n".getBytes(StandardCharsets.UTF_8))));
    }

}