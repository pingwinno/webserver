package com.study.models;

import com.study.exceptions.InternalServerErrorException;
import com.study.exceptions.ResourceNotFoundException;
import com.study.processing.RequestParser;
import com.study.processing.ResponseWriter;
import com.study.readers.ResourceReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

@Slf4j
public class RequestHandler implements Runnable {
    private final Socket socket;
    private final ResourceReader resourceReader;

    public RequestHandler(Socket socket, ResourceReader resourceReader) {
        this.socket = socket;
        this.resourceReader = resourceReader;
    }

    @Override
    public void run() {
        try {
            handleRequest(socket);
        } catch (IOException e) {
            log.error("Error during sending response", e);
            throw new InternalServerErrorException("Can't write response", e);
        }
    }

    private void handleRequest(Socket socket) throws IOException {
        try {
            var request = RequestParser.parse(socket.getInputStream());
            ResponseWriter.writeResponse(request, resourceReader, socket.getOutputStream());
        } catch (ResourceNotFoundException e) {
            ResponseWriter.writeError(e, socket.getOutputStream());
            throw new ResourceNotFoundException(e);
        } catch (InternalServerErrorException e) {
            ResponseWriter.writeError(e, socket.getOutputStream());
            throw new InternalServerErrorException(e);
        } finally {
            socket.close();
        }
    }
}

