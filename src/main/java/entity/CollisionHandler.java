package entity;

public class CollisionHandler {
    public enum Result { NONE, ATE_FOOD, HIT_SELF, HIT_WALL }

    public Result check(Snake snake, Food food, boolean[][] walls,
                        boolean wrap, int cols, int rows) {

        Segment head = snake.getHead();
        int x = head.getX(), y = head.getY();

        if (wrap) {
            if (x < 0) x = cols-1; else if (x >= cols) x = 0;
            if (y < 0) y = rows-1; else if (y >= rows) y = 0;
            head.set_Position(x, y);
        } else {
            if (x < 0 || x >= cols || y < 0 || y >= rows) return Result.HIT_WALL;
        }

        if (walls != null && walls[x][y]) return Result.HIT_WALL;

        for (Segment s : snake.getBody())
            if (s.getX()==x && s.getY()==y) return Result.HIT_SELF;

        if (food != null && food.getX()==x && food.getY()==y) return Result.ATE_FOOD;

        return Result.NONE;
    }
}
