package edu.university.go.game;

import edu.university.go.board.Board;

import java.util.Set;

class InProgress implements GameState {

    private final Set<String> players;

    InProgress(Set<String> players) {
        this.players = players;
    }

    @Override
    public void addPlayer(Game game, String playerId) {
        throw new IllegalStateException("Game already started");
    }

    @Override
    public void makeMove(Game game, Move move) {

        if (!players.contains(move.playerId())) {
            throw new IllegalArgumentException("Unknown player");
        }

        if (move.color() != game.getCurrentTurn()) {
            throw new IllegalStateException("Not your turn");
        }

        Board board = game.getBoard();
        boolean placed = board.placeStone(move.color(), move.x(), move.y());
        if (!placed) {
            // placement failed (out of bounds, occupied, or suicide) -> reject move
            throw new IllegalStateException("Invalid move");
        }

        game.notifyObservers(GameEvent.MOVE_PLAYED);
        game.switchTurn();
    }
}
