import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BoatGameUI extends JFrame {

    public BoatGameUI() {
        // Set the title of the window
        setTitle("Nouka Baich");

        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a custom JPanel with image background
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        // Create buttons with blue background and custom font
        JButton startButton = new JButton("Start");
        startButton.setBackground(Color.BLUE);
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 48));

        JButton settingButton = new JButton("Setting");
        settingButton.setBackground(Color.BLUE);
        settingButton.setForeground(Color.WHITE);
        settingButton.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 48));

        // Add action listener to the start button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the DifficultySelectionUI frame when the start button is clicked
                new DifficultySelectionUI().setVisible(true);
                // Close the current frame
                dispose();
            }
        });

        // Create a GridBagConstraints instance
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.set(50, 50, 50, 50);

        // Add buttons to the backgroundPanel with GridBagConstraints
        backgroundPanel.add(startButton, gbc);

        gbc.gridy = 1;
        backgroundPanel.add(settingButton, gbc);

        // Set the custom panel as the content pane
        setContentPane(backgroundPanel);

        // Set frame size
        setSize(1024, 768);

        // Center the frame on the screen
        setLocationRelativeTo(null);
    }

    // Custom JPanel to handle the background image
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = ImageIO.read(getClass().getResource("/frame2.png"));
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
}
