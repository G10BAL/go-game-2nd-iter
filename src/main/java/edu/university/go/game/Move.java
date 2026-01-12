package edu.university.go.game;

import edu.university.go.board.Color;

public record Move(
        Color color,
        int x,
        int y,
        String playerId
) {}
