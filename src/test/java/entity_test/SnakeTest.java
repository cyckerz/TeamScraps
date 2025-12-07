package entity_test;

import entity.Segment;
import entity.Snake;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SnakeTest {

    @Test
    void initializeSnakeTest() {
        //length of snake at start is 3
        Snake snake = new Snake();

        Segment head = snake.getHead();
        List<Segment> body = snake.getBody();

        assertEquals(3, body.size()+1);


        //check initial positions
        assertEquals(1, head.getX());
        assertEquals(3, head.getY());

        assertEquals(1, body.get(0).getX());
        assertEquals(2, body.get(0).getY());

        assertEquals(1, body.get(1).getX());
        assertEquals(1, body.get(1).getY());


        //check occupies() return true
        assertTrue(snake.occupies(1,3));
        assertTrue(snake.occupies(1,2));
        assertTrue(snake.occupies(1,1));
        assertFalse(snake.occupies(0,0));
    }

    @Test
    void setDirectionTest() {
        //cannot go opposite

        Snake snake = new Snake();

        snake.setDirection(Snake.Direction.LEFT);
        snake.move();
        Segment head = snake.getHead();

        assertEquals(2, head.getX());
        assertEquals(3, head.getY());

        snake.setDirection(Snake.Direction.UP);
        snake.move();
        head = snake.getHead();

        assertEquals(2, head.getX());
        assertEquals(2, head.getY());
    }

    @Test
    void moveTest() {
        //check all segment move in correct order
        Snake snake = new Snake();

        snake.move();
        Segment head = snake.getHead();
        List<Segment> body = snake.getBody();

        assertEquals(2, head.getX());
        assertEquals(3, head.getY());

        assertEquals(1, body.get(0).getX());
        assertEquals(3, body.get(0).getY());

        assertEquals(1, body.get(1).getX());
        assertEquals(2, body.get(1).getY());
    }

    @Test
    void growTest() {
        //segment will increase at tail position
        Snake snake = new Snake();
        List<Segment> bodyBefore = snake.getBody();
        int lengthBefore = bodyBefore.size() + 1;

        Segment tailBefore = bodyBefore.get(bodyBefore.size()-1);
        int tailX = tailBefore.getX();
        int tailY = tailBefore.getY();

        snake.grow();

        List<Segment> bodyAfter = snake.getBody();
        int lengthAfter = bodyAfter.size() + 1;

        assertEquals(lengthBefore+1, lengthAfter);

        Segment tailAfter = bodyAfter.get(bodyAfter.size()-1);
        assertEquals(tailX, tailAfter.getX());
        assertEquals(tailY, tailAfter.getY());
    }

    @Test
    void reverseFlipsHeadAndTail() {
        Snake snake = new Snake();

        Segment oldHead = snake.getHead();
        List<Segment> oldBody = snake.getBody();
        Segment oldTail = oldBody.get(oldBody.size() - 1);

        snake.reverse();

        Segment newHead = snake.getHead();
        Segment newTail = snake.getBody().get(snake.getBody().size() - 1);

        // new head should be old tail
        assertEquals(oldTail.getX(), newHead.getX());
        assertEquals(oldTail.getY(), newHead.getY());

        // new tail should be old head
        assertEquals(oldHead.getX(), newTail.getX());
        assertEquals(oldHead.getY(), newTail.getY());
    }

    @Test
    void reverseFlipsOrderHeadAndTail() {
        Snake snake = new Snake();
        snake.grow(); // make it a bit longer so flip is interesting

        // capture original order: [head, body...]
        java.util.List<Segment> before = new ArrayList<>();
        before.add(snake.getHead());
        before.addAll(snake.getBody());

        int originalLength = before.size();

        // act
        snake.reverse();

        // capture new order after reverse
        java.util.List<Segment> after = new ArrayList<>();
        after.add(snake.getHead());
        after.addAll(snake.getBody());

        // length must stay the same
        assertEquals(originalLength, after.size());

        // each segment should appear in the opposite position
        for (int i = 0; i < originalLength; i++) {
            Segment b = before.get(i);
            Segment a = after.get(originalLength - 1 - i);

            assertEquals(b.getX(), a.getX(), "X mismatch at index " + i);
            assertEquals(b.getY(), a.getY(), "Y mismatch at index " + i);
        }
    }

    @Test
    void reversePreservesOccupiedCells() {
        Snake snake = new Snake();
        snake.grow(); // again: have 4 segments total

        // occupied cells before reverse
        Set<String> before = new HashSet<>();
        before.add(snake.getHead().getX() + "," + snake.getHead().getY());
        for (Segment s : snake.getBody()) {
            before.add(s.getX() + "," + s.getY());
        }

        // act
        snake.reverse();

        // occupied cells after reverse
        Set<String> after = new HashSet<>();
        after.add(snake.getHead().getX() + "," + snake.getHead().getY());
        for (Segment s : snake.getBody()) {
            after.add(s.getX() + "," + s.getY());
        }

        // same set of coordinates before and after
        assertEquals(before, after);
    }



}
