package edu.university.go.javafx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import edu.university.go.board.Board;
import edu.university.go.board.Point;
import edu.university.go.game.EnhancedGameController;
import edu.university.go.game.GameEvent;
import edu.university.go.game.GameObserver;

public class GameBoardCanvas extends Canvas implements GameObserver {
    
    // Fixed canvas size
    private static final int CANVAS_SIZE = 600;
    private static final int MARGIN = 40;
    
    private final EnhancedGameController controller;
    private final Board board;
    private final double cellSize;
    
    public GameBoardCanvas(EnhancedGameController controller) {
        super(CANVAS_SIZE, CANVAS_SIZE);
        
        this.controller = controller;
        this.board = controller.getBoard();
        
        // Calculate cell size based on board size to fit in canvas
        // Available space = CANVAS_SIZE - 2 * MARGIN
        // Cell size = available space / (board size - 1)
        this.cellSize = (CANVAS_SIZE - 2 * MARGIN) / (double) (board.getSize() - 1);
        
        controller.addObserver(this);
        
        setOnMouseClicked(this::handleClick);
        draw();
    }
    
    private void handleClick(MouseEvent event) {
        Point p = pixelToPoint(event.getX(), event.getY());
        if (p != null) {
            if (controller.makeMove(p.x, p.y)) {
                draw();
            }
        }
    }
    
    private void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        
        // Background
        gc.setFill(Color.rgb(165, 119, 21, 1));
        gc.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw grid
        gc.setStroke(Color.BLACK);
        for (int i = 0; i < board.getSize(); i++) {
            double pos = MARGIN + i * cellSize;
            gc.strokeLine(pos, MARGIN, pos, MARGIN + (board.getSize() - 1) * cellSize);
            gc.strokeLine(MARGIN, pos, MARGIN + (board.getSize() - 1) * cellSize, pos);
        }
        
        // Stones
        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                edu.university.go.board.Color color = board.get(x, y);
                if (color != edu.university.go.board.Color.EMPTY) {
                    drawStone(gc, x, y, color);
                }
            }
        }
    }
    
    private void drawStone(GraphicsContext gc, int x, int y, edu.university.go.board.Color color) {
        double cx = MARGIN + y * cellSize;
        double cy = MARGIN + x * cellSize;
        double radius = cellSize * 0.45;
        
        if (color == edu.university.go.board.Color.BLACK) {
            gc.setFill(Color.BLACK);
        } else {
            gc.setFill(Color.WHITE);
        }
        
        gc.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);
        
        if (color == edu.university.go.board.Color.WHITE) {
            gc.setStroke(Color.BLACK);
            gc.strokeOval(cx - radius, cy - radius, radius * 2, radius * 2);
        }
    }
    
    private Point pixelToPoint(double x, double y) {
        int px = (int) Math.round((x - MARGIN) / cellSize);
        int py = (int) Math.round((y - MARGIN) / cellSize);
        
        if (px >= 0 && px < board.getSize() && py >= 0 && py < board.getSize()) {
            return new Point(py, px);
        }
        return null;
    }
    
    @Override
    public void onGameEvent(GameEvent event) {
        if (event == GameEvent.MOVE_PLAYED) {
            draw();
        }
    }
}