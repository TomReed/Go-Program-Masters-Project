package com.golifeanddeath.ui;

import com.golifeanddeath.engine.AlphaBetaDB;
import com.golifeanddeath.engine.MoveChecker;
import com.golifeanddeath.engine.NextMoves;
import com.golifeanddeath.io.GoFileHandler;
import com.golifeanddeath.model.GameHistory;
import com.golifeanddeath.model.GoBoard;

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
import java.nio.file.Path;

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

import static com.golifeanddeath.model.GoBoard.BOARD_SIZE;

@SuppressWarnings("serial")
public class GoGUI extends JFrame implements ActionListener, MouseListener {

    // instance variables
    JPanel controlPanel, editPanel, stonesPanel, modePanel, depthPanel, computerColourPanel, computerPanel;

    JButton loadButton, saveButton, passButton, clearButton, boundaryButton, startButton;

    JTextField playerText;
    JTextArea problemText;

    // radio buttons for different modes
    JRadioButton humanRadio, computerRadio, editRadio;
    ButtonGroup opponentGroup;

    // radio buttons for different edit options
    JRadioButton whiteRadio, blackRadio, emptyRadio, OOBradio, lifeRadio;
    ButtonGroup editGroup;

    JRadioButton computerWhite, computerBlack;
    ButtonGroup computerColourGroup;

    // combo boxes for breadth and depth limits
    JComboBox<Integer> breadthCombo, depthCombo;

    JLabel depthLabel, breadthLabel;

    BoardImage image;

    final int FRAME_WIDTH = 1008;
    final int FRAME_HEIGHT = 835;
    private GoFileHandler fileHandler;
    private GoBoard theBoard;
    private GameHistory history;
    private MoveChecker move;
    private JFileChooser chooser;
    private MoveComputer compute;

    public GoGUI() {
        createComponents();
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setTitle("Go Life and Death");
        setLocationRelativeTo(null);

        // MODE RADIO BUTTONS
        modePanel.setLayout(new GridLayout(3, 1));
        modePanel.setBorder(new TitledBorder(new EtchedBorder(), "Game Options"));
        modePanel.add(computerRadio);
        humanRadio.setSelected(true);
        modePanel.add(humanRadio);
        modePanel.add(editRadio);

        // EDIT OPTIONS BUTTONS
        stonesPanel.setLayout(new GridLayout(5, 1));
        stonesPanel.setBorder(new TitledBorder(new EtchedBorder(), "Add Stones or Spaces"));
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

        // Computer options
        depthPanel.setLayout(new GridLayout(2, 2));
        depthPanel.setBorder(new TitledBorder(new EtchedBorder(), "Tree Search Options"));
        depthPanel.add(depthLabel);
        depthPanel.add(depthCombo);
        depthCombo.setSelectedItem(99);
        depthPanel.add(breadthLabel);
        depthPanel.add(breadthCombo);
        breadthCombo.setSelectedItem(5);

        computerColourPanel.setLayout(new GridLayout(1, 2));
        computerColourPanel.add(computerBlack);
        computerColourPanel.add(computerWhite);
        computerBlack.setSelected(true);
        computerColourPanel.setBorder(new TitledBorder(new EtchedBorder(), "Computer colour:"));
        computerPanel.setVisible(false);

        controlPanel.add(playerText);
        controlPanel.add(modePanel);
        controlPanel.add(editPanel);

        computerPanel.add(computerColourPanel);
        computerPanel.add(depthPanel);
        computerPanel.add(startButton);
        controlPanel.add(computerPanel);

        controlPanel.add(passButton);
        controlPanel.add(loadButton);
        controlPanel.add(saveButton);
        controlPanel.add(problemText);

        add(image, BorderLayout.WEST);
        add(controlPanel, BorderLayout.EAST);

        fileHandler = new GoFileHandler();
        File goDirectory = resolveProblemsDirectory();
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(goDirectory);

        // initialise a new clear board and obtain reference to its history
        theBoard = new GoBoard();
        history = theBoard.getHistory();
        history.addGame(theBoard);
        move = new MoveChecker();
        update(theBoard);
    }

    private static File resolveProblemsDirectory() {
        // Look for goProblems relative to the working directory, then fall back to user home
        Path workingDir = Path.of(System.getProperty("user.dir"));
        Path candidate = workingDir.resolve("goProblems");
        if (candidate.toFile().isDirectory()) {
            return candidate.toFile();
        }
        // Fall back to parent directory (for when running from build output)
        candidate = workingDir.getParent() != null ? workingDir.getParent().resolve("goProblems") : candidate;
        if (candidate.toFile().isDirectory()) {
            return candidate.toFile();
        }
        return workingDir.toFile();
    }

    public void createComponents() {
        image = new BoardImage();
        image.setPreferredSize(new Dimension(800, 800));
        image.addMouseListener(this);

        controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(200, 800));

        computerPanel = new JPanel();
        computerPanel.setPreferredSize(new Dimension(200, 250));

        stonesPanel = new JPanel();
        stonesPanel.setPreferredSize(new Dimension(200, 155));

        editPanel = new JPanel();
        editPanel.setPreferredSize(new Dimension(200, 250));

        computerColourPanel = new JPanel();
        computerColourPanel.setPreferredSize(new Dimension(196, 40));

        modePanel = new JPanel();
        modePanel.setPreferredSize(new Dimension(196, 90));

        depthPanel = new JPanel();
        depthPanel.setPreferredSize(new Dimension(196, 80));

        depthLabel = new JLabel("MAX DEPTH:");
        breadthLabel = new JLabel("MAX BREADTH:");

        depthCombo = new JComboBox<>();
        for (int i = 0; i < 100; i++) {
            depthCombo.addItem(i);
        }
        breadthCombo = new JComboBox<>();
        for (int i = 1; i < 100; i++) {
            breadthCombo.addItem(i);
        }

