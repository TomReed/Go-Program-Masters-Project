package go;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JOptionPane;


public class GoFileHandler {

	private GoBoard theBoard;

	public GoBoard loadProblem(String filename){
		//instantiate a new GoBoard to populate with data from file
		theBoard = new GoBoard();

		File inputFile = new File(filename);
		try{
			Scanner goProblem = new Scanner(inputFile);
			//Obtain stone colours and locations from file and populate array
			for(int row=18;row>-1;row--){
				String line = goProblem.nextLine();
				for (int column=0;column<19;column++){
					theBoard.getArray()[row][column]=line.charAt(column);
				}
			}
			//Obtain information about player turn and capture and update 
			//instance variables
			String infoLine = goProblem.nextLine();
			String[] info =infoLine.split(",");
			//set the turn
			theBoard.setTurn(info[0].charAt(0));
			//set row and column of special group 
			theBoard.setLifeGroup(Integer.parseInt(info[1]),Integer.parseInt(info[2]));
			String description="";
			while(goProblem.hasNextLine()){
				description+=goProblem.nextLine();
			}
			theBoard.setDescription(description);
			
			goProblem.close();
		}
		catch(Exception e){

			JOptionPane.showMessageDialog(null,"No Valid File Found"); 

		}
		return theBoard;

	}
	
	public void saveProblem(GoBoard theBoard, String filename, String description){
		
		PrintWriter out;
		try {
			out = new PrintWriter(filename);
			out.print(theBoard.toString());
			out.print("\n"+description);
			out.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "file not found", "file not found",JOptionPane.ERROR_MESSAGE);
		}

	}


}
