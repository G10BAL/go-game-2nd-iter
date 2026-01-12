package edu.university.go.server;

import edu.university.go.game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static Server instance;

    private final int port = 9999;
    private final GameSession session;

    private Server() {
        // Board size is hardcoded, I guess
        // I will change it in 2nd iteration
        Game game = GameFactory.createGame(19);
        session = new GameSession(game);
    }

    public static synchronized Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Go server started on port " + port);

        while (true) {
            Socket client = serverSocket.accept();
            new Thread(new ClientHandler(client, session)).start();
        }
    }
}
