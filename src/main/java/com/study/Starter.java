package com.study;

import java.io.IOException;

public class Starter {
    public static void main(String[] args) throws IOException {
        new Server(3000, "webapp").start();
    }
}
