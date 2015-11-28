package go;

import java.util.ArrayList;

public class NextMoves {


	/*returns a list of the best moves up to the number maxNumber
	 * 
	 */
	public static ArrayList<GoBoard> getBestMoves(GoBoard board,int maxNumberOfMoves){
		ArrayList<GoBoard> allMoves = getMoves(board);
		
		if (allMoves.size()>maxNumberOfMoves){
			ArrayList<GoBoard> bestMoves = new ArrayList<GoBoard>();
			for(GoBoard move: allMoves){
				int moveScore = BoardEvaluator.evaluate(move,move.getPlayer());
				move.setCurrentScore(moveScore);
			}

			allMoves.sort(null);
			
			for(int i=0;i<maxNumberOfMoves;i++){
				bestMoves.add(allMoves.get(i));;
			//	System.out.println("bestMoves score: "+ allMoves.get(i).getCurrentScore());
			}
			return bestMoves;  //limited list of moves, strongest first
		}
		else{
			return allMoves; //as sorted list of all moves, strongest first
		}
	}













	//take a GoBoard as an argument and return an arrayList of possible next moves
	public static ArrayList<GoBoard> getMoves(GoBoard newBoard){
		ArrayList<GoBoard> movesList;
		GoBoard temp;
		MoveChecker checker = new MoveChecker();
		//1: create a copy of newBoard temp 
		temp = newBoard.copyBoard();
		//2: make movesList a new ArrayList
		movesList = new ArrayList<GoBoard>();

		//2: for each position on the board:
		//2.1 try and play row column on temp
		//2.2 if valid: 
		// (i) add temp to movesList;
		// (ii)reset temp as a new copy of newBoard	

		for (int row=0;row<19;row++){
			for (int column=0;column<19;column++){
				if (checker.play(temp,row,column)){  //.play method will modify temp if valid (moveChecker returns true)
					movesList.add(temp);
					temp = newBoard.copyBoard();
				}
			}
		}
		//ADD PASS AS A POSSIBLE MOVE
		temp.setPass(temp.getPass()+1); //increment pass by 1 (MoveChecker class sets to zero if stone player)
		if (temp.getTurn()=='x'){
			temp.setTurn('o');
		}
		else{
			temp.setTurn('x');
		}


		movesList.add(temp);
		return movesList;
	}

}
