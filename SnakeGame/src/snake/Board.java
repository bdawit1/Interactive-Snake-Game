package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Board
 * <p>
 * Main grid area that renders the snake and apple, owns the tick timer,
 * processes input, advances the game state, and detects collisions.
 * </p>
 *
 * @author Brook Dawit
 * @version 1.0
 * @since 2025-10-17
 */
class Board extends JPanel {
    private static final long serialVersionUID = 1L;

    GameData data;
    Header header;
    int direction = Direction.RIGHT;
    Timer timer;

    /**
     * Creates a Board that renders and updates the game using the provided data.
     * @param data   shared game state (grid, snake, apple, score)
     * @param header header panel that displays the score
     */
    Board(GameData data, Header header) {
        this.data = data;
        this.header = header;

        setBackground(ColorSet.BG);
        setPreferredSize(new Dimension(data.cols * data.size + 40, data.rows * data.size + 40));

        // lambda expression for Timer action
        timer = new Timer(200, e -> moveSnake());
        timer.start();

        // anonymous class (KeyAdapter) for input handling
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP    -> direction = Direction.UP;
                    case KeyEvent.VK_DOWN  -> direction = Direction.DOWN;
                    case KeyEvent.VK_LEFT  -> direction = Direction.LEFT;
                    case KeyEvent.VK_RIGHT -> direction = Direction.RIGHT;
                    case KeyEvent.VK_R     -> resetGame(); // quick restart
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    /** Direction constants. */
    static class Direction {
        static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
    }

    /** Inner class example implementing {@link Movable}. */
    class SnakeMover implements Movable {
        @Override public void move() { moveSnake(); }
    }

    /**
     * Advances the snake one step, handles growth/collisions, and repaints.
     */
    void moveSnake() {
        Point head = data.snake[0];
        Point newHead = new Point(head);

        switch (direction) {
            case Direction.UP    -> newHead.y--;
            case Direction.DOWN  -> newHead.y++;
            case Direction.LEFT  -> newHead.x--;
            case Direction.RIGHT -> newHead.x++;
        }

        // Wall collision
        if (newHead.x < 0 || newHead.x >= data.cols || newHead.y < 0 || newHead.y >= data.rows) {
            gameOver("Game Over — You hit the wall!");
            return;
        }

        // Self collision
        for (Point part : data.snake) {
            if (part.equals(newHead)) {
                gameOver("Game Over — You ran into yourself!");
                return;
            }
        }

        // Apple collision
        boolean grow = false;
        if (newHead.equals(data.apple)) {
            grow = true;
            data.score++;
            if (header != null) header.repaint(); // update header immediately
            spawnApple();
        }

        // Build next snake
        Point[] next = new Point[grow ? data.snake.length + 1 : data.snake.length];
        next[0] = newHead;
        for (int i = 1; i < next.length; i++) {
            next[i] = new Point(data.snake[i - 1]);
        }
        data.snake = next;

        repaint();
    }

    /**
     * Resets snake, score, direction, apple, and timer.
     */
    private void resetGame() {
        int cx = data.cols / 2, cy = data.rows / 2;
        data.snake = new Point[] {
            new Point(cx, cy),
            new Point(cx - 1, cy),
            new Point(cx - 2, cy)
        };
        data.score = 0;
        direction = Direction.RIGHT;
        spawnApple();
        if (header != null) header.repaint();
        repaint();
        timer.restart();
        requestFocusInWindow();
    }

    /**
     * Handles game over behavior: stop, reset score, prompt restart.
     * @param message message shown in the dialog
     */
    private void gameOver(String message) {
        timer.stop();
        data.score = 0;                 // reset score on terminate
        if (header != null) header.repaint();

        int choice = JOptionPane.showConfirmDialog(
            this, message + "\nPlay again?", "Game Over",
            JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            requestFocusInWindow();
        }
    }

    /**
     * Randomly places an apple on a free cell.
     */
    void spawnApple() {
        while (true) {
            int x = (int) (Math.random() * data.cols);
            int y = (int) (Math.random() * data.rows);
            Point candidate = new Point(x, y);
            boolean onSnake = false;
            for (Point p : data.snake) {
                if (p.equals(candidate)) { onSnake = true; break; }
            }
            if (!onSnake) { data.apple = candidate; return; }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = 20;
        int gridW = data.cols * data.size;
        int gridH = data.rows * data.size;
        int x0 = (getWidth() - gridW) / 2;
        int y0 = (getHeight() - gridH) / 2;

        // board bg
        g2.setColor(ColorSet.GRID1);
        g2.fillRoundRect(x0 - pad, y0 - pad, gridW + pad * 2, gridH + pad * 2, 16, 16);

        // minor grid
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(ColorSet.GRID1.darker());
        for (int i = 0; i <= data.cols; i++) g2.drawLine(x0 + i * data.size, y0, x0 + i * data.size, y0 + gridH);
        for (int j = 0; j <= data.rows; j++) g2.drawLine(x0, y0 + j * data.size, x0 + gridW, y0 + j * data.size);

        // major grid every 5
        g2.setColor(ColorSet.GRID2);
        for (int i = 0; i <= data.cols; i += 5) g2.drawLine(x0 + i * data.size, y0, x0 + i * data.size, y0 + gridH);
        for (int j = 0; j <= data.rows; j += 5) g2.drawLine(x0, y0 + j * data.size, x0 + gridW, y0 + j * data.size);

        drawApple(g2, x0, y0);
        drawSnake(g2, x0, y0);
    }

    /** Draws the apple. */
    private void drawApple(Graphics2D g2, int x0, int y0) {
        int x = x0 + data.apple.x * data.size;
        int y = y0 + data.apple.y * data.size;
        int s = data.size;
        int pad = Math.max(4, s / 7);
        int d = s - pad * 2;
        g2.setColor(ColorSet.APPLE);
        g2.fillOval(x + pad, y + pad, d, d);
    }

    /** Draws the snake. */
    private void drawSnake(Graphics2D g2, int x0, int y0) {
        for (int i = 0; i < data.snake.length; i++) {
            Point p = data.snake[i];
            int x = x0 + p.x * data.size;
            int y = y0 + p.y * data.size;
            boolean head = (i == 0);
            int s = data.size;
            int pad = Math.max(3, s / 8);

            g2.setColor(head ? ColorSet.HEAD : ColorSet.BODY);
            g2.fillRoundRect(x + pad, y + pad, s - pad * 2, s - pad * 2, s / 2, s / 2);

            g2.setColor(ColorSet.OUTLINE);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(x + pad, y + pad, s - pad * 2, s - pad * 2, s / 2, s / 2);

            if (head) {
                int eye = Math.max(2, s / 10);
                g2.setColor(Color.BLACK);
                g2.fillOval(x + s / 2 - eye - 2, y + s / 2 - eye, eye, eye);
                g2.fillOval(x + s / 2 + 2,         y + s / 2 - eye, eye, eye);
            }
        }
    }
}
