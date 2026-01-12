package edu.university.go.validators;

import edu.university.go.board.Board;
import edu.university.go.board.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class KoRuleTest {

    private KoRule validator;
    private Board board;

    @BeforeEach
    void setUp() {
        validator = new KoRule();
        board = new Board(9);
    }

    @Test
    @DisplayName("First move cannot be Ko violation")
    void testFirstMoveCannotBeKoViolation() {
        boolean isKo = validator.isKoViolation(board, 4, 4, Color.BLACK);
        assertFalse(isKo, "First move cannot be Ko violation");
    }

    @Test
    @DisplayName("Normal moves do not violate Ko")
    void testNormalMovesDoNotViolateKo() {
        // First turn
        board.placeStone(Color.BLACK, 4, 4);
        validator.updateState(board);

        // Second turn in a different place
        boolean isKo = validator.isKoViolation(board, 5, 5, Color.WHITE);
        assertFalse(isKo, "Normal move does not violate Ko");
    }

    @Test
    @DisplayName("Simple Ko scenario - capture and return")
    void testSimpleKoScenario() {
        // Setup position for Ko
        board.placeStone(Color.BLACK, 1, 0);
        board.placeStone(Color.WHITE, 1, 1);
        board.placeStone(Color.WHITE, 1, 3);
        board.placeStone(Color.BLACK, 0, 1);
        board.placeStone(Color.WHITE, 0, 2);
        board.placeStone(Color.BLACK, 2, 1);
        board.placeStone(Color.WHITE, 2, 2);

        // Save state
        validator.updateState(board);

        // Black captures white at (1,1)
        board.placeStone(Color.BLACK, 1, 2);

        // Now white tries to recapture immediately
        boolean isKo = validator.isKoViolation(board, 1, 1, Color.WHITE);
        assertTrue(isKo, "This should be a Ko violation - cannot return immediately");
    }

}