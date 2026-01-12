package edu.university.go.board;

import java.util.HashSet;
import java.util.Set;

public class Chain {
    private final Color color;
    private final Set<Point> stones = new HashSet<>();

    public Chain(Color color) {
        this.color = color;
    }

    public void add(Point p) {
        stones.add(p);
    }

    public Set<Point> getStones() {
        return stones;
    }

    public Color getColor() {
        return color;
    }
}
