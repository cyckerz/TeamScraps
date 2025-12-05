package entity_test;

import entity.Food;
import entity.Snake;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodTest {
    @Test
    void respawn_spawnOnWallOrSnake() {
        /*check if food spawn wall or snake body*/

        int cols = 5;
        int rows = 5;

        boolean[][] walls = new boolean[cols][rows];
        walls[0][0] = true;
        walls[2][2] = true;

        Snake snake = new Snake();
        Food food = new Food();

        food.respawn(walls, snake, cols, rows);

        int fx = food.getX();
        int fy = food.getY();

        assertFalse(walls[fx][fy], "Food spawned on a wall!");

        assertFalse(snake.occupies(fx, fy), "Food spawned on the snake!");
    }

    @Test
    void respawn_worksWithoutWalls() {
        /*check if method still works when no walls exist*/

        int c = 10;
        int r = 10;

        boolean[][] wall = null;
        Snake snake = new Snake();
        Food food = new Food();

        food.respawn(wall, snake, c,r);

        int fx = food.getX();
        int fy = food.getY();

        assertTrue(fx>=0 && fy<c);
        assertTrue(fx>=0 && fy<r);
        assertFalse(snake.occupies(fx, fy));
    }
}