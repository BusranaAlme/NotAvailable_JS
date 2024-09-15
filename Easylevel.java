import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Easylevel extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 1016;
    int boardHeight = 386;
    Image bgImg;
    Image boatImg;
    Image stoneImg;

    // Boat:
    int boatX = boardWidth / 4; // Fixed X position of the boat (1/4 from the left)
    int boatY = boardHeight / 2; // Starting Y position (center)
    int boatWidth = 50;
    int boatHeight = 50;

    class Boat {
        int x = boatX; // X remains constant, no horizontal movement
        int y = boatY; // Y changes with up/down keys
        int width = boatWidth;
        int height = boatHeight;
        Image img;

        Boat(Image img) {
            this.img = img;
        }
    }

    // Stones:
    int stoneWidth = 50;
    int stoneHeight = 50;

    class Stone {
        int x;
        int y;
        int width = stoneWidth;
        int height = stoneHeight;
        Image img;
        boolean passed = false;

        Stone(int x, int y, Image img) {
            this.x = x;
            this.y = y;
            this.img = img;
        }
    }

    // Game variables:
    Boat boat;
    int velocityY = 0; // Vertical velocity for up/down movement
    int velocityX = -4; // Stone velocity along the x-axis
    int gravity = 1;
    double score = 0;

    ArrayList<Stone> stones;
    Random random = new Random();
    Timer gameLoop;
    Timer placeStonesTimer;

    boolean gameOver = false;

    Easylevel() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        // Load images:
        bgImg = new ImageIcon(getClass().getResource("mainbg.png")).getImage();
        boatImg = new ImageIcon(getClass().getResource("boat1.png")).getImage();
        stoneImg = new ImageIcon(getClass().getResource("stone.png")).getImage();

        boat = new Boat(boatImg);
        stones = new ArrayList<>();

        // Place stones timer (for top and bottom stones)
        placeStonesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeStones();
            }
        });
        placeStonesTimer.start();

        // Game loop timer
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    // Place stones (Top and bottom stones)
    public void placeStones() {
        // Top stone
        Stone topStone = new Stone(boardWidth, 0, stoneImg);
        stones.add(topStone);

        // Bottom stone
        Stone bottomStone = new Stone(boardWidth, boardHeight - stoneHeight, stoneImg);
        stones.add(bottomStone);

        // Occasionally place a random stone in the middle
        if (random.nextInt(3) == 0) {
            int randomY = random.nextInt(boardHeight - 2 * stoneHeight) + stoneHeight;
            Stone randomStone = new Stone(boardWidth, randomY, stoneImg);
            stones.add(randomStone);
        }
    }

    // Drawing the game
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Draw background
        g.drawImage(bgImg, 0, 0, boardWidth, boardHeight, null);

        // Draw boat
        g.drawImage(boat.img, boat.x, boat.y, boat.width, boat.height, null);

        // Draw stones
        for (Stone stone : stones) {
            g.drawImage(stone.img, stone.x, stone.y, stone.width, stone.height, null);
        }

        // Draw score
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over!! " + (int) score, 10, 35);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    // Moving stones and checking for collisions
    public void move() {
        boat.y += velocityY; // Move the boat up and down
        boat.y = Math.max(0, Math.min(boat.y, boardHeight - boat.height)); // Keep boat within the screen

        // Move stones
        for (int i = 0; i < stones.size(); i++) {
            Stone stone = stones.get(i);
            stone.x += velocityX; // Move stones left

            // Check for collision
            if (collision(boat, stone)) {
                gameOver = true;
            }

            // Remove stones that go off-screen
            if (stone.x + stone.width < 0) {
                stones.remove(i);
                i--; // Adjust index after removal
            }

            // Increase score when boat passes a stone
            if (!stone.passed && boat.x > stone.x + stone.width) {
                stone.passed = true;
                score++; // Increment score
            }
        }

        if (gameOver) {
            gameLoop.stop();
            placeStonesTimer.stop();
        }
    }

    // Check for collision between boat and stones
    public boolean collision(Boat b, Stone s) {
        return b.x < s.x + s.width && b.x + b.width > s.x && b.y < s.y + s.height && b.y + b.height > s.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                velocityY = -5; // Move boat up
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                velocityY = 5; // Move boat down
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                velocityX = (velocityX == 0) ? -4 : 0; // Pause/unpause game (stones)
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                velocityX -= 2; // Speed up stones (acceleration)
            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // Restart game after game over
            resetGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
            velocityY = 0; // Stop vertical movement when key is released
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Reset game after game over
    public void resetGame() {
        boat.y = boatY; // Reset boat position
        velocityY = 0; // Reset velocity
        velocityX = -4; // Reset stone velocity
        stones.clear(); // Clear all stones
        score = 0; // Reset score
        gameOver = false; // Reset game state
        gameLoop.start(); // Restart game loop
        placeStonesTimer.start(); // Restart stone timer
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Easy Level");
        Easylevel gamePanel = new Easylevel();
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
