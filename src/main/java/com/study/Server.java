package com.study;

import com.study.readers.JavaResourceReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;

@Slf4j
public class Server {
    private final int port;
    private final String resourcePath;

    public Server(int port, String resourceFolder) {
        this.port = port;
        this.resourcePath = resourceFolder;
    }

    public void start() throws IOException {
        log.info("Server started on port:{}", port);
        try (var serverSocket = new ServerSocket(port)) {
            while (true) {
                var socket = serverSocket.accept();
                new Thread(new RequestHandler(socket, new JavaResourceReader(resourcePath) {
                })).start();
            }
        }
    }
}
