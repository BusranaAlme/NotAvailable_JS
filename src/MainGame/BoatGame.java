package MainGame;
import BoatObstacle.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoatGame extends JPanel implements ActionListener {
    private GameAssets assets;
    private GameUI ui;
    private GameRenderer renderer;
    private GameLogic gameLogic;
    private Timer timer;
    private Boat boat;
    private boolean gameOver;
    private boolean paused;
    private GameState currentState;

    public BoatGame() {
        setPreferredSize(new Dimension(1280, 720));
        setBackground(Color.CYAN);

        assets = new GameAssets();
        ui = new GameUI();
        renderer = new GameRenderer(assets, ui);
        gameLogic = new GameLogic(1280, 720);

        initializeGame();
        setupInputHandlers();
    }

    private void initializeGame() {
        boat = new Boat();
        gameOver = false;
        paused = false;
        timer = new Timer(16, this);
        currentState = GameState.MENU;
    }

    private void setupInputHandlers() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
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

    private void handleMouseClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        switch (currentState) {
            case MENU:
                if (ui.getStartButton().contains(mouseX, mouseY)) {
                    currentState = GameState.DIFFICULTY_SELECTION;
                } else if (ui.getInstructionsButton().contains(mouseX, mouseY)) {
                    currentState = GameState.INSTRUCTIONS;
                } else if (ui.getCreditButton().contains(mouseX, mouseY)) {
                    currentState = GameState.CREDITS;
                } else if (ui.getExitButton().contains(mouseX, mouseY)) {
                    System.exit(0);
                }
                break;
            case DIFFICULTY_SELECTION:
                if (ui.getEasyButton().contains(mouseX, mouseY)) {
                    startGame(2);
                } else if (ui.getMediumButton().contains(mouseX, mouseY)) {
                    startGame(4);
                } else if (ui.getHardButton().contains(mouseX, mouseY)) {
                    startGame(6);
                }
                break;
            case GAME_OVER:
                if (ui.getPlayAgainButton().contains(mouseX, mouseY)) {
                    currentState = GameState.DIFFICULTY_SELECTION;
                } else if (ui.getMainMenuButton().contains(mouseX, mouseY)) {
                    currentState = GameState.MENU;
                }
                break;
            case INSTRUCTIONS:
            case CREDITS:
                if (ui.getBackButton().contains(mouseX, mouseY)) {
                    currentState = GameState.MENU;
                }
                break;
        }
        repaint();
    }

    private void handleKeyPress(KeyEvent e) {
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

    private void startGame(int speed) {
        boat = new Boat();
        gameOver = false;
        paused = false;
        gameLogic.initializeGame(boat, speed);
        timer.start();
        currentState = GameState.PLAYING;
    }

    private void pauseGame() {
        paused = true;
        timer.stop();
        currentState = GameState.PAUSED;
        repaint();
    }

    private void unpauseGame() {
        paused = false;
        timer.start();
        currentState = GameState.PLAYING;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (currentState) {
            case MENU:
                g.drawImage(assets.getMainMenuBackground(), 0, 0, getWidth(), getHeight(), this);
                renderer.drawMenu(g);
                break;
            case DIFFICULTY_SELECTION:
                g.drawImage(assets.getDifficultyBackground(), 0, 0, getWidth(), getHeight(), this);
                renderer.drawDifficultyMenu(g);
                break;
            case PLAYING:
            case PAUSED:
                renderer.drawGame(g, boat, gameLogic.getObstacles(), gameLogic.getScore(), paused);
                break;
            case GAME_OVER:
                renderer.drawGameOver(g, gameLogic.getScore());
                break;
            case INSTRUCTIONS:
                renderer.drawInstructions(g);
                break;
            case CREDITS:
                g.drawImage(assets.getCreditBackground(), 0, 0, getWidth(), getHeight(), this);
                renderer.drawCredits(g);
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentState == GameState.PLAYING && !paused) {
            gameLogic.updateGame();
            if (gameLogic.checkCollisions()) {
                gameOver = true;
                currentState = GameState.GAME_OVER;
                timer.stop();
                GameSound.playCollisionSound();
            }
            repaint();
        }
    }
}