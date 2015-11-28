package go;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class GoGUI extends JFrame implements ActionListener,MouseListener {


	//instance variables
	JPanel controlPanel,editPanel,stonesPanel,modePanel,depthPanel,computerColourPanel,computerPanel;

	JButton loadButton,saveButton,passButton,clearButton,boundaryButton,startButton;

	JTextField playerText;
	JTextArea  problemText;
	JTextField infoText;

	JTextField textInput;

	//radio buttons for different modes - computer/human/edit board
	JRadioButton humanRadio,computerRadio,editRadio;
	ButtonGroup opponentGroup;

	//radio buttons for different edit options
	JRadioButton whiteRadio,blackRadio,emptyRadio,OOBradio,lifeRadio;
	ButtonGroup editGroup;

	//JRadioButton
	JRadioButton computerWhite,computerBlack;
	ButtonGroup computerColourGroup;


	//combo boxes for breadth and depth limits
	JComboBox<Integer> breadthCombo, depthCombo;

	JLabel depthLabel, breadthLabel,playLabel;




	BoardImage image;

	final int FRAME_WIDTH=1008;
	final int FRAME_HEIGHT=835;
	private GoFileHandler fileHandler;
	private GoBoard theBoard; 
	private GameHistory history; 
	private MoveChecker move;
	private JFileChooser chooser;
	private MoveComputer compute;


	//flags

	public GoGUI(){


		createComponents();
		setSize(FRAME_WIDTH,FRAME_HEIGHT);
		setResizable(false);
		setTitle("Go Life and Death");
		setLocationRelativeTo(null); //centres the frame on the display
		//controlPanel.setBackground(Color.white);

		//MODE RADIO BUTTONS - human/computer/edit
		modePanel.setLayout(new GridLayout(3,1));
		modePanel.setBorder(new TitledBorder(new EtchedBorder(),"Game Options"));
		modePanel.add(computerRadio);
		humanRadio.setSelected(true);
		modePanel.add(humanRadio);
		modePanel.add(editRadio);
		//modePanel.setBackground(Color.WHITE);
		//modePanel.setForeground(Color.WHITE);




		//EDIT OPTIONS BUTTONS
		//stonesPanel.setBackground(Color.WHITE);
		//stonesPanel.setForeground(Color.WHITE);
		stonesPanel.setLayout(new GridLayout(5,1));
		stonesPanel.setBorder(new TitledBorder(new EtchedBorder(),"Add Stones or Spaces"));
		stonesPanel.add(whiteRadio);
		stonesPanel.add(blackRadio);
		whiteRadio.setSelected(true);
		stonesPanel.add(emptyRadio);
		stonesPanel.add(OOBradio);
		stonesPanel.add(lifeRadio);
		editPanel.add(stonesPanel);
		editPanel.add(boundaryButton);
		editPanel.add(clearButton);
		editPanel.setVisible(false);


		//Computer options
		//depthPanel.setBackground(Color.WHITE);
		depthPanel.setLayout(new GridLayout(2,2));
		depthPanel.setBorder(new TitledBorder(new EtchedBorder(),"Tree Search Options"));
		depthPanel.add(depthLabel);
		depthPanel.add(depthCombo);
		depthCombo.setSelectedItem(99);
		depthPanel.add(breadthLabel);
		depthPanel.add(breadthCombo);
		breadthCombo.setSelectedItem(5);

		computerColourPanel.setLayout(new GridLayout(1,2));
		computerColourPanel.add(computerBlack);
		computerColourPanel.add(computerWhite);
		computerBlack.setSelected(true);
		computerColourPanel.setBorder(new TitledBorder(new EtchedBorder(),"Computer colour:"));
		computerPanel.setVisible(false);










		controlPanel.add(playerText);
		controlPanel.add(modePanel);
		controlPanel.add(editPanel);

		computerPanel.add(computerColourPanel);
		computerPanel.add(depthPanel);
		computerPanel.add(startButton);
		controlPanel.add(computerPanel);
		//controlPanel.add(computerColourPanel);
		//controlPanel.add(depthPanel);

		//controlPanel.add(startButton);
		controlPanel.add(passButton);
		controlPanel.add(loadButton);
		controlPanel.add(saveButton);
		controlPanel.add(problemText);


		add(image,BorderLayout.WEST);
		add(controlPanel,BorderLayout.EAST);

		fileHandler = new GoFileHandler();
		File goDirectory = new File("c:\\goProblems");
		chooser= new JFileChooser();
		chooser.setCurrentDirectory(goDirectory);

		//initialise a new clear board and obtain reference to its history
		theBoard = new GoBoard();
		history = theBoard.getHistory(); //start a new game history with new game
		history.addGame(theBoard); //add initial board to history
		//instantiate a movechecker class to play moves;
		move = new MoveChecker();
		update(theBoard);

	}

	public void createComponents(){
		//Create board image object and add mouse listener
		image = new BoardImage();
		image.setPreferredSize(new Dimension(800,800));
		image.addMouseListener(this);

		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(200,800));

		computerPanel = new JPanel();
		computerPanel.setPreferredSize(new Dimension(200,250));

		stonesPanel = new JPanel();
		stonesPanel.setPreferredSize(new Dimension(200,155));

		editPanel = new JPanel();
		editPanel.setPreferredSize(new Dimension(200,250));

		computerColourPanel = new JPanel();
		computerColourPanel.setPreferredSize(new Dimension(196,40));

		modePanel = new JPanel();
		modePanel.setPreferredSize(new Dimension(196,90));

		depthPanel = new JPanel();
		depthPanel.setPreferredSize(new Dimension(196,80));

		depthLabel = new JLabel("MAX DEPTH:");
		breadthLabel = new JLabel("MAX BREADTH:");


		depthCombo = new JComboBox<Integer>();
		for(int i=0;i<100;i++){
			depthCombo.addItem(i);
		}
		breadthCombo = new JComboBox<Integer>();
		for(int i=1;i<100;i++){
			breadthCombo.addItem(i);
		}

		Dimension controlButtonSize = new Dimension(196,30);

		startButton = new JButton("START");
		startButton.setPreferredSize(controlButtonSize);
		startButton.addActionListener(this);
		startButton.setForeground(Color.DARK_GRAY);
		startButton.setFont(new Font("ariel",Font.BOLD,14));
		loadButton = new JButton("LOAD");
		loadButton.setPreferredSize(controlButtonSize);
		loadButton.addActionListener(this);
		saveButton = new JButton("SAVE ");
		saveButton.setPreferredSize(controlButtonSize);
		saveButton.addActionListener(this);
		passButton = new JButton("PASS");
		passButton.setPreferredSize(controlButtonSize);
		passButton.addActionListener(this);

		clearButton = new JButton("Clear All Stones");
		clearButton.setPreferredSize(controlButtonSize);
		clearButton.addActionListener(this);
		boundaryButton = new JButton("Set Space to Out of Bounds");
		boundaryButton.setPreferredSize(controlButtonSize);
		boundaryButton.addActionListener(this);


		//create radio button group for human or computer opponents
		humanRadio = new JRadioButton("Play against human.");
		humanRadio.addActionListener(this);

		computerRadio = new JRadioButton("Play against computer.");
		computerRadio.addActionListener(this);

		editRadio = new JRadioButton("Edit the Go Board");
		editRadio.addActionListener(this);

		computerBlack = new JRadioButton("Black");
		computerWhite = new JRadioButton("White");
		computerColourGroup = new ButtonGroup();
		computerColourGroup.add(computerBlack);
		computerColourGroup.add(computerWhite);



		opponentGroup = new ButtonGroup();
		opponentGroup.add(humanRadio);
		opponentGroup.add(computerRadio);
		opponentGroup.add(editRadio);
		whiteRadio = new JRadioButton("ADD WHITE STONES   ");
		whiteRadio.addActionListener(this);
		blackRadio = new JRadioButton("ADD BLACK STONES   ");
		blackRadio.addActionListener(this);
		emptyRadio = new JRadioButton("CLEAR SPACES   ");
		emptyRadio.addActionListener(this);
		OOBradio = new JRadioButton("MARK OUT OF BOUNDS  ");
		OOBradio.addActionListener(this);
		lifeRadio = new JRadioButton("SET LIFE/DEATH GROUP");
		lifeRadio.addActionListener(this);
		editGroup = new ButtonGroup();
		editGroup.add(blackRadio);
		editGroup.add(whiteRadio);
		editGroup.add(emptyRadio);
		editGroup.add(OOBradio);
		editGroup.add(lifeRadio);




		Font goFont = new Font("ariel", Font.BOLD,12);


		playerText = new JTextField(16);
		playerText.setEditable(false);
		playerText.setFont(goFont);

		problemText = new JTextArea(13,14);
		problemText.setFont(new Font("ariel",Font.BOLD,17));
		//problemText.setForeground(Color.BLUE);
		problemText.setEditable(false);
		problemText.setLineWrap(true);
		problemText.setWrapStyleWord(true);
		//infoText = new JTextField(10);
		//infoText.setEditable(false);

	}

	private class MoveComputer extends SwingWorker<Void,Void>{
		protected Void doInBackground() {
			playerText.setForeground(Color.RED);
			playerText.setText("* PROCESSING PLEASE WAIT *");
			//long startTime = System.currentTimeMillis();
			NextMoves.getBestMoves(theBoard, 5);
			AlphaBetaDB abDB = new AlphaBetaDB();
			theBoard = abDB.getBestMove(theBoard,(int)depthCombo.getSelectedItem(),(int)breadthCombo.getSelectedItem());	
			playerText.setForeground(Color.BLACK);
			update(theBoard);
			//System.err.println("DONE");
			//long endTime = System.currentTimeMillis();
			//System.out.print("Compute time: "+ (endTime - startTime)/1000.000+ " seconds");
			if(theBoard.getPass()!=0){
				JOptionPane.showMessageDialog(null, "The computer passed, your go!");
			}
			return null;
		}

	}






	public void computeMove(){
		//	System.err.println("Processing");
		compute = new MoveComputer();
		compute.execute();
	}


	@Override
	public void mouseClicked(MouseEvent event) {

		/*   TEST CODE FOR COUNTING GOOD AND BAD SHAPES
		System.out.println("Number of black mouths: "+BoardEvaluator.countMouths(theBoard.getArray(),'x'));
		System.out.println("Number of black empty triangles: "+BoardEvaluator.countEmptyTriangles(theBoard.getArray(),'x'));
		System.out.println("Number of white mouths: "+BoardEvaluator.countMouths(theBoard.getArray(),'o'));
		System.out.println("Number of white empty triangles: "+BoardEvaluator.countEmptyTriangles(theBoard.getArray(),'o'));
		System.out.println("Board score black:" + BoardEvaluator.evaluate(theBoard,'x'));
		System.out.println("Board score white:" + BoardEvaluator.evaluate(theBoard,'o'));
		 */


		//OBTAIN LOCATION OF MOUSE CLICK ON BOARD TO NEAREST INTERSECTION OF LINES
		//return mouse locations rounded to nearest location divisible by 40.
		int x = (event.getX()+20) - (event.getX()+20)%40; 
		int y = (event.getY()+20) -(event.getY()+20)%40;

		//x positions correspond to boardArray columns where 0 is column 0, 760 is column 1
		//y positions correspond to boardArray rows where 0 is row 19 and 760 is row 0

		//CONVERT TO BOARD ARRAY LOCATIONS - (40pixels between locations)
		int column = x/40-1;
		int row = 19-y/40;

		//EDIT MODE
		if(editRadio.isSelected()){
			if(whiteRadio.isSelected()){theBoard.getArray()[row][column]='o';}
			else if(blackRadio.isSelected()){theBoard.getArray()[row][column]='x';}
			else if(emptyRadio.isSelected()){theBoard.getArray()[row][column]='-';}
			else if(OOBradio.isSelected()){theBoard.getArray()[row][column]='*';}
			else {theBoard.setLifeGroup(row, column);}
			update(theBoard);
		}

		//PLAY MODES - HUMAN OR COMPUTER
		else {
			//human plays a move
			if (move.play(theBoard,row,column)){ //if move is valid
				update(theBoard);

				//computer plays following move using minimax/alpha beta if computer radio button selected 
				if(computerRadio.isSelected()){
					update(theBoard);
					computeMove();
				}

				int lRow = theBoard.getLifeLocation()[0];
				int lCol = theBoard.getLifeLocation()[1];
				if(theBoard.getArray()[lRow][lCol]=='-'){
					JOptionPane.showMessageDialog(null, "GROUP CAPTURED!" ,"Life or Death", JOptionPane.PLAIN_MESSAGE);
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "YOU CANNOT PLAY HERE" ,"INVALID MOVE", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {


		if (e.getSource()==loadButton){
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal==JFileChooser.APPROVE_OPTION);{
				String path =chooser.getSelectedFile().getPath();
				theBoard=fileHandler.loadProblem(path);
				history = theBoard.getHistory(); //start a new game history with new game
				history.addGame(theBoard); //add initial board to history
				move = new MoveChecker();
				update(theBoard);
				problemText.setText(theBoard.getDescription());
				setTitle("Go Life and Death: "+chooser.getSelectedFile().getName());
			}
		}

		else if(e.getSource()==startButton){
			if((computerBlack.isSelected()&&(theBoard.getTurn()=='x'))||(computerWhite.isSelected()&&theBoard.getTurn()=='o')){	
				computeMove();
				update(theBoard);
				//check if computer kills group, if so display message
				int lRow = theBoard.getLifeLocation()[0];
				int lCol = theBoard.getLifeLocation()[1];
				if(theBoard.getArray()[lRow][lCol]=='-'){
					JOptionPane.showMessageDialog(null, "GROUP CAPTURED!" ,"Life and Death", JOptionPane.WARNING_MESSAGE);
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "It's your turn, click where you'd like to play or press pass." ,"Life and Death", JOptionPane.INFORMATION_MESSAGE);
			}
		}

		else if (e.getSource()==saveButton){
			int returnVal = chooser.showSaveDialog(this);
			if (returnVal==JFileChooser.APPROVE_OPTION);{
				String path =chooser.getSelectedFile().getPath();
				fileHandler.saveProblem(theBoard,path,problemText.getText());
				setTitle("Go Life and Death: "+chooser.getSelectedFile().getName());
			}
		}
		else if(e.getSource()==passButton){
			if(!computerRadio.isSelected()){ 
				theBoard.switchPlayer();
				theBoard.setPass(0);
				update(theBoard); //update this GUI --to change displayed turn
			}
			//compute mode selected - if it is not the computer's go - pass and compute next move
			else if ((computerBlack.isSelected()&&theBoard.getTurn()=='o')||(computerWhite.isSelected()&&theBoard.getTurn()=='x')){
				theBoard.switchPlayer();
				theBoard.setPass(0);
				computeMove();
				update(theBoard);
				//check if computer kills group, if so display message
				int lRow = theBoard.getLifeLocation()[0];
				int lCol = theBoard.getLifeLocation()[1];
				if(theBoard.getArray()[lRow][lCol]=='-'){
					JOptionPane.showMessageDialog(null, "GROUP CAPTURED!" ,"Life or Death", JOptionPane.WARNING_MESSAGE);
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "It's not your go, press START for computer to play" ,"Life or Death", JOptionPane.WARNING_MESSAGE);
			}
		}


		else if(e.getSource()==clearButton){
			theBoard=new GoBoard();
			update(theBoard);
		}
		else if(e.getSource()==boundaryButton){
			for (int i=0;i<19;i++){
				for (int j=0;j<19;j++){
					if (theBoard.getArray()[i][j]=='-'){
						theBoard.getArray()[i][j]='*';
					}
				}
			}
			update(theBoard);
		}
		else if (e.getSource()==editRadio){
			editPanel.setVisible(true);
			problemText.setEditable(true);
			computerPanel.setVisible(false);
		}
		else if((e.getSource()==humanRadio)){
			editPanel.setVisible(false);
			problemText.setEditable(false);
			computerPanel.setVisible(false);
		}
		else if(e.getSource()==computerRadio){
			editPanel.setVisible(false);
			problemText.setEditable(false);
			computerPanel.setVisible(true);
		}

	}

	public void update(GoBoard board){

		image.updateImage(board);
		if(board.getTurn()=='o'){
			playerText.setText("WHITE TO PLAY");
		}
		else{
			playerText.setText("BLACK TO PLAY");
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}



}



