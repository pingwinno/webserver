package com.study.readers;

import com.study.exceptions.ResourceNotFoundException;
import com.study.models.Request;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileResourceReaderTest {
    private static final String filePath = FileResourceReaderTest.class.getClassLoader().getResource("webapp/cat.gif").getPath();
    private static final String resourceFolder = filePath.replace("/webapp/cat.gif", "");
    private static final Map<String, String> EXPECTED_HEADERS = Map.of("Content-Type", "image/gif", "Content-Length", "129843");
    private final byte[] expectedFile = FileResourceReaderTest.class.getClassLoader().getResourceAsStream("webapp/cat.gif").readAllBytes();
    ResourceReader reader = new FileResourceReader(resourceFolder);

    FileResourceReaderTest() throws IOException {
    }

    @Test
    void should_returnFileWithHeaders_when_passRequestObjectWithFilePath() {
        var request = new Request();
        request.setPath("/webapp/cat.gif");

        var response = reader.getResponseBody(request);
        assertArrayEquals(expectedFile, response.getBody());
        assertEquals(EXPECTED_HEADERS, response.getHeaders());
    }

    @Test
    void should_throwResourceNotFoundException_when_passRequestObjectWithWrongFilePath() {
        var request = new Request();
        request.setPath("/webapp/somenonexistingfile.txt");
        assertThrows(ResourceNotFoundException.class, () -> reader.getResponseBody(request));
    }
    @Test
    void should_throwResourceNotFoundException_when_passRequestObjectWithNullFilePath() {
        var request = new Request();
        assertThrows(ResourceNotFoundException.class, () -> reader.getResponseBody(request));
    }
}