package com.study.readers;

import com.study.exceptions.ResourceNotFoundException;

import java.io.IOException;
import java.util.regex.Pattern;

public class JavaResourceReader implements ResourceReader {


    private String resourceFolder;

    public JavaResourceReader(String resourceFolder) {
        this.resourceFolder = resourceFolder;
    }

    @Override
    public byte[] getResponseBody(String requestMessage) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        var path = getResourcePath(requestMessage);
        try (var is = classloader.getResourceAsStream(String.join("/", resourceFolder, path))) {
            if (is != null) {
                return is.readAllBytes();
            }
            throw new ResourceNotFoundException("File not found. Path: " + path);
        } catch (IOException e) {
            throw new ResourceNotFoundException("Can't read file");
        }
    }



}
