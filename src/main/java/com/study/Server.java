package com.study;

import com.study.handlers.StaticContentHandler;
import com.study.processing.RequestDispatcher;
import com.study.processing.RequestProcessor;
import com.study.readers.FileResourceReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

@Slf4j
public class Server {
    private final int port;
    private final String resourcePath;
    private final Executor pool = Executors.newCachedThreadPool();

    public Server(int port, String resourcePath) {
        this.port = port;
        this.resourcePath = resourcePath;
    }

    public void start() throws IOException {
        log.info("Server started on port:{}", port);
        try (var serverSocket = new ServerSocket(port)) {
            while (true) {
                var socket = serverSocket.accept();
                pool.execute(new RequestProcessor(socket, new RequestDispatcher(
                        new LinkedHashMap<>(Map.of(Pattern.compile("/*."),
                                new StaticContentHandler(
                                        new FileResourceReader(resourcePath)))))));
            }
        }
    }
}
