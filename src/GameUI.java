import java.awt.Rectangle;

public class GameUI {
    private Rectangle startButton;
    private Rectangle exitButton;
    private Rectangle playAgainButton;
    private Rectangle instructionsButton;
    private Rectangle backButton;
    private Rectangle mainMenuButton;
    private Rectangle creditButton;
    private Rectangle easyButton;
    private Rectangle mediumButton;
    private Rectangle hardButton;

    public GameUI() {
        initializeButtons();
    }

    private void initializeButtons() {
        startButton = new Rectangle(590, 200, 100, 50);
        instructionsButton = new Rectangle(565, 300, 150, 50);
        creditButton = new Rectangle(565, 400, 150, 50);
        exitButton = new Rectangle(590, 500, 100, 50);
        playAgainButton = new Rectangle(490, 400, 150, 50);
        mainMenuButton = new Rectangle(650, 400, 150, 50);
        backButton = new Rectangle(590, 600, 100, 50);
        easyButton = new Rectangle(560, 250, 150, 50);
        mediumButton = new Rectangle(560, 320, 150, 50);
        hardButton = new Rectangle(560, 390, 150, 50);
    }

    // Getters for all buttons
    public Rectangle getStartButton() { return startButton; }
    public Rectangle getExitButton() { return exitButton; }
    public Rectangle getPlayAgainButton() { return playAgainButton; }
    public Rectangle getInstructionsButton() { return instructionsButton; }
    public Rectangle getBackButton() { return backButton; }
    public Rectangle getMainMenuButton() { return mainMenuButton; }
    public Rectangle getCreditButton() { return creditButton; }
    public Rectangle getEasyButton() { return easyButton; }
    public Rectangle getMediumButton() { return mediumButton; }
    public Rectangle getHardButton() { return hardButton; }
}