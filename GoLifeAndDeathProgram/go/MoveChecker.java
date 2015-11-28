package go;

import java.util.ArrayList;


public class MoveChecker {

	private GoBoard theBoard;
	private ArrayList<Integer> capturedStones;
	private char enemy;
	private char player;
	private char[][] board;
	private GameHistory history;
	boolean stonesToCapture = true;

	public MoveChecker(){}

	//if move is invalid returns false, else returns true and updates the board;

	public boolean play(GoBoard b,int row, int column) {
		theBoard = b;
		history = theBoard.getHistory();
		board = theBoard.getArray();
		player = theBoard.getTurn();
		// instantiate a list to holds location of all captured stones
		ArrayList<Integer> allCaptives = new ArrayList<Integer>(); 



		if (player=='x')
			enemy='o';
		else 
			enemy='x';


		//1. Return false if location is not empty.
		if ((board[row][column]!= '-')||(row==b.getLifeLocation()[0]&&column==b.getLifeLocation()[1])){ 
			return false;
		}
		//2. Add a stone to the board array
		board[row][column] = player;


		//3. Check N,S,E and W for captured groups and remove these stones.
		if (theBoard.north(row,column)==enemy){
			stonesToCapture=true;
			capturedStones = new ArrayList<Integer>();
			capture(row+1,column);
			if (stonesToCapture==true){
				allCaptives.addAll(capturedStones);
			}
			clearMarkers();
		}
		if (theBoard.south(row,column)==enemy){
			stonesToCapture=true;
			capturedStones= new ArrayList<Integer>();
			capture(row-1,column);
			if(stonesToCapture==true){
				allCaptives.addAll(capturedStones);
			}
			clearMarkers();
		}
		if (theBoard.east(row,column)==enemy){
			stonesToCapture=true;
			capturedStones=new ArrayList<Integer>();
			capture(row,column+1);
			if(stonesToCapture==true){
				allCaptives.addAll(capturedStones);
			}
			clearMarkers();
		}
		if (theBoard.west(row,column)==enemy){
			stonesToCapture=true;
			capturedStones=new ArrayList<Integer>();
			capture(row,column-1);
			if(stonesToCapture==true){
				allCaptives.addAll(capturedStones);
			}
			clearMarkers();
		}
		//remove captives from north, south, east and west from the board 
		//(to be returned later only if ko)
		theBoard.removeGroups(allCaptives);


		//4. Evaluate whether new stone now has liberties. If so, check ko
		//if ko then return any previously captured stones
		//clearMarkers();
		if (hasLiberties(row,column)){
			clearMarkers();

			//if in history then ko! -- undo changes to board and return false
			if (history.inHistory(theBoard)){ 

				//remove played stone as not legal
				board[row][column]='-'; 

				//replace captured stones
				theBoard.addGroups(enemy,allCaptives);

				return false;
			}
			//add board to history and return true as has liberties and board not in history
			else{
				theBoard.setPass(0); //a valid move was played so not pass
				theBoard.setMoveRow(row); //record location of played stone
				theBoard.setMoveColumn(column); //record location of played stone
				theBoard.switchPlayer(); //set to next player's turn
				history.addGame(theBoard);
				return true;  
			}

		}
		//return false as has no liberties
		else{
			clearMarkers();			// remove p and e markers								
			board[row][column]='-'; //remove played stone
			return false;  
		}
	}

	public boolean hasLiberties(int row, int column){
		//char[][] board = theBoard.getArray();
		char player=theBoard.getTurn();
		board[row][column]='p';	// mark the stone as processed player's stone
		if (theBoard.hasAdjacentSpace(row, column)){
			return true;
		}

		if (theBoard.north(row, column)==player){
			if (row<18)
				if (hasLiberties(row+1,column)) return true;
		}
		if (theBoard.south(row, column)==player){
			if (row>0)
				if (hasLiberties(row-1,column)) return true;
		}
		if (theBoard.west(row, column)==player){
			if (column>0)
				if(hasLiberties(row,column-1))return true;
		}
		if (theBoard.east(row, column)==player){
			if (column<18)
				if (hasLiberties(row,column+1))return true;
		}

		return false;
	}


	//returns coordinates of stones in groups with no liberties as a arrayList<integer>

	public ArrayList<Integer> capture(int row, int column){
		char[][] board = theBoard.getArray();

		//mark the stone as processed
		board[row][column]='e';

		//add the row and column numbers to the list of captured stones
		capturedStones.add(row);	
		capturedStones.add(column);

		//Stone has adjacent space? 
		//YES: there is no group to capture in this direction here so empty stone collection and return.
		if (theBoard.hasAdjacentSpace(row, column)){
			capturedStones = new ArrayList<Integer>();
			stonesToCapture=false;  //flag to not collect stones in this direction
			return capturedStones;
		}
		//NO: are there still unprocessed enemy stones to north, south, west or east? 
		// Yes: process them. No: return string of captured stones. 		
		if(theBoard.north(row,column)==enemy){
			capture(row+1,column);
		}
		if(theBoard.south(row,column)==enemy){
			capture(row-1,column);
		}
		if(theBoard.west(row,column)==enemy){
			capture(row,column-1);
		}
		if(theBoard.east(row,column)==enemy){
			capture(row,column+1);
		}
		return capturedStones; 
	}

	public void clearMarkers(){
		//char[][] board=theBoard.getArray();
		for (int i=0;i<19;i++){
			for (int j=0;j<19;j++){
				if (board[i][j]=='p')
					board[i][j]=player;
				else if(board[i][j]=='e')
					board[i][j]=enemy;
			}
		}
	}

}
