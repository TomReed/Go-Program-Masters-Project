package go;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GoMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GoGUI gui = new GoGUI();
            gui.setVisible(true);
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}
