package go;

import java.util.ArrayList;

import static go.GoBoard.BOARD_SIZE;
import static go.GoBoard.MAX_STONES;

public class GameHistory {

    private ArrayList<ArrayList<char[][]>> history;

    public GameHistory() {
        history = new ArrayList<>(MAX_STONES);
        for (int i = 0; i < MAX_STONES; i++) {
            history.add(new ArrayList<>());
        }
    }

    public ArrayList<char[][]> getGames(int count) {
        return history.get(count);
    }

    public void addGame(GoBoard board) {
        int numStones = board.getStoneCount();
        ArrayList<char[][]> gameList = history.get(numStones);
        char[][] newBoard = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(board.getArray()[i], 0, newBoard[i], 0, BOARD_SIZE);
        }
        gameList.add(newBoard);
    }

    public boolean inHistory(GoBoard board) {
        int numStones = board.getStoneCount();
        for (char[][] g : history.get(numStones)) {
            boolean match = true;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (g[i][j] != board.getArray()[i][j]) {
                        match = false;
                    }
                }
            }
            if (match) return true;
        }
        return false;
    }

    public void setHistory(ArrayList<ArrayList<char[][]>> history) {
        this.history = history;
    }

    public GameHistory copyHistory() {
        GameHistory copyHist = new GameHistory();
        ArrayList<ArrayList<char[][]>> newArray = new ArrayList<>(MAX_STONES);
        for (int i = 0; i < MAX_STONES; i++) {
            ArrayList<char[][]> boardsList = new ArrayList<>();
            newArray.add(boardsList);
            for (char[][] b : history.get(i)) {
                char[][] copyOfBoard = new char[BOARD_SIZE][BOARD_SIZE];
                for (int j = 0; j < BOARD_SIZE; j++) {
                    System.arraycopy(b[j], 0, copyOfBoard[j], 0, BOARD_SIZE);
                }
                boardsList.add(copyOfBoard);
            }
        }
        copyHist.setHistory(newArray);
        return copyHist;
    }
}
