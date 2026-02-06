package com.golifeanddeath.engine;

import com.golifeanddeath.model.GameHistory;
import com.golifeanddeath.model.GoBoard;

import java.util.ArrayList;

import static com.golifeanddeath.model.GoBoard.BOARD_SIZE;
import static com.golifeanddeath.model.GoBoard.MAX_INDEX;

public class MoveChecker {

    private GoBoard theBoard;
    private ArrayList<Integer> capturedStones;
    private char enemy;
    private char player;
    private char[][] board;
    private GameHistory history;
    private boolean stonesToCapture = true;

    public MoveChecker() {}

    public boolean play(GoBoard b, int row, int column) {
        theBoard = b;
        history = theBoard.getHistory();
        board = theBoard.getArray();
        player = theBoard.getTurn();
        ArrayList<Integer> allCaptives = new ArrayList<>();

        if (player == 'x')
            enemy = 'o';
        else
            enemy = 'x';

        // 1. Return false if location is not empty.
        if (board[row][column] != '-' || (row == b.getLifeLocation()[0] && column == b.getLifeLocation()[1])) {
            return false;
        }
        // 2. Add a stone to the board array
        board[row][column] = player;

        // 3. Check N,S,E and W for captured groups and remove these stones.
        if (theBoard.north(row, column) == enemy) {
            stonesToCapture = true;
            capturedStones = new ArrayList<>();
            capture(row + 1, column);
            if (stonesToCapture) {
                allCaptives.addAll(capturedStones);
            }
            clearMarkers();
        }
        if (theBoard.south(row, column) == enemy) {
            stonesToCapture = true;
            capturedStones = new ArrayList<>();
            capture(row - 1, column);
            if (stonesToCapture) {
                allCaptives.addAll(capturedStones);
            }
            clearMarkers();
        }
        if (theBoard.east(row, column) == enemy) {
            stonesToCapture = true;
            capturedStones = new ArrayList<>();
            capture(row, column + 1);
            if (stonesToCapture) {
                allCaptives.addAll(capturedStones);
            }
            clearMarkers();
        }
        if (theBoard.west(row, column) == enemy) {
            stonesToCapture = true;
            capturedStones = new ArrayList<>();
            capture(row, column - 1);
            if (stonesToCapture) {
                allCaptives.addAll(capturedStones);
            }
            clearMarkers();
        }
        theBoard.removeGroups(allCaptives);

        // 4. Evaluate whether new stone now has liberties
        if (hasLiberties(row, column)) {
            clearMarkers();

            if (history.inHistory(theBoard)) {
                board[row][column] = '-';
                theBoard.addGroups(enemy, allCaptives);
                return false;
            } else {
                theBoard.setPass(0);
                theBoard.setMoveRow(row);
                theBoard.setMoveColumn(column);
                theBoard.switchPlayer();
                history.addGame(theBoard);
                return true;
            }
        } else {
            clearMarkers();
            board[row][column] = '-';
            return false;
        }
    }

    public boolean hasLiberties(int row, int column) {
        char player = theBoard.getTurn();
        board[row][column] = 'p';
        if (theBoard.hasAdjacentSpace(row, column)) {
            return true;
        }

        if (theBoard.north(row, column) == player && row < MAX_INDEX) {
            if (hasLiberties(row + 1, column)) return true;
        }
        if (theBoard.south(row, column) == player && row > 0) {
            if (hasLiberties(row - 1, column)) return true;
        }
        if (theBoard.west(row, column) == player && column > 0) {
            if (hasLiberties(row, column - 1)) return true;
        }
        if (theBoard.east(row, column) == player && column < MAX_INDEX) {
            if (hasLiberties(row, column + 1)) return true;
        }

        return false;
    }

    public ArrayList<Integer> capture(int row, int column) {
        char[][] board = theBoard.getArray();
        board[row][column] = 'e';

        capturedStones.add(row);
        capturedStones.add(column);

        if (theBoard.hasAdjacentSpace(row, column)) {
            capturedStones = new ArrayList<>();
            stonesToCapture = false;
            return capturedStones;
        }

        if (theBoard.north(row, column) == enemy) {
            capture(row + 1, column);
        }
        if (theBoard.south(row, column) == enemy) {
            capture(row - 1, column);
        }
        if (theBoard.west(row, column) == enemy) {
            capture(row, column - 1);
        }
        if (theBoard.east(row, column) == enemy) {
            capture(row, column + 1);
        }
        return capturedStones;
    }

    public void clearMarkers() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 'p')
                    board[i][j] = player;
                else if (board[i][j] == 'e')
                    board[i][j] = enemy;
            }
        }
    }
}
