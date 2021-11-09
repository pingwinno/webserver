package com.study.processing;

import com.study.exceptions.NotFoundException;
import com.study.handlers.Handler;
import com.study.models.Request;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RequestDispatcher {
    private final LinkedHashMap<Pattern, Handler> handlers;

    /**
     * Constructor for ordered handlers
     *
     * @param handlers - Linked map of request handlers.
     *                 A handler priority is defined by handlers order in the map from first to last.
     */
    public RequestDispatcher(LinkedHashMap<Pattern, Handler> handlers) {
        this.handlers = handlers;
    }

    public Handler getHandler(Request request) {
        return handlers.entrySet()
                .stream()
                .filter(entry -> entry.getKey().matcher(request.getPath()).find())
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
