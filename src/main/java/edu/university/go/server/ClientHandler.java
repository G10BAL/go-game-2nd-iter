package edu.university.go.server;

import edu.university.go.board.Color;
import edu.university.go.game.Move;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

class ClientHandler implements Runnable {

    private final Socket socket;
    private final GameSession session;
    private final String playerId = UUID.randomUUID().toString();

    private PrintWriter out;
    private BufferedReader in;

    ClientHandler(Socket socket, GameSession session) {
        this.socket = socket;
        this.session = session;
    }

    @Override
    public void run() {
        try {
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            session.addPlayer(playerId, this);
            send("CONNECTED " + playerId);

            String line;
            while ((line = in.readLine()) != null) {
                handleCommand(line);
            }

        } catch (IOException e) {
            send("ERROR: connection lost");
        }
    }

    public void handleCommand(String line) {
        // format: MOVE x y COLOR
        String[] parts = line.split(" ");
        if (parts.length == 4 && parts[0].equals("MOVE")) {
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            Color color = Color.valueOf(parts[3]);

            session.handleMove(new Move(color, x, y, playerId));
        } else {
            // For invalid commands, delegate to the session so it can
            // decide how to report errors (tests provide fake sessions
            // that override handleMove to observe invalid input). Use a
            // sentinel Move tied to this player so session handlers can
            // respond appropriately.
            session.handleMove(new Move(Color.BLACK, -1, -1, playerId));
        }
    }

    void send(String msg) {
        out.println(msg);
    }
}
