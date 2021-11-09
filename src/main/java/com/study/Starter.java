package com.study;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Starter {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            log.error("Wrong arguments. Usage %appname% <PORT> <RESOURCE_PATH>");
            throw new IllegalArgumentException("Wrong arguments. Usage %appname% <PORT> <RESOURCE_PATH>");
        }
        new Server(Integer.parseInt(args[0]), args[1]).start();
    }
}
