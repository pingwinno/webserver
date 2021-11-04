package com.study;

import com.study.exceptions.ResourceNotFoundException;

import java.io.IOException;
import java.util.regex.Pattern;

public class JavaResourceHandler implements ResourceHandler {
    private static final String INDEX_HTML = "index.html";
    private static final Pattern PATTERN = Pattern.compile("GET /(.+) HTTP/1.1.*", Pattern.CASE_INSENSITIVE);

    private String resourceFolder;

    public JavaResourceHandler(String resourceFolder) {
        this.resourceFolder = resourceFolder;
    }

    @Override
    public byte[] getResponseBody(String requestBody) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        var path = getResourcePath(requestBody);
        try (var is = classloader.getResourceAsStream(String.join("/", resourceFolder, path))) {
            if (is != null) {
                return is.readAllBytes();
            }
            throw new ResourceNotFoundException("File not found. Path: " + path);
        } catch (IOException e) {
            throw new ResourceNotFoundException("Can't read file");
        }
    }

    private String getResourcePath(String data) {

        var matcher = PATTERN.matcher(data);
        if (matcher.find()) {
            var path = matcher.group(1);
            return path != null ? path : INDEX_HTML;
        }
        return INDEX_HTML;
    }

}
