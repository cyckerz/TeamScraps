package screen;

import engine.Core;
import engine.GameStates;
import engine.StateMachine;
import engine.InputManager;
import entity.*;

import java.awt.*;

public class GameScreen {
    private final StateMachine states;
    private final InputManager input;

    private final int unit = 20;
    private final int cols = Core.WIDTH / unit;
    private final int rows = Core.HEIGHT / unit;

    private boolean wrap = true;           // set false for border walls
    private boolean[][] walls;             // environment grid
    private final CollisionHandler collider = new CollisionHandler();

    private Snake snake;
    private Food food;

    public GameScreen(StateMachine states, InputManager input) {
        this.states = states;
        this.input = input;
    }

    public void onEnter() {
        snake = new Snake();
        buildMapEasy();
        food = new Food();
        food.respawn(walls, snake, cols, rows);
    }

    public void onExit() { /* nothing for now */ }

    public void update(double dt) {
        if (states.is(GameStates.PAUSED)) return;

        Snake.Direction nd = input.consumeDirectionChange();
        if (nd != null) snake.setDirection(nd);

        snake.move();

        CollisionHandler.Result r =
                collider.check(snake, food, walls, wrap, cols, rows);

        switch (r) {
            case ATE_FOOD:
                snake.grow();
                food.respawn(walls, snake, cols, rows);
                break;
            case HIT_SELF:
            case HIT_WALL:
                states.set(GameStates.GAME_OVER);
                break;
            default: /* no-op */ }
    }

    public void render(Graphics2D g) {
        // walls
        if (walls != null) {
            g.setColor(Color.DARK_GRAY);
            for (int x = 0; x < cols; x++)
                for (int y = 0; y < rows; y++)
                    if (walls[x][y]) g.fillRect(x * unit, y * unit, unit, unit);
        }
        // food
        g.setColor(Color.RED);
        g.fillRect(food.getX() * unit, food.getY() * unit, unit, unit);

        // snake
        g.setColor(Color.GREEN);
        snake.rendering(g, unit);
    }

    // --- simple environment presets (easy). Others can be added later.
    private void buildMapEasy() {
        walls = new boolean[cols][rows];
        if (!wrap) {
            for (int x = 0; x < cols; x++) { walls[x][0] = true; walls[x][rows - 1] = true; }
            for (int y = 0; y < rows; y++) { walls[0][y] = true; walls[cols - 1][y] = true; }
        }
    }
}
