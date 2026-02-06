package go;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;

import static go.GoBoard.BOARD_SIZE;
import static go.GoBoard.MAX_INDEX;

@SuppressWarnings("serial")
public class BoardImage extends JComponent {

    private char[][] imageArray;
    private int[] specialGroup;

    public BoardImage() {
        this.setSize(800, 800);

        imageArray = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                imageArray[i][j] = '-';
            }
        }

        specialGroup = new int[]{0, 0};
    }

    public void updateImage(GoBoard board) {
        imageArray = board.getArray();
        specialGroup = board.getLifeLocation();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Color wood = new Color(220, 148, 79);

        g.setColor(wood);
        g.fillRect(0, 0, 800, 800);
        g.setColor(Color.BLACK);
        // draw vertical lines
        for (int x = 40; x < 800; x += 40) {
            g.drawLine(x, 40, x, 760);
        }
        // draw horizontal lines
        for (int y = 40; y < 800; y += 40) {
            g.drawLine(40, y, 760, y);
        }

        // draw key intersection marks
        for (int x = 156; x < 650; x += 240) {
            for (int y = 156; y < 650; y += 240) {
                g.fillOval(x, y, 8, 8);
            }
        }

        // draw column labels (A-T, no I by convention) and row labels (1-19)
        g.setFont(new Font("monospaced", Font.BOLD, 16));
        String columnLabels = "ABCDEFGHJKLMNOPQRST";
        int xPosition = 38;
        int yPosition = 765;
        for (int i = 0; i < BOARD_SIZE; i++) {
            g.drawString(columnLabels.charAt(i) + "", xPosition, 790);
            g.drawString(columnLabels.charAt(i) + "", xPosition, 18);
            xPosition += 40;
            g.drawString(i + 1 + "", 3, yPosition);
            g.drawString(i + 1 + "", 780, yPosition);
            yPosition -= 40;
        }

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                int x = 40 + 40 * column;
                int y = 40 + 40 * (MAX_INDEX - row);
                if (imageArray[row][column] == 'x') {
                    g.setColor(Color.BLACK);
                    g.fillOval(x - 19, y - 19, 38, 38);
                }
                if (imageArray[row][column] == 'o') {
                    g.setColor(Color.WHITE);
                    g.fillOval(x - 19, y - 19, 38, 38);
                }
                if (imageArray[row][column] == '*') {
                    g.setColor(Color.DARK_GRAY);
                    g.drawOval(x - 14, y - 14, 28, 28);
                    g.drawLine(x + 10, y - 10, x - 10, y + 10);
                }
            }
        }

        // Draw red dot to mark life/death group
        g.setColor(Color.RED);
        int row = specialGroup[0];
        int column = specialGroup[1];
        int x = 40 + 40 * column;
        int y = 40 + 40 * (MAX_INDEX - row);
        g.fillOval(x - 6, y - 6, 12, 12);
    }
}
