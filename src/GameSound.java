import javax.sound.sampled.*;
import java.io.*;

public class GameSound {
    public static void playCollisionSound() {
        try {
            InputStream audioInputStream = GameSound.class.getClassLoader().getResourceAsStream("assets/end.au");
            if (audioInputStream == null) {
                System.out.println("Audio file not found!");
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioInputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}