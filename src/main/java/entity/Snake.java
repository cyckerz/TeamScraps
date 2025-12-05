// in entity/Snake.java
package entity;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Snake {
    public enum Direction { UP, DOWN, LEFT, RIGHT }

    private final List<Segment> segments = new ArrayList<>();
    private Direction direction = Direction.RIGHT;

    public Snake() { initializeSnake(); }

    public void initializeSnake() {
        direction = Direction.RIGHT;
        segments.clear();
        segments.add(new Segment(1,3));
        segments.add(new Segment(1,2));
        segments.add(new Segment(1,1));
    }

    public Segment getHead() { return segments.get(0); }
    public List<Segment> getBody() { return segments.subList(1, segments.size()); }

    public boolean occupies(int x, int y) {
        for (Segment s : segments) if (s.getX()==x && s.getY()==y) return true;
        return false;
    }

    public void setDirection(Direction newDir) {
        if (!isOpposite(direction, newDir)) direction = newDir;
    }
    private boolean isOpposite(Direction a, Direction b) {
        return (a==Direction.UP && b==Direction.DOWN) || (a==Direction.DOWN && b==Direction.UP) ||
                (a==Direction.LEFT && b==Direction.RIGHT) || (a==Direction.RIGHT && b==Direction.LEFT);
    }

    public void move() {
        int headX = segments.get(0).getX();
        int headY = segments.get(0).getY();
        switch (direction) {
            case UP:    headY--; break;
            case DOWN:  headY++; break;
            case LEFT:  headX--; break;
            case RIGHT: headX++; break;
        }
        for (int i = segments.size()-1; i>0; i--) {
            segments.get(i).set_Position(segments.get(i-1).getX(), segments.get(i-1).getY());
        }
        segments.get(0).set_Position(headX, headY);
    }

    public void grow() {
        Segment tail = segments.get(segments.size()-1);
        segments.add(new Segment(tail.getX(), tail.getY()));
    }

    public void rendering(Graphics g, int unitSize) {
        for (Segment s : segments) {
            g.fillOval(s.getX()*unitSize, s.getY()*unitSize, unitSize, unitSize);
        }
    }
}
