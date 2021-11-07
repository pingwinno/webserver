package com.study;

import com.study.exceptions.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
public class RequestParser {

    private static final Pattern METHOD_AND_PATH = Pattern.compile("(\\p{Upper}+) (/.*) HTTP/1.1.*");
    private static final Pattern HEADER = Pattern.compile("^(.+): (.+)$");


    public Request parse(InputStream inputStream) {
        var request = new Request();
        try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            List<String> requestMessage = new LinkedList<>();
            var line = "";
            while (Objects.nonNull(line = reader.readLine()) && !line.isEmpty()) {
                requestMessage.add(line);
            }
            injectPathAndMethod(requestMessage, request);
            injectHeaders(requestMessage, request);

        } catch (IOException e) {
            throw new InternalServerErrorException("Error during message handling", e);
        }
        return request;
    }

    private void injectPathAndMethod(List<String> lines, Request request) {
        var methodAndHeader = lines.remove(0);
        var matcher = METHOD_AND_PATH.matcher(methodAndHeader);
        if (matcher.find()) {
            var method = HttpMethod.getByName(matcher.group(1));
            var path = matcher.group(2);
            if (Objects.nonNull(method) && Objects.nonNull(path)) {
                request.setPath(path);
                request.setHttpMethod(method);
                return;
            }
        }
        log.error("Can't parse HTTP method and/or path from: {}", methodAndHeader);
        throw new InternalServerErrorException("Can't parse HTTP method and/or path");
    }

    private void injectHeaders(List<String> lines, Request request) {
        if (lines.isEmpty()) {
            return;
        }
        var headers = new HashMap<String, String>();
        for (String line : lines) {
            var matcher = HEADER.matcher(line);
            if (matcher.find()) {
                headers.put(matcher.group(1), matcher.group(2));
            }
        }
        request.setHeaders(headers);
    }
}
