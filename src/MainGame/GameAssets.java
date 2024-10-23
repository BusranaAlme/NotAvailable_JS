package MainGame;
import BoatObstacle.*;
import java.awt.Image;
import javax.swing.ImageIcon;

public class GameAssets {
    private Image mainMenuBackground;
    private Image creditBackground;
    private Image instructionBackground;
    private Image difficultyBackground;
    private Image gameBackground;

    public GameAssets() {
        loadImages();
    }

    private void loadImages() {
        mainMenuBackground = new ImageIcon(getClass().getResource("/assets/menuBG.png")).getImage();
        creditBackground = new ImageIcon(getClass().getResource("/assets/menuBG.png")).getImage();
        instructionBackground = new ImageIcon(getClass().getResource("/assets/menuBG.png")).getImage();
        difficultyBackground = new ImageIcon(getClass().getResource("/assets/menuBG.png")).getImage();
        gameBackground = new ImageIcon(getClass().getResource("/assets/mainBG.png")).getImage();
    }

    public Image getMainMenuBackground() { return mainMenuBackground; }
    public Image getCreditBackground() { return creditBackground; }
    public Image getInstructionBackground() { return instructionBackground; }
    public Image getDifficultyBackground() { return difficultyBackground; }
    public Image getGameBackground() { return gameBackground; }
}
