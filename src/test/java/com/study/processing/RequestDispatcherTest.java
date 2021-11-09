package com.study.processing;

import com.study.exceptions.NotFoundException;
import com.study.handlers.Handler;
import com.study.handlers.StaticContentHandler;
import com.study.models.Request;
import com.study.readers.FileResourceReader;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestDispatcherTest {

    private static final Handler EXPECTED_HANDLER = new StaticContentHandler(new FileResourceReader(""));
    private static final RequestDispatcher dispatcher =
            new RequestDispatcher(new LinkedHashMap<>(Map.of(Pattern.compile("/path"), EXPECTED_HANDLER)));

    @Test
    void should_returnStaticContentHandler_whenPassValidPath() {
        var request = new Request();
        request.setPath("/path");
        var actualHandler = dispatcher.getHandler(request);
        assertEquals(EXPECTED_HANDLER, actualHandler);
    }

    @Test
    void should_returnStaticContentHandler_whenPassValidPathWithWildcard() {
        RequestDispatcher dispatcher =
                new RequestDispatcher(new LinkedHashMap<>(Map.of(Pattern.compile("/path/*."), EXPECTED_HANDLER)));
        var request = new Request();
        request.setPath("/path/morepath/evenmorepath");
        var actualHandler = dispatcher.getHandler(request);
        assertEquals(EXPECTED_HANDLER, actualHandler);
    }

    @Test
    void should_throwNotFoundException_whenPassInvalidPath() {
        var request = new Request();
        request.setPath("/someOtherPath");
        assertThrows(NotFoundException.class, () -> dispatcher.getHandler(request));
    }

}