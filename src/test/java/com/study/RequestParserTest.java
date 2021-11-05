package com.study;

import static org.junit.jupiter.api.Assertions.*;

class RequestParserTest {
    private static final String GET_REQUEST = "GET /home.html HTTP/1.1\n" +
            "Host: developer.mozilla.org\n" +
            "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:50.0) Gecko/20100101 Firefox/50.0\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
            "Accept-Language: en-US,en;q=0.5\n" +
            "Accept-Encoding: gzip, deflate, br\n" +
            "Referer: https://developer.mozilla.org/testpage.html\n" +
            "Connection: keep-alive\n" +
            "Upgrade-Insecure-Requests: 1\n" +
            "If-Modified-Since: Mon, 18 Jul 2016 02:36:04 GMT\n" +
            "If-None-Match: \"c561c68d0ba92bbeb8b0fff2a9199f722e3a621a\"\n" +
            "Cache-Control: max-age=0";

}