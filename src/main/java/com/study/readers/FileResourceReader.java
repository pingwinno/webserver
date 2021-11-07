package com.study.readers;

import com.study.exceptions.InternalServerErrorException;
import com.study.exceptions.ResourceNotFoundException;
import com.study.models.Request;
import com.study.models.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;
import java.util.LinkedHashMap;

@Slf4j
public class FileResourceReader implements ResourceReader {
    private final static String CONTENT_TYPE = "Content-Type";
    private final static String CONTENT_LENGTH = "Content-Length";
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private final String resourceFolder;

    public FileResourceReader(String resourceFolder) {
        this.resourceFolder = resourceFolder;
    }

    @Override
    public Response getResponseBody(Request request) {
        try {
            var file = new File(resourceFolder + request.getPath());
            if (!file.exists()) {
                log.warn("File with path: {} not found", file.getAbsolutePath());
                throw new ResourceNotFoundException("File with path: " + file.getAbsolutePath() + " not found");
            }
            var mimeType = URLConnection.guessContentTypeFromName(file.getName());
            var contentLength = String.valueOf(file.length());
            var headers = new LinkedHashMap<String, String>();
            headers.put(CONTENT_TYPE, mimeType != null ? mimeType : DEFAULT_CONTENT_TYPE);
            headers.put(CONTENT_LENGTH, contentLength);
            return Response.builder().body(new FileInputStream(file).readAllBytes()).headers(headers).build();

        } catch (FileNotFoundException e) {
            log.warn("File not found with path: {}", request.getPath());
            throw new ResourceNotFoundException("File not found ", e);
        } catch (IOException e) {
            log.error("Can't read file with path: {}", request.getPath());
            throw new InternalServerErrorException("Can't read file", e);
        }
    }
}
