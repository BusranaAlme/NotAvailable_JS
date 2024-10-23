import java.util.ArrayList;
import java.util.Random;

public class GameLogic {
    private ArrayList<Obstacle> obstacles;
    private Boat boat;
    private int score;
    private int obstacleSpeed;
    private int screenWidth;
    private int screenHeight;

    public GameLogic(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.obstacles = new ArrayList<>();
    }

    public void initializeGame(Boat boat, int speed) {
        this.boat = boat;
        this.obstacleSpeed = speed;
        this.score = 0;
        obstacles.clear();
    }

    public void updateGame() {
        boat.move();
        updateObstacles();
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

        if (rand.nextInt(100) < 5) {
            boolean overlaps = true;
            Obstacle newObstacle = null;

            while (overlaps) {
                int startY = rand.nextInt(screenHeight - 275) + 83;
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
