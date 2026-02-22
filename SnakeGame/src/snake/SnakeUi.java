package snake;

import javax.swing.*;
import java.awt.*;

/**
 * SnakeUi 
 * <p>Builds the window, header, and board; starts the UI.</p>
 *
 * <h2>Project:</h2> CMSC132 – Project 4 (Snake)
 * @author Brook Dawit
 * @author Moshe
 * @version 1.0
 * @since 2025-10-17
 */
public class SnakeUi {

    /**
     * Starts the UI by creating a window, adding the header and board, and showing it.
     * @param args CLI args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Snake");
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            GameData data = GameData.sample();
            Header header = new Header(data);
            Board board = new Board(data, header);

            JPanel layout = new JPanel(new BorderLayout());
            layout.setBackground(ColorSet.BG);
            layout.add(header, BorderLayout.NORTH);
            layout.add(board, BorderLayout.CENTER);

            window.setContentPane(layout);
            window.setSize(720, 820);
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            // ensure key focus starts on the board for arrow input
            board.requestFocusInWindow();
        });
    }
}
