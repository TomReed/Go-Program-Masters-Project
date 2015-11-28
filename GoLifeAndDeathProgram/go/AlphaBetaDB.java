package go;
import java.util.ArrayList;

/*
 * Methods:
 * .solve		-sets the player instance variable to player who made the move (i.e. NOT GoBoard.getTurn());
 * 				-takes a goBoard, calls alphaBeta and returns the score  
 * .bestMove    -takes a goBoard and returns the strongest move(GoBoard) that could be played next using .solve(..)
 * .alphaBeta   -returns alphaBeta on score from perspective of player set in player variable 
 * 
 */

public class AlphaBetaDB {

	private char player;  //holds the player from whose perspective moves are evaluated
	//public int depth;

	/* solve(..)
	 * Returns alphaBeta value on a board from the perspective of player who made the move (NOT .getTurn());
	 */ 
	public int solve(GoBoard board,int depth,int breadth){
		//this.depth=depth;
		player=board.getPlayer();
		return alphaBeta(board,Integer.MIN_VALUE,Integer.MAX_VALUE,false,depth,breadth);
	}

	/* getBestMove(..)
	 * Takes a GoBoard as an argument and calls solve on each child GoBoard
	 * Returns the child with the highest score from solve 
	 */
	public GoBoard getBestMove(GoBoard board,int depth,int breadth){
		//this.depth=depth;
		ArrayList<GoBoard> children = NextMoves.getBestMoves(board,breadth);
		int bestScore=Integer.MIN_VALUE;
		GoBoard bestChild = new GoBoard();
		for(GoBoard child:children){
			int abScore = solve(child,depth,breadth);
			if (abScore>=bestScore){
				bestScore=abScore;
				bestChild=child;
			}
		}
		return bestChild;
	}

	/* loosely based on a version of the alpha beta algorithm described at:
  		Minimax Search with Alpha-Beta Pruning [Algorithm Wiki] (no date) 
		Available at: http://will.thimbleby.net/algorithms/doku.php?id=minimax_search_with_alpha-beta_pruning
	 */	

	public int alphaBeta(GoBoard node, int alpha,int beta,boolean maximisingPlayer, int depth,int breadth){

		//NextMoves.getBestMoves returns Collection of strongest next moves of node(from perspective of 
		//player making them)
		//System.out.println(""+depth);

		int bestScore;
		//If stop conditions are met(life group captures, two passes, or depth limit reached
		//static board evaluator value is returned
		if(BoardEvaluator.stop(node)||(depth<=0)){
			bestScore = BoardEvaluator.evaluate(node,player);
		}
		else if(maximisingPlayer){
			ArrayList<GoBoard> children = NextMoves.getBestMoves(node,breadth);
			bestScore = alpha;

			for (GoBoard child : children){
				int childScore = alphaBeta(child,bestScore,beta,false,depth-1,breadth);
				//System.out.println("Max child value: "+childScore);
				bestScore = Math.max(bestScore, childScore);
				if (beta <= bestScore){
					break;
				}
			}
		}
		else{
			ArrayList<GoBoard> children = NextMoves.getBestMoves(node,breadth);
			bestScore = beta;
			for (GoBoard child : children){
				int childScore = alphaBeta(child,alpha,bestScore,true,depth-1,breadth);
				//System.out.println("Max child value: "+childScore);
				bestScore = Math.min(bestScore, childScore);
				if (bestScore <= alpha) {
					break;
				}
			}
		}
		//System.out.println("bestScore is "+ bestScore);
		return bestScore;
	}


}













