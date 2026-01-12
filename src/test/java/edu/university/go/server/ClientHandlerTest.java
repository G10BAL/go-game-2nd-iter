package edu.university.go.server;

import edu.university.go.game.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientHandlerTest {

    @Test
    void invalidCommandProducesError() {
        FakeSession session = new FakeSession();
        ClientHandler handler = new ClientHandler(null, session);

        handler.handleCommand("INVALID");

        assertTrue(session.errorSent);
    }

    static class FakeSession extends GameSession {

        boolean errorSent = false;

        FakeSession() {
            super();
        }

        @Override
        void handleMove(Move move) {
            errorSent = true;
        }
    }
}
