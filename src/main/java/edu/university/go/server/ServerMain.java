package edu.university.go.server;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) throws IOException {
        Server server = Server.getInstance();
        server.start();
    }
}
