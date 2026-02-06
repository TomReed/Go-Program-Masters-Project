# Go Life and Death Problem Solver

A Java Swing application that solves Go (board game) life-and-death problems using alpha-beta minimax search. Built as a Master's dissertation project.

## Prerequisites

- Java 21 or later

## Building

Using the Gradle wrapper:

```bash
./gradlew build
```

Or with a system-installed Gradle (8.x+):

```bash
gradle build
```

## Running

```bash
./gradlew run
```

Or run the built JAR directly:

```bash
java -jar build/libs/GoLifeAndDeath.jar
```

## Usage

The application opens a 19x19 Go board with a control panel on the right.

### Modes

Switch between modes using the **Game Options** radio buttons:

- **Play against human** -- Two players alternate clicking on the board to place stones.
- **Play against computer** -- Play moves against the AI engine. Choose the computer's colour (black/white) and configure search depth/breadth, then press **START** for the computer to move.
- **Edit the Go Board** -- Set up a custom board position for analysis.

### Editing a Board

In edit mode you can:

1. **Add white/black stones** -- Select a stone colour and click intersections.
2. **Clear spaces** -- Remove individual stones.
3. **Mark out of bounds** -- Mark areas outside the problem region (click "Set Space to Out of Bounds" to fill all remaining empty space).
4. **Set life/death group** -- Click a stone to designate the group whose survival is being evaluated. A red dot marks the selected group.

### Playing Against the Computer

1. Load or edit a problem position.
2. Select **Play against computer**.
3. Choose the computer's colour.
4. Set **MAX DEPTH** (search tree depth limit; 99 = essentially unlimited) and **MAX BREADTH** (number of candidate moves evaluated per level).
5. Press **START** if it is the computer's turn, or click the board to play your move.
6. Press **PASS** to skip your turn.

### Loading and Saving Problems

- **LOAD** -- Opens a file chooser to load a `.txt` problem file. Sample problems are included in the `goProblems/` directory.
- **SAVE** -- Saves the current board position and description to a file.

### Problem File Format

Problem files are plain text:

```
<19 rows of 19 characters each, top row first>
<turn>,<lifeRow>,<lifeColumn>
<optional description>
```

Characters: `x` (black), `o` (white), `-` (empty), `*` (out of bounds).

## Project Structure

```
src/main/java/go/
  GoMain.java          Entry point
  GoGUI.java           Swing GUI and event handling
  GoBoard.java         Board state representation
  BoardImage.java      Board rendering
  GoFileHandler.java   Load/save problem files
  GameHistory.java     Move history for Ko detection
  MoveChecker.java     Move validation and capture logic
  BoardEvaluator.java  Static board evaluation heuristics
  AlphaBetaDB.java     Alpha-beta minimax search engine
  NextMoves.java       Candidate move generation
goProblems/            Sample life-and-death problems
```
