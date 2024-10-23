import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

class Boat {
    private int x, y;
    private int dx, dy;
    private int width, height;
    private Image boatImage;

    public Boat() {
        width = 58;
        height = 57;
        x = 100;
        y = 350;
        boatImage = new ImageIcon(getClass().getResource("/assets/boat.png")).getImage();
    }

    public void draw(Graphics g) {
        //g.setColor(Color.RED);
        //g.fillRect(x, y, width, height);

        g.drawImage(boatImage, x, y, width, height, null);
    }

    public void move() {
        x += dx;
        y += dy;

        if (y < 83) y = 83;
        if (y > 566) y = 566;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
            dy = -15;
        }
        if (key == KeyEvent.VK_DOWN) {
            dy = 15;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
