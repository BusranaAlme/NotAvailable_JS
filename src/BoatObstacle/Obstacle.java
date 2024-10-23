package BoatObstacle;

import javax.swing.*;
import java.awt.*;

public class Obstacle {
    private int x, y;
    private int width, height;
    private Image obstacleImage;

    public Obstacle(int startX, int startY) {
        x = startX;
        y = startY;
        width = 53;
        height = 44;
        // load obstacle image
        obstacleImage = new ImageIcon(getClass().getResource("/assets/obstacle.png")).getImage();
    }

    public void draw(Graphics g) {
        // g.setColor(Color.GRAY);
        // g.fillRect(x, y, width, height);
        g.drawImage(obstacleImage, x, y, width, height, null);  // Draw the obstacle
    }

    public void move(int speed) {
        x -= speed;  // Adjust speed based on difficulty level
    }

    public int getX() {
        return x;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}