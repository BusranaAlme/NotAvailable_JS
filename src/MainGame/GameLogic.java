package MainGame;

import BoatObstacle.*;
import java.util.ArrayList;
import java.util.Random;

public class GameLogic {
    private ArrayList<Obstacle> obstacles;
    private Boat boat;
    private int score;
    private int obstacleSpeed;
    private int screenWidth;
    private int screenHeight;
    private int difficulty; // 0: Easy, 1: Medium, 2: Hard
    private int timeElapsed; // To keep track of time for speed increase

    public GameLogic(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.obstacles = new ArrayList<>();
        this.timeElapsed = 0;
    }

    public void initializeGame(Boat boat, int speed) {
        this.boat = boat;
        this.difficulty = difficulty;
        this.obstacleSpeed = speed;
        this.score = 0;
        obstacles.clear();
    }

    public void updateGame() {
        boat.move();
        updateObstacles();
        updateObstacleSpeed();
    }
    private void updateObstacleSpeed() {
        timeElapsed++;
        if (timeElapsed % 100 == 0) { // Every 100 ticks (adjust as needed)
            switch (difficulty) {
                case 0: // Easy
                    obstacleSpeed += 1; // Increment speed slightly
                    break;
                case 1: // Medium
                    obstacleSpeed += 2; // Increment speed more than easy
                    break;
                case 2: // Hard
                    obstacleSpeed += 3; // Increment speed the most
                    break;
            }
        }
    }

    public boolean checkCollisions() {
        for (Obstacle obs : obstacles) {
            if (obs.getBounds().intersects(boat.getBounds())) {
                return true;
            }
        }
        return false;
    }

    private void updateObstacles() {
        Random rand = new Random();

        if (rand.nextInt(100) < 7) {
            boolean overlaps = true;
            Obstacle newObstacle = null;

            while (overlaps) {
                int startY = rand.nextInt(screenHeight - 275) + 120;
                newObstacle = new Obstacle(screenWidth, startY);
                overlaps = false;

                for (Obstacle obs : obstacles) {
                    if (newObstacle.getBounds().intersects(obs.getBounds())) {
                        overlaps = true;
                        break;
                    }
                }
            }
            obstacles.add(newObstacle);
        }

        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obs = obstacles.get(i);
            obs.move(obstacleSpeed);

            // Adjusted removal logic to use bounding box for width
            if (obs.getX() + obs.getBounds().width < 0) {
                obstacles.remove(i);
                score++;
            }
        }
    }

    public ArrayList<Obstacle> getObstacles() { return obstacles; }
    public int getScore() { return score; }
}
