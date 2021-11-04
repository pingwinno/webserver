package com.study;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private final int port;
    private final String resourceFolder;

    public Server(int port, String resourceFolder) {
        this.port = port;
        this.resourceFolder = resourceFolder;
    }

    public void start() throws IOException {
        var serverSocket = new ServerSocket(port);
        while (true) {
            var socket = serverSocket.accept();
            new Thread(new RequestHandler(socket, new JavaResourceHandler(resourceFolder) {
            })).start();
        }
    }
}
