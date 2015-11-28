package go;
import java.util.ArrayList;



public class GameHistory {

	private ArrayList<ArrayList<char[][]>> history;

	//constructor
	public GameHistory(){
		history = new ArrayList<ArrayList<char[][]>>();
		//add empty arrayLists to it to create initial locations  
		ArrayList<char[][]> emptyArrayList = new ArrayList<char[][]>();
		for (int i=0;i<361;i++){
			history.add(emptyArrayList); //the empty arraylists
		}
	}
	
	//returns ArrayList containing boards with stone count
	public ArrayList<char[][]> getGames(int count){
		ArrayList<char[][]> histBoards = history.get(count);
		
		return histBoards;
	}
	
	

	//add a COPY of this game to ArrayList at index = GoBoard.stoneCount
	public void  addGame(GoBoard board){
		int numStones = board.getStoneCount();
		ArrayList<char[][]> gameList = history.get(numStones); //obtain reference to collection corresponding to stone count
		char[][] newBoard = new char[19][19];
		//create a deep copy of board to be added to collection
		for (int i=0;i<19;i++){
			for (int j=0;j<19;j++){
				newBoard[i][j]=board.getArray()[i][j];
			}
		}
		// add a copy of the board to the collection here
		gameList.add(newBoard); 
	}
	
	
	

	//returns  true if GoBoard with the same board array is in the history
	public boolean inHistory(GoBoard board){
		int numStones = board.getStoneCount();
		for (char[][] g : history.get(numStones)){
			boolean match = true;
			for (int i=0;i<19;i++){
				for (int j=0;j<19;j++){
					if(g[i][j]!=board.getArray()[i][j]){
						match = false;
					}
				}
			}
			if (match) return true;
		}
		return false;
	}
	
	public void  setHistory (ArrayList<ArrayList<char[][]>> history){
		this.history = history;
	}
	
	
	//creates a copy of the history
	public GameHistory copyHistory(){
		GameHistory copyHist = new GameHistory();
		//copy array
		ArrayList<ArrayList<char[][]>> newArray = new ArrayList<ArrayList<char[][]>>();
		ArrayList<char[][]> boardsList; 
		for (int i=0;i<361;i++){
			boardsList = new ArrayList<char[][]>();
			newArray.add(boardsList); //add an empty array list at position i
			for (char[][] b : history.get(i)){
				char[][] copyOfboard = new char[19][19];
				boardsList.add(copyOfboard);
				for(int j=0;j<19;j++){
					for (int k=0;k<19;k++){
						copyOfboard[j][k]=b[j][k];
					}
				}
			}
		}
		
		
		//set 
		copyHist.setHistory(newArray);
		
		return copyHist;
	}
	
	
	
}