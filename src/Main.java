import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        // Create a JLabel for the title
        JLabel titleLabel = new JLabel("Nouka Baich", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24)); // Change to a stylish font

        // Create the game instance
        BoatGame game = new BoatGame();

        // Set the layout for the frame
        frame.setLayout(new BorderLayout());
        frame.add(titleLabel, BorderLayout.NORTH); // Add title at the top
        frame.add(game, BorderLayout.CENTER); // Add game in the center

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false); // Maximize button disabled
    }
}
