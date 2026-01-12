package edu.university.go.validators;

import edu.university.go.board.Board;
import edu.university.go.board.Color;

public class KoRule {
    
    private Board previousBoard;
    
    public boolean isKoViolation(Board currentBoard, int x, int y, Color color) {
        if (previousBoard == null) {
            return false;
        }
        
        Board simulated = currentBoard.clone();
        boolean placed = simulated.placeStone(color, x, y);
        
        if (!placed) {
            return false;
        }
        
        return simulated.equals(previousBoard);
    }
    
    public void updateState(Board board) {
        this.previousBoard = board.clone();
    }
    
    public void reset() {
        this.previousBoard = null;
    }
}