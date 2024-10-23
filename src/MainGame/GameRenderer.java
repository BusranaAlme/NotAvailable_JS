package MainGame;
import BoatObstacle.*;
import java.awt.*;

public class GameRenderer {
    private GameAssets assets;
    private GameUI ui;

    public GameRenderer(GameAssets assets, GameUI ui) {
        this.assets = assets;
        this.ui = ui;
    }

    public void drawMenu(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.drawString("Nouka Baich", 530, 100);

        g2d.setFont(new Font("Arial", Font.PLAIN, 24));

        drawButton(g2d, ui.getStartButton(), Color.GREEN, "Start");
        drawButton(g2d, ui.getInstructionsButton(), Color.BLUE, "Instructions");
        drawButton(g2d, ui.getCreditButton(), Color.MAGENTA, "Credits");
        drawButton(g2d, ui.getExitButton(), Color.RED, "Exit");
    }

    public void drawDifficultyMenu(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.drawString("Select Difficulty", 500, 200);

        g2d.setFont(new Font("Arial", Font.PLAIN, 24));

        drawButton(g2d, ui.getEasyButton(), Color.GREEN, "Easy");
        drawButton(g2d, ui.getMediumButton(), Color.ORANGE, "Medium");
        drawButton(g2d, ui.getHardButton(), Color.RED, "Hard");
    }

    private void drawButton(Graphics2D g2d, Rectangle button, Color color, String text) {
        g2d.setColor(Color.GRAY);
        g2d.fillRoundRect(button.x + 5, button.y + 5, button.width, button.height, 20, 20);
        g2d.setColor(color);
        g2d.fillRoundRect(button.x, button.y, button.width, button.height, 20, 20);
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, button.x + (button.width - g2d.getFontMetrics().stringWidth(text)) / 2,
                button.y + button.height - 15);
    }

    public void drawGame(Graphics g, Boat boat, java.util.ArrayList<Obstacle> obstacles, int score, boolean paused) {
        g.drawImage(assets.getGameBackground(), 0, 0, 1280, 720, null);

        boat.draw(g);
        for (Obstacle obs : obstacles) {
            obs.draw(g);
        }

        Color backgroundColor = new Color(0, 0, 0, 150);
        g.setColor(backgroundColor);
        g.fillRect(10, 10, 120, 45);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 40);

        if (paused) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Paused", 580, 360);
        }
    }

    public void drawGameOver(Graphics g, int score) {
        g.drawImage(assets.getGameBackground(), 0, 0, 1280, 720, null);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Game Over", 550, 200);

        Color backgroundColor = new Color(0, 0, 0, 200);
        g.setColor(backgroundColor);
        g.fillRect(575, 270, 130, 40);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 590, 300);

        drawButton((Graphics2D)g, ui.getPlayAgainButton(), Color.GREEN, "Play Again");
        drawButton((Graphics2D)g, ui.getMainMenuButton(), Color.BLUE, "Main Menu");
    }

    public void drawInstructions(Graphics g) {
        g.drawImage(assets.getInstructionBackground(), 0, 0, 1280, 720, null);

        g.setFont(new Font("Arial", Font.BOLD, 24));
        Color backgroundColor = new Color(0, 0, 0, 150);
        g.setColor(backgroundColor);
        g.fillRect(220, 170, 800, 300);

        g.setColor(Color.WHITE);
        g.drawString("INSTRUCTIONS:", 250, 240);
        g.drawString("1. Use the UP and DOWN arrow keys to move the boat.", 260, 290);
        g.drawString("2. Avoid hitting the obstacles.", 260, 340);
        g.drawString("3. Press ESC to pause the game.", 260, 390);

        drawButton((Graphics2D)g, ui.getBackButton(), Color.CYAN, "Back");
    }

    public void drawCredits(Graphics g) {
        Color backgroundColor = new Color(0, 0, 0, 150);
        g.setColor(backgroundColor);
        g.fillRect(450, 150, 400, 400);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Created By:", 540, 270);
        g.drawString("Md. Ahad Siddiki", 540, 320);
        g.drawString("MOST. Busrana Alme", 540, 370);
        g.drawString("Md. Kobir Hossain", 540, 420);

        drawButton((Graphics2D)g, ui.getBackButton(), Color.GREEN, "Back");
    }
}