package snake;

import javax.swing.*;
import java.awt.*;

/**
 * Header
 * <p>Top strip that shows the title and current score.</p>
 *
 * <h2>Project:</h2> CMSC132 – Project 4 (Snake)
 * @version 1.0
 * @since 2025-10-17
 * @author Brook Dawit
 * @author Moshe
 */
class Header extends JPanel {
    private static final long serialVersionUID = 1L;

    GameData data;

    /**
     * Creates a header bound to {@link GameData}.
     * @param data shared game data for score display
     */
    Header(GameData data) {
        this.data = data;
        setPreferredSize(new Dimension(10, 64));
        setBackground(ColorSet.BG);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth(), h = getHeight();

        g2.setColor(ColorSet.GRID2);
        g2.fillRoundRect(12, 12, w - 24, h - 24, 16, 16);

        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.setColor(ColorSet.BLUE);
        g2.drawString("Google Snake", 24, h / 2 + 8);

        String scoreText = "Score: " + data.score;
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(scoreText);
        g2.setColor(ColorSet.TEXT);
        g2.drawString(scoreText, w - tw - 24, h / 2 + 8);
    }
}
