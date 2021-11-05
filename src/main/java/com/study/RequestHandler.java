package com.study;

import com.study.exceptions.InternalServerErrorException;
import com.study.exceptions.ResourceNotFoundException;
import com.study.readers.ResourceReader;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final Socket socket;
    private final ResourceReader resourceReader;

    public RequestHandler(Socket socket, ResourceReader resourceReader) {
        this.socket = socket;
        this.resourceReader = resourceReader;
    }

    @Override
    public void run() {
        handleRequest(socket);
    }

    private void handleRequest(Socket socket) {
        try (socket; var outputStream = new BufferedOutputStream(socket.getOutputStream());
             var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            try {
                var body = new StringBuilder();
                var line = "";
                while (!(line = reader.readLine()).isEmpty()) {
                    body.append(line);
                }
                var receivedMessage = body.toString();
                var data = resourceReader.getResponseBody(receivedMessage);
                outputStream.write(("HTTP/1.1 200 OK\n").getBytes());
                outputStream.write(("\n").getBytes());
                outputStream.write(data);
                outputStream.flush();
            } catch (ResourceNotFoundException e) {
                outputStream.write(("HTTP/1.1 404 Not Found).getBytes()\n").getBytes());
                outputStream.flush();
                throw new ResourceNotFoundException(e);
            } catch (InternalServerErrorException | IOException e) {
                outputStream.write(("HTTP/1.1 500 Internal Server Error").getBytes());
                outputStream.flush();
                throw new InternalServerErrorException(e);
            }
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
    }
}

