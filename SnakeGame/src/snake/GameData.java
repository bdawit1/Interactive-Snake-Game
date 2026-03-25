package snake;

import java.awt.Point;

/**
 * GameData
 * <p>Encapsulates grid dimensions, snake body, apple placement, and score.</p>
 *
 * @author Brook Dawit
 * @version 1.0
 * @since 2025-10-17
 */
class GameData {
    int cols, rows, size;
    Point[] snake;  // head at index 0
    Point apple;
    int score;

    /**
     * Constructs a {@code GameData}.
     * @param cols  number of columns
     * @param rows  number of rows
     * @param size  cell size (pixels)
     * @param snake initial snake body (head first)
     * @param apple initial apple position
     * @param score starting score
     */
    GameData(int cols, int rows, int size, Point[] snake, Point apple, int score) {
        this.cols = cols;
        this.rows = rows;
        this.size = size;
        this.snake = snake;
        this.apple = apple;
        this.score = score;
    }

    /**
     * Convenience factory with sample values.
     * @return starter {@code GameData} for demo
     */
    static GameData sample() {
        int cols = 20, rows = 20, size = 28;
        Point[] snake = new Point[] {
            new Point(10, 10),
            new Point(9, 10),
            new Point(8, 10),
            new Point(7, 10),
            new Point(7, 11),
            new Point(7, 12)
        };
        Point apple = new Point(14, 6);
        int score = 0;
        return new GameData(cols, rows, size, snake, apple, score);
    }
}
