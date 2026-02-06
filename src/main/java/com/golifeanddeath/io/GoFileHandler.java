package com.golifeanddeath.io;

import com.golifeanddeath.model.GoBoard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.golifeanddeath.model.GoBoard.BOARD_SIZE;

public class GoFileHandler {

    private static final Logger LOGGER = Logger.getLogger(GoFileHandler.class.getName());

    public GoBoard loadProblem(String filename) {
        GoBoard theBoard = new GoBoard();
        File inputFile = new File(filename);

        try (Scanner goProblem = new Scanner(inputFile)) {
            for (int row = BOARD_SIZE - 1; row >= 0; row--) {
                String line = goProblem.nextLine();
                for (int column = 0; column < BOARD_SIZE; column++) {
                    theBoard.getArray()[row][column] = line.charAt(column);
                }
            }
            String infoLine = goProblem.nextLine();
            String[] info = infoLine.split(",");
            theBoard.setTurn(info[0].charAt(0));
            theBoard.setLifeGroup(Integer.parseInt(info[1]), Integer.parseInt(info[2]));

            var description = new StringBuilder();
            while (goProblem.hasNextLine()) {
                description.append(goProblem.nextLine());
            }
            theBoard.setDescription(description.toString());
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "File not found: {0}", filename);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid number format in file: {0}", filename);
        }

        return theBoard;
    }

    public void saveProblem(GoBoard theBoard, String filename, String description) {
        try (PrintWriter out = new PrintWriter(filename)) {
            out.print(theBoard.toString());
            out.print("\n" + description);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "Cannot write to file: {0}", filename);
        }
    }
}
