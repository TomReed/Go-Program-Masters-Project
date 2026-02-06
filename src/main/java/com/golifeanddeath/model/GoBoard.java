package com.golifeanddeath.model;

import java.util.ArrayList;

public class GoBoard implements Comparable<GoBoard> {

    public static final int BOARD_SIZE = 19;
    public static final int MAX_INDEX = BOARD_SIZE - 1;
    public static final int MAX_STONES = BOARD_SIZE * BOARD_SIZE;

    private char[][] boardArray;
    private char turn = 'x';
    private GameHistory history;
    private int pass = 0;
    private int currentScore = 0;

    private int lifeRow = 0;
    private int lifeColumn = 0;
    private int moveRow;
    private int moveColumn;

    private char lifePlayer = 'o';
    private String description = "";

    public GoBoard() {
        boardArray = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                boardArray[i][j] = '-';
            }
        }
        history = new GameHistory();
    }

    public void addStone(char stone, int x, int y) {
        boardArray[x][y] = stone;
    }

    public char[][] getArray() {
        return boardArray;
    }

    public GameHistory getHistory() {
        return history;
    }

    public void setHistory(GameHistory history) {
        this.history = history;
    }

    public boolean hasAdjacentSpace(int row, int column) {
        if (column > 0) {
            char temp = boardArray[row][column - 1];
            if (temp == '-' || temp == '*') return true;
        }
        if (column < MAX_INDEX) {
            char temp = boardArray[row][column + 1];
            if (temp == '-' || temp == '*') return true;
        }
        if (row < MAX_INDEX) {
            char temp = boardArray[row + 1][column];
            if (temp == '-' || temp == '*') return true;
        }
        if (row > 0) {
            char temp = boardArray[row - 1][column];
            if (temp == '-' || temp == '*') return true;
        }
        return false;
    }

    public char east(int row, int column) {
        if (column < MAX_INDEX) {
            return boardArray[row][column + 1];
        }
        return 'e';
    }

    public char west(int row, int column) {
        if (column > 0) {
            return boardArray[row][column - 1];
        }
        return 'e';
    }

    public char north(int row, int column) {
        if (row < MAX_INDEX) {
            return boardArray[row + 1][column];
        }
        return 'e';
    }

    public char south(int row, int column) {
        if (row > 0) {
            return boardArray[row - 1][column];
        }
        return 'e';
    }

    public char getTurn() {
        return turn;
    }

    public void setTurn(char turn) {
        this.turn = turn;
    }

    public int getStoneCount() {
        int count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (boardArray[i][j] == 'x' || boardArray[i][j] == 'o') {
                    count++;
                }
            }
        }
        return count;
    }

    public void switchPlayer() {
        turn = (turn == 'x') ? 'o' : 'x';
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (int row = MAX_INDEX; row >= 0; row--) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                sb.append(boardArray[row][column]);
            }
            sb.append('\n');
        }
        sb.append(turn).append(',').append(lifeRow).append(',').append(lifeColumn);
        return sb.toString();
    }

    public boolean compareArray(char[][] newBoard) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (this.boardArray[i][j] != newBoard[i][j])
                    return false;
            }
        }
        return true;
    }

    public void removeGroups(ArrayList<Integer> stones) {
        for (int i = 0; i < stones.size() - 1; i = i + 2) {
            boardArray[stones.get(i)][stones.get(i + 1)] = '-';
        }
    }

    public void addGroups(char stone, ArrayList<Integer> groups) {
        for (int i = 0; i < groups.size() - 1; i = i + 2) {
            boardArray[groups.get(i)][groups.get(i + 1)] = stone;
        }
    }

    public GoBoard copyBoard() {
        GoBoard copy = new GoBoard();
        copy.turn = this.turn;
        copy.history = history.copyHistory();
        copy.pass = this.pass;
        copy.lifeColumn = this.lifeColumn;
        copy.lifeRow = this.lifeRow;
        copy.lifePlayer = this.lifePlayer;

        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(this.boardArray[i], 0, copy.boardArray[i], 0, BOARD_SIZE);
        }
        return copy;
    }

    public void setLifeGroup(int row, int column) {
        lifeRow = row;
        lifeColumn = column;
        lifePlayer = boardArray[row][column];
    }

    public char getlifeChar() {
        return boardArray[lifeRow][lifeColumn];
    }

    public int[] getLifeLocation() {
        return new int[]{lifeRow, lifeColumn};
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public char getLifePlayer() {
        return lifePlayer;
    }

    public void setLifePlayer(char lifePlayer) {
        this.lifePlayer = lifePlayer;
    }

    public int getMoveRow() {
        return moveRow;
    }

    public void setMoveRow(int moveRow) {
        this.moveRow = moveRow;
    }

    public int getMoveColumn() {
        return moveColumn;
    }

    public void setMoveColumn(int moveColumn) {
        this.moveColumn = moveColumn;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    @Override
    public int compareTo(GoBoard b) {
        return Integer.compare(b.currentScore, this.currentScore);
    }

    public char getPlayer() {
        return (turn == 'o') ? 'x' : 'o';
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
