package com.golifeanddeath.engine;

import com.golifeanddeath.model.GoBoard;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveCheckerTest {

    private MoveChecker checker;
    private GoBoard board;

    @Before
    public void setUp() {
        checker = new MoveChecker();
        board = new GoBoard();
        // Set life group to corner (0,0) so it doesn't interfere with most tests.
        board.addStone('x', 0, 0);
        board.setLifeGroup(0, 0);
    }

    // --- Basic valid/invalid moves ---

    @Test
    public void validMoveOnEmptyIntersection() {
        assertTrue(checker.play(board, 5, 5));
    }

    @Test
    public void moveOnOccupiedSpaceIsRejected() {
        board.addStone('o', 5, 5);
        assertFalse(checker.play(board, 5, 5));
    }

    @Test
    public void moveOnLifeGroupLocationIsRejected() {
        // Life group is at (0,0) — clear the stone to isolate the life-group check
        board.getArray()[0][0] = '-';
        assertFalse(checker.play(board, 0, 0));
    }

    // --- Turn management ---

    @Test
    public void turnSwitchesAfterValidMove() {
        assertEquals('x', board.getTurn());
        checker.play(board, 5, 5);
        assertEquals('o', board.getTurn());
    }

    @Test
    public void moveRowAndColumnRecordedAfterValidMove() {
        checker.play(board, 7, 3);
        assertEquals(7, board.getMoveRow());
        assertEquals(3, board.getMoveColumn());
    }

    @Test
    public void passCounterResetAfterValidMove() {
        board.setPass(1);
        checker.play(board, 5, 5);
        assertEquals(0, board.getPass());
    }

    // --- Edge and corner moves ---

    @Test
    public void validMoveInCorner() {
        assertTrue(checker.play(board, 18, 18));
    }

    @Test
    public void validMoveOnEdge() {
        assertTrue(checker.play(board, 18, 9));
    }

    // --- Capture logic ---

    @Test
    public void singleStoneCaptured() {
        // White stone at (5,5) surrounded on 3 sides by black
        board.addStone('o', 5, 5);
        board.addStone('x', 5, 4); // west
        board.addStone('x', 5, 6); // east
        board.addStone('x', 6, 5); // north

        // Black plays south of white — completes surround, captures white
        assertTrue(checker.play(board, 4, 5));
        assertEquals('-', board.getArray()[5][5]);
    }

    @Test
    public void groupCaptured() {
        // Two white stones at (5,5) and (5,6), surrounded by black
        board.addStone('o', 5, 5);
        board.addStone('o', 5, 6);

        board.addStone('x', 5, 4);  // west of group
        board.addStone('x', 5, 7);  // east of group
        board.addStone('x', 6, 5);  // north of (5,5)
        board.addStone('x', 6, 6);  // north of (5,6)
        board.addStone('x', 4, 5);  // south of (5,5)

        // Black plays south of (5,6) to complete the surround
        assertTrue(checker.play(board, 4, 6));
        assertEquals('-', board.getArray()[5][5]);
        assertEquals('-', board.getArray()[5][6]);
    }

    @Test
    public void captureAtEdge() {
        // White stone on bottom edge at (0,5)
        // south=edge, west=black, north=black, east=black plays
        board.addStone('o', 0, 5);
        board.addStone('x', 0, 4); // west
        board.addStone('x', 1, 5); // north

        // Black plays east to complete surround
        assertTrue(checker.play(board, 0, 6));
        assertEquals('-', board.getArray()[0][5]);
    }

    // --- Suicide prevention ---

    @Test
    public void suicideMoveRejected() {
        // Black tries to play at (5,5) surrounded by white with liberties
        board.setTurn('x');
        board.addStone('o', 5, 4); // west
        board.addStone('o', 5, 6); // east
        board.addStone('o', 6, 5); // north
        board.addStone('o', 4, 5); // south

        assertFalse(checker.play(board, 5, 5));
        assertEquals('-', board.getArray()[5][5]);
    }

    @Test
    public void suicideAllowedIfItCaptures() {
        // White stone at (5,5) surrounded by black on 3 sides
        board.addStone('o', 5, 5);
        board.addStone('x', 6, 5); // north of white
        board.addStone('x', 4, 5); // south of white
        board.addStone('x', 5, 6); // east of white

        // Surround play spot (5,4) with white so it looks like suicide
        board.addStone('o', 6, 4); // north of play spot
        board.addStone('o', 4, 4); // south of play spot
        board.addStone('o', 5, 3); // west of play spot

        // Black plays at (5,4) — captures white at (5,5), gaining a liberty
        assertTrue(checker.play(board, 5, 4));
        assertEquals('-', board.getArray()[5][5]);
        assertEquals('x', board.getArray()[5][4]);
    }

    // --- Ko rule ---

    @Test
    public void koMoveRejected() {
        // Classic ko shape
        board.addStone('x', 6, 5);
        board.addStone('x', 4, 5);
        board.addStone('x', 5, 4);

        board.addStone('o', 6, 6);
        board.addStone('o', 4, 6);
        board.addStone('o', 5, 7);
        board.addStone('o', 5, 5); // white stone to be captured

        // Record the initial position in history (as GoGUI does on load)
        board.getHistory().addGame(board);

        // Black captures at (5,6)
        board.setTurn('x');
        assertTrue(checker.play(board, 5, 6));
        assertEquals('-', board.getArray()[5][5]);
        assertEquals('x', board.getArray()[5][6]);

        // White tries to recapture at (5,5) — ko, should be rejected
        assertEquals('o', board.getTurn());
        assertFalse(checker.play(board, 5, 5));
    }

    // --- Board state preserved on invalid move ---

    @Test
    public void boardUnchangedAfterInvalidMove() {
        board.addStone('o', 5, 5);
        char[][] before = new char[GoBoard.BOARD_SIZE][GoBoard.BOARD_SIZE];
        char[][] arr = board.getArray();
        for (int i = 0; i < GoBoard.BOARD_SIZE; i++) {
            System.arraycopy(arr[i], 0, before[i], 0, GoBoard.BOARD_SIZE);
        }

        assertFalse(checker.play(board, 5, 5));

        for (int i = 0; i < GoBoard.BOARD_SIZE; i++) {
            assertArrayEquals("Row " + i + " changed after invalid move",
                    before[i], board.getArray()[i]);
        }
    }

    // --- History tracking ---

    @Test
    public void validMoveIsAddedToHistory() {
        checker.play(board, 5, 5);
        int stoneCount = board.getStoneCount();
        assertTrue(board.getHistory().getGames(stoneCount).size() > 0);
    }
}
