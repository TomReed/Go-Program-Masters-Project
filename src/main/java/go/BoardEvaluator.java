package go;

import static go.GoBoard.BOARD_SIZE;
import static go.GoBoard.MAX_INDEX;

public class BoardEvaluator {

    public static boolean stop(GoBoard board) {
        if (board.getPass() > 1) {
            return true;
        }
        return board.getlifeChar() != 'x' && board.getlifeChar() != 'o';
    }

    public static int evaluate(GoBoard board, char player) {
        char lifePlayer = board.getLifePlayer();

        if (board.getlifeChar() != 'x' && board.getlifeChar() != 'o') {
            if (lifePlayer != player) {
                return 100000;
            } else {
                return -1000000;
            }
        } else {
            return heuristicValue(board, player);
        }
    }

    public static int heuristicValue(GoBoard board, char player) {
        char enemy = (player == 'x') ? 'o' : 'x';
        char defender = board.getLifePlayer();
        char attacker = (defender == 'o') ? 'x' : 'o';

        int score = 0;
        char[][] goban = board.getArray();

        // 1. Check board for removal of liberties or hanes played by attacking side
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                if (goban[row][column] == defender) {
                    char north = (row < MAX_INDEX) ? goban[row + 1][column] : 'B';
                    char east = (column < MAX_INDEX) ? goban[row][column + 1] : 'B';
                    char south = (row > 0) ? goban[row - 1][column] : 'B';
                    char west = (column > 0) ? goban[row][column - 1] : 'B';

                    if (north == attacker) score += 30;
                    if (east == attacker) score += 30;
                    if (south == attacker) score += 30;
                    if (west == attacker) score += 30;

                    if (north == attacker) {
                        if (west == attacker) score += 50;
                        if (east == attacker) score += 50;
                    }
                    if (south == attacker) {
                        if (west == attacker) score += 50;
                        if (east == attacker) score += 50;
                    }
                }
            }
        }
        // 2. Check for attacker's empty triangles
        score += countEmptyTriangles(goban, attacker) * 75;

        // Scores so far from attacker's perspective - negate if player is defender
        if (player == defender) {
            score = score * -1;
        }

        // 3. Check board for mouths (good shape)
        score += countMouths(goban, player) * 100;
        score += countMouths(goban, enemy) * -100;

        return score;
    }

    public static int countEmptyTriangles(char[][] b, char player) {
        int count = 0;

        for (int row = 1; row < BOARD_SIZE; row++) {
            for (int column = 0; column < MAX_INDEX; column++) {
                if (b[row][column] == '-') {
                    if (b[row][column + 1] == player && b[row - 1][column] == player && b[row - 1][column + 1] == player) {
                        count++;
                    }
                } else if (b[row][column] == player) {
                    if (b[row][column + 1] == player) {
                        if ((b[row - 1][column] == player && b[row - 1][column + 1] == '-') ||
                                (b[row - 1][column] == '-' && b[row - 1][column + 1] == player)) {
                            count++;
                        }
                    } else if (b[row][column + 1] == '-') {
                        if (b[row - 1][column] == player && b[row - 1][column + 1] == player) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public static int countMouths(char[][] b, char player) {
        int count = 0;

        for (int row = 1; row < MAX_INDEX; row++) {
            for (int column = 1; column < MAX_INDEX; column++) {
                if (b[row][column] != player) {
                    if ((b[row + 1][column] == player) && (b[row - 1][column] != player)) {
                        if (b[row - 1][column - 1] == player && b[row - 1][column + 1] == player) {
                            char nw = b[row + 1][column - 1];
                            char ne = b[row + 1][column + 1];
                            char w = b[row][column - 1];
                            char e = b[row][column + 1];
                            if (nw == player && ne != player && e == player && w != player) {
                                count++;
                            } else if (nw != player && ne == player && e != player && w == player) {
                                count++;
                            }
                        }
                    } else if ((b[row - 1][column] == player) && (b[row + 1][column] != player)) {
                        if (b[row + 1][column - 1] == player && b[row + 1][column + 1] == player) {
                            char sw = b[row - 1][column - 1];
                            char se = b[row - 1][column + 1];
                            char w = b[row][column - 1];
                            char e = b[row][column + 1];
                            if (sw == player && se != player && e == player && w != player) {
                                count++;
                            } else if (sw != player && se == player && e != player && w == player) {
                                count++;
                            }
                        }
                    }
                }
            }
        }
        return count;
    }
}