        Dimension controlButtonSize = new Dimension(196, 30);

        startButton = new JButton("START");
        startButton.setPreferredSize(controlButtonSize);
        startButton.addActionListener(this);
        startButton.setForeground(Color.DARK_GRAY);
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
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

        Font goFont = new Font("Arial", Font.BOLD, 12);

        playerText = new JTextField(16);
        playerText.setEditable(false);
        playerText.setFont(goFont);

        problemText = new JTextArea(13, 14);
        problemText.setFont(new Font("Arial", Font.BOLD, 17));
        problemText.setEditable(false);
        problemText.setLineWrap(true);
        problemText.setWrapStyleWord(true);
    }

    private class MoveComputer extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() {
            playerText.setForeground(Color.RED);
            playerText.setText("* PROCESSING PLEASE WAIT *");
            NextMoves.getBestMoves(theBoard, 5);
            AlphaBetaDB abDB = new AlphaBetaDB();
            theBoard = abDB.getBestMove(theBoard, (int) depthCombo.getSelectedItem(), (int) breadthCombo.getSelectedItem());
            playerText.setForeground(Color.BLACK);
            update(theBoard);
            if (theBoard.getPass() != 0) {
                JOptionPane.showMessageDialog(null, "The computer passed, your go!");
            }
            return null;
        }
    }

    public void computeMove() {
        compute = new MoveComputer();
        compute.execute();
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        int x = (event.getX() + 20) - (event.getX() + 20) % 40;
        int y = (event.getY() + 20) - (event.getY() + 20) % 40;

        int column = x / 40 - 1;
        int row = BOARD_SIZE - y / 40;

        // EDIT MODE
        if (editRadio.isSelected()) {
            if (whiteRadio.isSelected()) { theBoard.getArray()[row][column] = 'o'; }
            else if (blackRadio.isSelected()) { theBoard.getArray()[row][column] = 'x'; }
            else if (emptyRadio.isSelected()) { theBoard.getArray()[row][column] = '-'; }
            else if (OOBradio.isSelected()) { theBoard.getArray()[row][column] = '*'; }
            else { theBoard.setLifeGroup(row, column); }
            update(theBoard);
        }
        // PLAY MODES
        else {
            if (move.play(theBoard, row, column)) {
                update(theBoard);

                if (computerRadio.isSelected()) {
                    update(theBoard);
                    computeMove();
                }

                int lRow = theBoard.getLifeLocation()[0];
                int lCol = theBoard.getLifeLocation()[1];
                if (theBoard.getArray()[lRow][lCol] == '-') {
                    JOptionPane.showMessageDialog(null, "GROUP CAPTURED!", "Life or Death", JOptionPane.PLAIN_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "YOU CANNOT PLAY HERE", "INVALID MOVE", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadButton) {
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getPath();
                theBoard = fileHandler.loadProblem(path);
                history = theBoard.getHistory();
                history.addGame(theBoard);
                move = new MoveChecker();
                update(theBoard);
                problemText.setText(theBoard.getDescription());
                setTitle("Go Life and Death: " + chooser.getSelectedFile().getName());
            }
        } else if (e.getSource() == startButton) {
            if ((computerBlack.isSelected() && theBoard.getTurn() == 'x') ||
                    (computerWhite.isSelected() && theBoard.getTurn() == 'o')) {
                computeMove();
                update(theBoard);
                int lRow = theBoard.getLifeLocation()[0];
                int lCol = theBoard.getLifeLocation()[1];
                if (theBoard.getArray()[lRow][lCol] == '-') {
                    JOptionPane.showMessageDialog(null, "GROUP CAPTURED!", "Life and Death", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "It's your turn, click where you'd like to play or press pass.",
                        "Life and Death", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (e.getSource() == saveButton) {
            int returnVal = chooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getPath();
                fileHandler.saveProblem(theBoard, path, problemText.getText());
                setTitle("Go Life and Death: " + chooser.getSelectedFile().getName());
            }
        } else if (e.getSource() == passButton) {
            if (!computerRadio.isSelected()) {
                theBoard.switchPlayer();
                theBoard.setPass(0);
                update(theBoard);
            } else if ((computerBlack.isSelected() && theBoard.getTurn() == 'o') ||
                    (computerWhite.isSelected() && theBoard.getTurn() == 'x')) {
                theBoard.switchPlayer();
                theBoard.setPass(0);
                computeMove();
                update(theBoard);
                int lRow = theBoard.getLifeLocation()[0];
                int lCol = theBoard.getLifeLocation()[1];
                if (theBoard.getArray()[lRow][lCol] == '-') {
                    JOptionPane.showMessageDialog(null, "GROUP CAPTURED!", "Life or Death", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "It's not your go, press START for computer to play",
                        "Life or Death", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == clearButton) {
            theBoard = new GoBoard();
            update(theBoard);
        } else if (e.getSource() == boundaryButton) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (theBoard.getArray()[i][j] == '-') {
                        theBoard.getArray()[i][j] = '*';
                    }
                }
            }
            update(theBoard);
        } else if (e.getSource() == editRadio) {
            editPanel.setVisible(true);
            problemText.setEditable(true);
            computerPanel.setVisible(false);
        } else if (e.getSource() == humanRadio) {
            editPanel.setVisible(false);
            problemText.setEditable(false);
            computerPanel.setVisible(false);
        } else if (e.getSource() == computerRadio) {
            editPanel.setVisible(false);
            problemText.setEditable(false);
            computerPanel.setVisible(true);
        }
    }

    public void update(GoBoard board) {
        image.updateImage(board);
        if (board.getTurn() == 'o') {
            playerText.setText("WHITE TO PLAY");
        } else {
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
