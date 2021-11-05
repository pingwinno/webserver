package com.study;

import com.study.exceptions.InternalServerErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;


public class RequestParser {
    private static final String INDEX_HTML = "index.html";
    private static final Pattern METHOD_AND_PATH = Pattern.compile("(\\p{Upper}) /(.+) HTTP/1.1.*");


    private String getResourcePath(InputStream inputStream) {
        var body = new StringBuilder();
        try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {

            var line = "";
            while (!(line = reader.readLine()).isEmpty()) {
                body.append(line);
                body.append("\n");
            }
        } catch (IOException e) {
            throw new InternalServerErrorException("Error during message handling", e);
        }

        var matcher = METHOD_AND_PATH.matcher(body);
        if (matcher.find()) {
            var method = matcher.group(1);
            var path = matcher.group(2);
            return path != null ? path : INDEX_HTML;
        }
        return INDEX_HTML;
    }
}
