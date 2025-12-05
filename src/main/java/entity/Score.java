package entity;

import java.io.Serializable;

/**
 * Represents a game score - just tracks foods eaten
 */
public class Score implements Serializable, Comparable<Score> {
    private static final long serialVersionUID = 1L;

    private String playerName;
    private int foodsEaten;  // This is all we track!
    private long timestamp;

    public Score(String playerName, int foodsEaten) {
        this.playerName = playerName;
        this.foodsEaten = foodsEaten;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters
    public String getPlayerName() { return playerName; }
    public int getFoodsEaten() { return foodsEaten; }
    public long getTimestamp() { return timestamp; }

    /**
     * Format for display - just show foods eaten!
     */
    public String getFormattedScore() {
        return String.format("%s: %d foods", playerName, foodsEaten);
    }

    /**
     * Compare by foods eaten (highest first)
     */
    @Override
    public int compareTo(Score other) {
        return Integer.compare(other.foodsEaten, this.foodsEaten);
    }

    @Override
    public String toString() {
        return String.format("Score{player='%s', foods=%d}",
                playerName, foodsEaten);
    }
}