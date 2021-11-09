package com.study.processing;

import com.study.exceptions.HttpException;
import com.study.exceptions.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

@Slf4j
public class RequestProcessor implements Runnable {
    private final Socket socket;
    private final RequestDispatcher requestDispatcher;

    public RequestProcessor(Socket socket, RequestDispatcher requestDispatcher) {
        this.socket = socket;
        this.requestDispatcher = requestDispatcher;
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
            var requestHandler = requestDispatcher.getHandler(request);
            var response = requestHandler.handleRequest(request);
            ResponseWriter.writeResponse(response, socket.getOutputStream());
        } catch (HttpException e) {
            ResponseWriter.writeError(e, socket.getOutputStream());
            throw new HttpException(e);
        } finally {
            socket.close();
        }
    }
}

