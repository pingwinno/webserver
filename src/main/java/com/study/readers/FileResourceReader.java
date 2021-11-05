package com.study.readers;

public class FileResourceReader implements ResourceReader {
    @Override
    public byte[] getResponseBody(String requestBody) {
        return new byte[0];
    }
}
