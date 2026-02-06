package go;

import java.util.ArrayList;

import static go.GoBoard.BOARD_SIZE;

public class NextMoves {

    public static ArrayList<GoBoard> getBestMoves(GoBoard board, int maxNumberOfMoves) {
        ArrayList<GoBoard> allMoves = getMoves(board);

        if (allMoves.size() > maxNumberOfMoves) {
            ArrayList<GoBoard> bestMoves = new ArrayList<>();
            for (GoBoard move : allMoves) {
                int moveScore = BoardEvaluator.evaluate(move, move.getPlayer());
                move.setCurrentScore(moveScore);
            }

            allMoves.sort(null);

            for (int i = 0; i < maxNumberOfMoves; i++) {
                bestMoves.add(allMoves.get(i));
            }
            return bestMoves;
        } else {
            return allMoves;
        }
    }

    public static ArrayList<GoBoard> getMoves(GoBoard newBoard) {
        ArrayList<GoBoard> movesList;
        GoBoard temp;
        MoveChecker checker = new MoveChecker();
        temp = newBoard.copyBoard();
        movesList = new ArrayList<>();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                if (checker.play(temp, row, column)) {
                    movesList.add(temp);
                    temp = newBoard.copyBoard();
                }
            }
        }
        // Add pass as a possible move
        temp.setPass(temp.getPass() + 1);
        temp.switchPlayer();
        movesList.add(temp);
        return movesList;
    }
}
