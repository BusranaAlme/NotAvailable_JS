import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class BoatGame extends JPanel implements ActionListener {

    private Image mainMenuBackground;
    private Image creditBackground;
    private Image instructionBackground;
    private Image difficultyBackground;
    private Image gameBackground;

    private Timer timer;
    private Boat boat;
    private ArrayList<Obstacle> obstacles;
    private boolean gameOver;
    private boolean paused;
    private int score;
    private int obstacleSpeed; // Speed of obstacles, changes with difficulty

    // Game States
    private enum GameState { MENU, DIFFICULTY_SELECTION, PLAYING, GAME_OVER, PAUSED, INSTRUCTIONS, CREDITS };
    private GameState currentState;

    // Buttons
    private Rectangle startButton, exitButton, playAgainButton, instructionsButton, backButton, mainMenuButton;
    private Rectangle easyButton, mediumButton, hardButton; // Difficulty buttons
    private Rectangle creditButton; // Credit button
    public BoatGame() {
        setPreferredSize(new Dimension(1280, 720));  // Set window size to 1280x720
        setBackground(Color.CYAN);

        boat = new Boat();
        obstacles = new ArrayList<>();
        gameOver = false;
        score = 0;
        paused = false;
        obstacleSpeed = 2;  // Default speed for obstacles

        // Set the timer to 60 FPS (16ms delay between frames)
        timer = new Timer(16, this);

        // Initialize game state to menu
        currentState = GameState.MENU;

        // Create buttons
        startButton = new Rectangle(590, 200, 100, 50);  // Centered for 1280x720 screen
        instructionsButton = new Rectangle(565, 300, 150, 50);  // Make the button larger to emphasize Instructions
        creditButton = new Rectangle(565, 400, 150, 50);  // Credit button above exit
        exitButton = new Rectangle(590, 500, 100, 50);
        playAgainButton = new Rectangle(490, 400, 150, 50);
        mainMenuButton = new Rectangle(650, 400, 150, 50);  // New Main Menu button next to Play Again
        backButton = new Rectangle(590, 600, 100, 50);  // For returning to the menu from the instructions screen
        backButton = new Rectangle(590, 600, 100, 50);  // For returning to the menu from the instructions/credit screen

        mainMenuBackground = new ImageIcon(getClass().getResource("/assets/menuBG.png")).getImage();
        creditBackground = new ImageIcon(getClass().getResource("/assets/menuBG.png")).getImage();
        instructionBackground = new ImageIcon(getClass().getResource("/assets/menuBG.png")).getImage();
        difficultyBackground = new ImageIcon(getClass().getResource("/assets/menuBG.png")).getImage();
        gameBackground = new ImageIcon(getClass().getResource("/assets/mainBG.png")).getImage();

        // Vertical alignment for difficulty buttons with a small gap
        easyButton = new Rectangle(560, 250, 150, 50);
        mediumButton = new Rectangle(560, 320, 150, 50);
        hardButton = new Rectangle(560, 390, 150, 50);

        // Add mouse listener for button clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if (currentState == GameState.PLAYING && !gameOver) {
                    boat.keyPressed(e);
                    if (key == KeyEvent.VK_ESCAPE) {
                        pauseGame();
                    }
                } else if (currentState == GameState.PAUSED) {
                    if (key == KeyEvent.VK_ESCAPE) {
                        unpauseGame();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (currentState == GameState.PLAYING && !gameOver) {
                    boat.keyReleased(e);
                }
            }
        });

        setFocusable(true);
    }

    // Handle mouse clicks for button interactions
    private void handleMouseClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (currentState == GameState.MENU) {
            if (startButton.contains(mouseX, mouseY)) {
                currentState = GameState.DIFFICULTY_SELECTION;  // Move to difficulty selection screen
                repaint();
            } else if (instructionsButton.contains(mouseX, mouseY)) {
                currentState = GameState.INSTRUCTIONS;
                repaint();
            } else if (creditButton.contains(mouseX, mouseY)) {
                currentState = GameState.CREDITS;  // Move to credits screen
                repaint();
            } else if (exitButton.contains(mouseX, mouseY)) {
                System.exit(0);
            }
        } else if (currentState == GameState.DIFFICULTY_SELECTION) {
            if (easyButton.contains(mouseX, mouseY)) {
                startGame(2);  // Easy speed
            } else if (mediumButton.contains(mouseX, mouseY)) {
                startGame(4);  // Medium speed
            } else if (hardButton.contains(mouseX, mouseY)) {
                startGame(6);  // Hard speed
            }
        } else if (currentState == GameState.GAME_OVER) {
            if (playAgainButton.contains(mouseX, mouseY)) {
                currentState = GameState.DIFFICULTY_SELECTION;  // Go back to difficulty selection on game over
                repaint();
            } else if (mainMenuButton.contains(mouseX, mouseY)) {
                currentState = GameState.MENU;
                repaint();
            }
        } else if (currentState == GameState.INSTRUCTIONS || currentState == GameState.CREDITS) {
            if (backButton.contains(mouseX, mouseY)) {
                currentState = GameState.MENU;
                repaint();
            }
        }
    }

    // Start the game with a specific obstacle speed based on difficulty
    private void startGame(int speed) {
        boat = new Boat();
        obstacles.clear();
        gameOver = false;
        score = 0;
        paused = false;
        obstacleSpeed = speed;  // Set the obstacle speed based on difficulty
        timer.start();
        currentState = GameState.PLAYING;
    }

    // Pause the game
    private void pauseGame() {
        paused = true;
        timer.stop();
        currentState = GameState.PAUSED;
        repaint();
    }

    // Unpause the game
    private void unpauseGame() {
        paused = false;
        timer.start();
        currentState = GameState.PLAYING;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (currentState == GameState.MENU) {
            g.drawImage(mainMenuBackground, 0, 0, getWidth(), getHeight(), this); // Draw menu background
            drawMenu(g);
        } else if (currentState == GameState.DIFFICULTY_SELECTION) {
            g.drawImage(difficultyBackground, 0, 0, getWidth(), getHeight(), this); // Draw difficulty menu background
            drawDifficultyMenu(g);
        } else if (currentState == GameState.PLAYING || currentState == GameState.PAUSED) {
            g.drawImage(gameBackground, 0, 0, getWidth(), getHeight(), this); // Draw game background
            drawGame(g);
        } else if (currentState == GameState.GAME_OVER) {
            drawGameOver(g); // No background needed here, unless you want one
        } else if (currentState == GameState.INSTRUCTIONS) {
            g.drawImage(instructionBackground, 0, 0, getWidth(), getHeight(), this); // Draw instructions background
            drawInstructions(g);
        } else if (currentState == GameState.CREDITS) {
            g.drawImage(creditBackground, 0, 0, getWidth(), getHeight(), this); // Draw credits background
            drawCredits(g);
        }
    }


    // Draw the menu screen
    private void drawMenu(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  // For smooth edges

        // Set font for the title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.drawString("Nouka Baich", 530, 100);

        // Set common font for buttons
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));

        // Start Button with rounded corners and shadow
        g2d.setColor(Color.GRAY); // Shadow
        g2d.fillRoundRect(startButton.x + 5, startButton.y + 5, startButton.width, startButton.height, 20, 20);  // Shadow offset by 5px
        g2d.setColor(Color.GREEN); // Button color
        g2d.fillRoundRect(startButton.x, startButton.y, startButton.width, startButton.height, 20, 20);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Start", startButton.x + 20, startButton.y + 35);

        // Instructions Button with rounded corners and shadow
        g2d.setColor(Color.GRAY); // Shadow
        g2d.fillRoundRect(instructionsButton.x + 5, instructionsButton.y + 5, instructionsButton.width, instructionsButton.height, 20, 20);
        g2d.setColor(Color.BLUE); // Button color
        g2d.fillRoundRect(instructionsButton.x, instructionsButton.y, instructionsButton.width, instructionsButton.height, 20, 20);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Instructions", instructionsButton.x + 10, instructionsButton.y + 35);

        // Credits Button with rounded corners and shadow
        g2d.setColor(Color.GRAY); // Shadow
        g2d.fillRoundRect(creditButton.x + 5, creditButton.y + 5, creditButton.width, creditButton.height, 20, 20);
        g2d.setColor(Color.MAGENTA); // Button color
        g2d.fillRoundRect(creditButton.x, creditButton.y, creditButton.width, creditButton.height, 20, 20);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Credits", creditButton.x + 35, creditButton.y + 35);

        // Exit Button with rounded corners and shadow
        g2d.setColor(Color.GRAY); // Shadow
        g2d.fillRoundRect(exitButton.x + 5, exitButton.y + 5, exitButton.width, exitButton.height, 20, 20);
        g2d.setColor(Color.RED); // Button color
        g2d.fillRoundRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height, 20, 20);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Exit", exitButton.x + 25, exitButton.y + 35);
    }


    // Draw the credits screen
    private void drawCredits(Graphics g) {

        // Set the color for the rectangle background behind the text
        Color backgroundColor = new Color(0, 0, 0, 150); // Black with some transparency
        g.setColor(backgroundColor);

        // Draw the rectangle behind the text (adjust the coordinates and size as needed)
        g.fillRect(450, 150, 400, 400);  // x, y, width, height for the rectangle

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Created By:", 540, 270);
        g.drawString("Md. Ahad Siddiki", 540, 320);
        g.drawString("MOST. Busrana Alme", 540, 370);
        g.drawString("Md. Kobir Hossain", 540, 420);

        g.setColor(Color.GREEN);
        g.fillRect(backButton.x, backButton.y, backButton.width, backButton.height);
        g.setColor(Color.WHITE);
        g.drawString("Back", backButton.x + 25, backButton.y + 35);
    }

    // Draw the difficulty selection menu with vertical alignment
    private void drawDifficultyMenu(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  // For smooth edges

        // Set font for the title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.drawString("Select Difficulty", 500, 200);

        // Set common font for buttons
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));

        // Easy Button with rounded corners and shadow
        g2d.setColor(Color.GRAY); // Shadow
        g2d.fillRoundRect(easyButton.x + 5, easyButton.y + 5, easyButton.width, easyButton.height, 20, 20);  // Shadow offset by 5px
        g2d.setColor(Color.GREEN); // Button color
        g2d.fillRoundRect(easyButton.x, easyButton.y, easyButton.width, easyButton.height, 20, 20);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Easy", easyButton.x + 45, easyButton.y + 35);

        // Medium Button with rounded corners and shadow
        g2d.setColor(Color.GRAY); // Shadow
        g2d.fillRoundRect(mediumButton.x + 5, mediumButton.y + 5, mediumButton.width, mediumButton.height, 20, 20);
        g2d.setColor(Color.ORANGE); // Button color
        g2d.fillRoundRect(mediumButton.x, mediumButton.y, mediumButton.width, mediumButton.height, 20, 20);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Medium", mediumButton.x + 30, mediumButton.y + 35);

        // Hard Button with rounded corners and shadow
        g2d.setColor(Color.GRAY); // Shadow
        g2d.fillRoundRect(hardButton.x + 5, hardButton.y + 5, hardButton.width, hardButton.height, 20, 20);
        g2d.setColor(Color.RED); // Button color
        g2d.fillRoundRect(hardButton.x, hardButton.y, hardButton.width, hardButton.height, 20, 20);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Hard", hardButton.x + 45, hardButton.y + 35);
    }


    // Draw the game screen
    private void drawGame(Graphics g) {

        g.drawImage(gameBackground, 0, 0, getWidth(), getHeight(), this);
        // Draw boat and obstacles
        boat.draw(g);
        for (Obstacle obs : obstacles) {
            obs.draw(g);
        }

        // Draw score
        Color backgroundColor = new Color(0, 0, 0, 150); // Black with some transparency
        g.setColor(backgroundColor);     // Draw the rectangle behind the text (adjust the coordinates and size as needed)
        g.fillRect(10, 10, 120, 45);   // x, y, width, height for the rectangle
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 40);

        // Draw pause message
        if (paused) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Paused", 580, 360);
        }
    }

    // Draw game over screen
    private void drawGameOver(Graphics g) {

        g.drawImage(gameBackground, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Game Over", 550, 200);

        Color backgroundColor = new Color(0, 0, 0, 200); // Black with 150/255 transparency
        g.setColor(backgroundColor);
        // Draw the background rectangle for the text (adjust the coordinates and size as needed)
        g.fillRect(575, 270, 130, 40);  // x, y, width, height

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 590, 300);

        g.setColor(Color.GREEN);
        g.fillRect(playAgainButton.x, playAgainButton.y, playAgainButton.width, playAgainButton.height);
        g.setColor(Color.WHITE);
        g.drawString("Play Again", playAgainButton.x + 13, playAgainButton.y + 35);

        g.setColor(Color.BLUE);
        g.fillRect(mainMenuButton.x, mainMenuButton.y, mainMenuButton.width, mainMenuButton.height);
        g.setColor(Color.WHITE);
        g.drawString("Main Menu", mainMenuButton.x + 13, mainMenuButton.y + 35);
    }

    // Draw instructions screen
    private void drawInstructions(Graphics g) {
        // Draw the instruction background image
        g.drawImage(instructionBackground, 0, 0, getWidth(), getHeight(), this);

        // Set the font and color for the text
        g.setFont(new Font("Arial", Font.BOLD, 24));

        // Set the color for the background rectangle (e.g., a semi-transparent color)
        Color backgroundColor = new Color(0, 0, 0, 150); // Black with 150/255 transparency
        g.setColor(backgroundColor);

        // Draw the background rectangle for the text (adjust the coordinates and size as needed)
        g.fillRect(220, 170, 800, 300);  // x, y, width, height

        // Set the color for the text
        g.setColor(Color.WHITE);

        // Draw the instruction text on top of the background
        g.drawString("INSTRUCTIONS:", 250, 240);
        g.drawString("1. Use the UP and DOWN arrow keys to move the boat.", 260, 290);
        g.drawString("2. Avoid hitting the obstacles.", 260, 340);
        g.drawString("3. Press ESC to pause the game.", 260, 390);

        g.setColor(Color.CYAN);
        g.fillRect(backButton.x, backButton.y, backButton.width, backButton.height);
        g.setColor(Color.WHITE);
        g.drawString("Back", backButton.x + 20, backButton.y + 35);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentState == GameState.PLAYING && !paused) {
            boat.move();
            updateObstacles();
            checkCollisions();
            repaint();
        }
    }

    private void updateObstacles() {
        Random rand = new Random();

        // Generate obstacles with a 5% chance per frame
        if (rand.nextInt(100) < 5) {
            // Try to create a new obstacle that does not overlap with others
            boolean overlaps = true;
            Obstacle newObstacle = null;

            // Keep trying to create a non-overlapping obstacle
            while (overlaps) {
                int startY = rand.nextInt(getHeight() - 275) + 83;  // Ensure it spawns between the boundaries
                newObstacle = new Obstacle(getWidth(), startY);
                overlaps = false;  // Assume no overlap, then check

                for (Obstacle obs : obstacles) {
                    // Check if the new obstacle intersects with any existing obstacle
                    if (newObstacle.getBounds().intersects(obs.getBounds())) {
                        overlaps = true;  // Overlaps, try again
                        break;
                    }
                }
            }

            // If no overlap is found, add the obstacle
            obstacles.add(newObstacle);
        }

        // Move existing obstacles and remove them if they go off-screen
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obs = obstacles.get(i);
            obs.move(obstacleSpeed);  // Adjust obstacle speed based on difficulty

            if (obs.getX() < 0) {
                obstacles.remove(i);
                score++;
            }
        }
    }

    private void checkCollisions() {
        for (Obstacle obs : obstacles) {
            if (obs.getBounds().intersects(boat.getBounds())) {
                gameOver = true;
                currentState = GameState.GAME_OVER;
                timer.stop();
                playCollisionSound();
            }
        }
    }

    private void playCollisionSound() {
        try {
            // Load the sound file from resources
            InputStream audioInputStream = getClass().getClassLoader().getResourceAsStream("assets/end.au");
            if (audioInputStream == null) {
                System.out.println("Audio file not found!");
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioInputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start(); // Play the sound
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }



}
