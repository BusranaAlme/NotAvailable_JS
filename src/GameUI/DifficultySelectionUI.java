package GameUI;
import level.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DifficultySelectionUI extends JFrame {

    public DifficultySelectionUI() {
        // Set the title of the window
        setTitle("Select Difficulty");

        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a custom JPanel with image background
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        // Create buttons with blue background and custom font
        JButton easyButton = new JButton("Easy");
        easyButton.setBackground(Color.BLUE);
        easyButton.setForeground(Color.WHITE);
        easyButton.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 36));

        JButton mediumButton = new JButton("Medium");
        mediumButton.setBackground(Color.BLUE);
        mediumButton.setForeground(Color.WHITE);
        mediumButton.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 36));

        JButton hardButton = new JButton("Hard");
        hardButton.setBackground(Color.BLUE);
        hardButton.setForeground(Color.WHITE);
        hardButton.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 36));

        // Create a GridBagConstraints instance
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.set(10, 10, 10, 10); // Set padding around components

        // Add buttons to the backgroundPanel with GridBagConstraints
        backgroundPanel.add(easyButton, gbc);

        gbc.gridy = 1; // Move to the next row
        backgroundPanel.add(mediumButton, gbc);

        gbc.gridy = 2; // Move to the next row
        backgroundPanel.add(hardButton, gbc);

        // Set the custom panel as the content pane
        setContentPane(backgroundPanel);

        // Set frame size
        setSize(675, 675);

        // Center the frame on the screen
        setLocationRelativeTo(null);

        // Add ActionListener to 'Easy' button to open Easylevel game
        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Dispose of the current frame
                dispose();

                // Create a new frame to host the game
                JFrame gameFrame = new JFrame("Flappy Bird - Easy Level");
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameFrame.add(new Easylevel());
                gameFrame.pack();
                gameFrame.setLocationRelativeTo(null); // Center the frame
                gameFrame.setVisible(true);
            }
        });
    }

    // Custom JPanel to handle the background image
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = ImageIO.read(getClass().getResource("/assets/fistview.png"));
            } catch (IOException e) {
                e.printStackTrace();
                // Handle exception or set a default background image
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        // Run the DifficultySelectionUI
        new DifficultySelectionUI().setVisible(true);
    }
}
