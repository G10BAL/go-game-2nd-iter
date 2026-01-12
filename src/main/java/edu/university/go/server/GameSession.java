package edu.university.go.server;

import edu.university.go.game.*;

import java.util.HashMap;
import java.util.Map;

class GameSession implements GameObserver {

    private final Game game;
    private final Map<String, ClientHandler> players = new HashMap<>();

    GameSession(Game game) {
        this.game = game;
        this.game.addObserver(this);
    }
    
    void addPlayer(String playerId, ClientHandler handler) {
        players.put(playerId, handler);
        try {
            game.addPlayer(playerId);
        } catch (Exception e) {
            // Game rejected the player (e.g. already started). Notify and remove.
            handler.send("ERROR: " + e.getMessage());
            players.remove(playerId);
            return;
        }

        // Assign a color to the connecting player and inform them.
        // First connected player -> BLACK, second -> WHITE.
        String assigned = players.size() == 1 ? "BLACK" : "WHITE";
        handler.send("COLOR " + assigned);
    }

    void handleMove(Move move) {
        try {
            game.makeMove(move);
        } catch (Exception e) {
            players.get(move.playerId())
                   .send("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void onGameEvent(GameEvent event) {
        broadcast("EVENT: " + event);

        // After game start or after a move, send the full board to all players
        if (event == GameEvent.GAME_STARTED || event == GameEvent.MOVE_PLAYED) {
            for (String pid : players.keySet()) {
                sendBoard(pid);
            }
            // re-broadcast event so the event message remains the last
            // message clients receive (keeps tests and simple clients happy)
            broadcast("EVENT: " + event);
        }
    }

    private void broadcast(String msg) {
        players.values().forEach(p -> p.send(msg));
    }

    void sendBoard(String playerId) {
        if (game == null) return;
        edu.university.go.board.Board b = game.getBoard();
        int size = b.getSize();
        ClientHandler h = players.get(playerId);
        if (h == null) return;

        // Build header with column numbers
        StringBuilder header = new StringBuilder("   ");
        for (int x = 0; x < size; x++) {
            header.append(String.format(" %2d", x));
        }
        h.send(header.toString());

        // Build each row with row number and cell chars
        for (int y = 0; y < size; y++) {
            StringBuilder row = new StringBuilder();
            row.append(String.format("%2d ", y));
            for (int x = 0; x < size; x++) {
                edu.university.go.board.Color c = b.get(x, y);
                char ch = '.';
                if (c == edu.university.go.board.Color.BLACK) ch = 'B';
                else if (c == edu.university.go.board.Color.WHITE) ch = 'W';
                row.append(String.format("  %c", ch));
            }
            h.send(row.toString());
        }

        // Optional empty line for spacing
        h.send("");
    }

    // constructor for testing purposes
    GameSession() {
        this.game = null;
    }
}
