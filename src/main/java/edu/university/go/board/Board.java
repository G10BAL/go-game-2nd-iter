package edu.university.go.board;

import java.util.*;

public class Board {

    private final int size;
    private final Color[][] grid;

    public Board(int size) {
        this.size = size;
        this.grid = new Color[size][size];
        for (int i = 0; i < size; i++)
            Arrays.fill(grid[i], Color.EMPTY);
    }

    public int getSize() {
        return size;
    }

    public Color get(int x, int y) {
        return grid[x][y];
    }

    // Check if the given coordinates are inside the board
    public boolean isInside(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    // Get neighboring points of a given point
    public List<Point> neighbors(Point p) {
        List<Point> result = new ArrayList<>();
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        for (int[] d : dirs) {
            int nx = p.x + d[0];
            int ny = p.y + d[1];
            if (isInside(nx, ny)) {
                result.add(new Point(nx, ny));
            }
        }
        return result;
    }

    // Get the chain of a stone
    public Chain getChain(Point start) {
        Color color = get(start.x, start.y);
        Chain chain = new Chain(color);
        Set<Point> visited = new HashSet<>();

        dfs(start, color, visited, chain);
        return chain;
    }

    // Search to find connected stones
    private void dfs(Point p, Color color, Set<Point> visited, Chain chain) {
        if (visited.contains(p)) return;
        visited.add(p);
        chain.add(p);

        for (Point n : neighbors(p)) {
            if (get(n.x, n.y) == color) {
                dfs(n, color, visited, chain);
            }
        }
    }

    // Count empty spaces for a chain
    public int countLiberties(Chain chain) {
        Set<Point> liberties = new HashSet<>();

        for (Point p : chain.getStones()) {
            for (Point n : neighbors(p)) {
                if (get(n.x, n.y) == Color.EMPTY) {
                    liberties.add(n);
                }
            }
        }
        return liberties.size();
    }

    public boolean placeStone(Color color, int x, int y) {
        if (!isInside(x, y)) return false;
        if (grid[x][y] != Color.EMPTY) return false;

        grid[x][y] = color;

        // Check enemy chains
        for (Point n : neighbors(new Point(x, y))) {
            if (get(n.x, n.y) == color.opposite()) {
                Chain enemy = getChain(n);
                if (countLiberties(enemy) == 0) {
                    removeChain(enemy);
                }
            }
        }

        // Check the suicide rule
        Chain own = getChain(new Point(x, y));
        if (countLiberties(own) == 0) {
            grid[x][y] = Color.EMPTY;
            return false;
        }

        return true;
    }

    // Remove a chain 
    private void removeChain(Chain chain) {
        for (Point p : chain.getStones()) {
            grid[p.x][p.y] = Color.EMPTY;
        }
    }
}