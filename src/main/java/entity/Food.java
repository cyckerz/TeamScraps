package entity;

import java.awt.Graphics;
import java.util.Random;

public class Food {
    private int x, y;
    private final Random rng = new Random();

    public int getX(){ return x; }
    public int getY(){ return y; }

    public void respawn(boolean[][] walls, Snake snake, int cols, int rows) {
        do { x = rng.nextInt(cols); y = rng.nextInt(rows); }
        while ((walls!=null && walls[x][y]) || snake.occupies(x, y));
    }

    public void render(Graphics g, int unit) {
        g.fillRect(x*unit, y*unit, unit, unit);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}


