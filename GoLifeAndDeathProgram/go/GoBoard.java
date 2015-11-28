package go;

import java.util.ArrayList;

public class GoBoard implements Comparable<GoBoard>{

	private char[][] boardArray;
	private char turn = 'x'; 
	private GameHistory history;
	private int pass = 0;  //holds number or previous passes
	private int currentScore =0;

	//to hold location of a stone in group of interest in life or death problems
	private int lifeRow = 0;
	private int lifeColumn = 0;
	//to hold location of the last stone played 
	private int moveRow; 
	private int moveColumn;

	private char lifePlayer = 'o'; //indicates player (black or white) in the life group
	private String description=""; //can be set to hold a description of the problem
	
	public GoBoard(){
		boardArray = new char[19][19];
		for (int i=0;i<19;i++){
			for (int j=0;j<19;j++){
				boardArray[i][j]='-';
			}
		}
		//create a new GameHistory;
		history = new GameHistory();
	}

	public void addStone(char stone, int x, int y){
		boardArray [x][y]=stone;
	}

	public char[][] getArray(){
		return boardArray;

	}

	public GameHistory getHistory(){
		return history;
	}

	public void setHistory(GameHistory history){
		this.history = history; 
	}



	//  
	public boolean hasAdjacentSpace(int row, int column){
		char temp ='z';
		if (column>0){
			temp = boardArray[row][column-1]; //west
			if ((temp =='-')||(temp == '*'))
				return true;
		}
		if (column<18){
			temp =boardArray[row][column+1]; //east
			if ((temp =='-')||(temp == '*'))
				return true;
		}
		if(row<18){
			temp= boardArray[row+1][column]; //north
			if ((temp =='-')||(temp == '*'))
				return true;

		}
		if (row>0){
			temp =boardArray[row-1][column]; //south
			if ((temp =='-')||(temp == '*'))
				return true;
		}
		return false;
	}

	//returns char at position on board or 'e' for edge of board
	public char east(int row, int column){
		if (column<18){
			return boardArray[row][column+1];
		}
		return 'e';
	}
	public char west(int row, int column){
		if (column>0){
			return boardArray[row][column-1];
		}
		return 'e';
	}
	public char north(int row, int column){
		if (row<18){
			return boardArray[row+1][column];
		}
		return 'e';
	}
	public char south(int row, int column){
		if (row>0){
			return boardArray[row-1][column];
		}
		return 'e';
	}


	public char getTurn() {
		return turn;
	}

	public void setTurn(char turn) {
		this.turn =turn;
	}

	//returns number of stones
	public int getStoneCount() {
		int count=0;
		for (int i=0;i<19;i++){
			for (int j=0;j<19;j++){
				if ((boardArray[i][j]=='x')||(boardArray[i][j]=='o')){count++;}
			}
		}
		return count;
	}

	public void switchPlayer() {
		if (turn=='x'){
			turn = 'o';
		}
		else{
			turn = 'x';
		}
	}

	public String toString(){
		String gameString="";
		for (int row = 18; row >=0; row --){
			for (int column=0; column<19; column++){
				gameString=(gameString+(boardArray[row][column]));
			}
			gameString+=("\n");
		}
		String infoString = (turn+","+lifeRow+","+lifeColumn);
		gameString = gameString+infoString;
		return gameString;
	}








	public boolean compareArray(char[][] newBoard){
		for (int i=0;i<19;i++){
			for (int j=0;j<19;j++){
				if (this.boardArray[i][j]!=newBoard[i][j])
					return false;
			}
		}
		return true;
	}

	public void removeGroups(ArrayList<Integer> stones){
		for (int i=0;i<stones.size()-1;i=i+2){
			boardArray[stones.get(i)][stones.get(i+1)]='-';
		}
	}

	public void addGroups(char stone,ArrayList<Integer> groups){
		for (int i=0;i<groups.size()-1;i=i+2){
			boardArray[groups.get(i)][groups.get(i+1)]=stone;
		}
	}


	//returns a copy of the GoBoard - 
	public GoBoard copyBoard(){
		GoBoard copy = new GoBoard();
		copy.turn=this.turn;
		copy.history = history.copyHistory();
		copy.pass = this.pass;
		copy.lifeColumn = this.lifeColumn;
		copy.lifeRow = this.lifeRow;
		copy.lifePlayer=this.lifePlayer;

		for (int i=0;i<19;i++){
			for (int j=0;j<19;j++){
				copy.boardArray[i][j]=this.boardArray[i][j];
			}
		}
		return copy;

	}

	public void setLifeGroup (int row, int column){
		lifeRow=row;
		lifeColumn=column;
		//System.err.println("life player changed");
		lifePlayer = boardArray[row][column];
	}

	//returns char at location in group of interest 
	public char getlifeChar(){
		char life = boardArray[lifeRow][lifeColumn];
		return life;
	}

	public int[] getLifeLocation(){
		int[] coordinates = new int[2];
		coordinates[0]=lifeRow;
		coordinates[1]=lifeColumn;
		return coordinates;
	}






	/**
	 * @return the pass
	 */
	public int getPass() {
		return pass;
	}

	/**
	 * @param pass the pass to set
	 */
	public void setPass(int pass) {
		this.pass = pass;
	}

	/**
	 * @return the lifePlayer
	 */
	public char getLifePlayer() {
		return lifePlayer;
	}

	/**
	 * @param lifePlayer the lifePlayer to set
	 */
	public void setLifePlayer(char lifePlayer) {
		this.lifePlayer = lifePlayer;
	}

	/**
	 * @return the moveRow
	 */
	public int getMoveRow() {
		return moveRow;
	}

	/**
	 * @param moveRow the moveRow to set
	 */
	public void setMoveRow(int moveRow) {
		this.moveRow = moveRow;
	}

	/**
	 * @return the moveColumn
	 */
	public int getMoveColumn() {
		return moveColumn;
	}

	/**
	 * @param moveColumn the moveColumn to set
	 */
	public void setMoveColumn(int moveColumn) {
		this.moveColumn = moveColumn;
	}

	/**
	 * @return the currentScore
	 */
	public int getCurrentScore() {
		return currentScore;
	}

	/**
	 * @param currentScore the currentScore to set
	 */
	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}

	/* Scores boards using static evaluator and:
	 * returns 0 if argument b is equal to this
	 * returns 10 if argument b is greater than this
	 * returns -10 if argument b is less that this
	 */
	@Override
	public int compareTo(GoBoard b) {
		if(b.currentScore>this.currentScore){return 10;}
		else if(b.currentScore<this.currentScore){return -10;}
		else{return 0;}
	}

	/**
	 * @return the player
	 */
	public char getPlayer() {
		char player;
		if (turn=='o'){
			player ='x';
		}
		else{
			player='o';
		}
		return player;
	}

	public void setDescription(String description) {
		this.description = description;
		
	}
	
	public String getDescription(){
		return description;
	}

	

	


}
