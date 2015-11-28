package go;

import javax.swing.JFrame;

public class GoMain {

	public static void main(String[] args){
		GoGUI gui = new GoGUI();
		gui.setVisible(true);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//System.out.println("Started");
	}

}