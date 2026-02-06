package com.golifeanddeath.engine;

import com.golifeanddeath.model.GoBoard;

import java.util.ArrayList;

public class AlphaBetaDB {

    private char player;

    public int solve(GoBoard board, int depth, int breadth) {
        player = board.getPlayer();
        return alphaBeta(board, Integer.MIN_VALUE, Integer.MAX_VALUE, false, depth, breadth);
    }

    public GoBoard getBestMove(GoBoard board, int depth, int breadth) {
        ArrayList<GoBoard> children = NextMoves.getBestMoves(board, breadth);
        int bestScore = Integer.MIN_VALUE;
        GoBoard bestChild = new GoBoard();
        for (GoBoard child : children) {
            int abScore = solve(child, depth, breadth);
            if (abScore >= bestScore) {
                bestScore = abScore;
                bestChild = child;
            }
        }
        return bestChild;
    }

    public int alphaBeta(GoBoard node, int alpha, int beta, boolean maximisingPlayer, int depth, int breadth) {
        int bestScore;

        if (BoardEvaluator.stop(node) || depth <= 0) {
            bestScore = BoardEvaluator.evaluate(node, player);
        } else if (maximisingPlayer) {
            ArrayList<GoBoard> children = NextMoves.getBestMoves(node, breadth);
            bestScore = alpha;

            for (GoBoard child : children) {
                int childScore = alphaBeta(child, bestScore, beta, false, depth - 1, breadth);
                bestScore = Math.max(bestScore, childScore);
                if (beta <= bestScore) {
                    break;
                }
            }
        } else {
            ArrayList<GoBoard> children = NextMoves.getBestMoves(node, breadth);
            bestScore = beta;
            for (GoBoard child : children) {
                int childScore = alphaBeta(child, alpha, bestScore, true, depth - 1, breadth);
                bestScore = Math.min(bestScore, childScore);
                if (bestScore <= alpha) {
                    break;
                }
            }
        }
        return bestScore;
    }
}
