package Final;

import javax.swing.*;
import java.awt.*;

public class MainApplication extends JFrame {
    private JButton startButton, creditsButton, quitButton;

    public MainApplication() {
        setTitle("Ambulance Maze Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load the background image
        ImageIcon backgroundImage = new ImageIcon("background.jpg"); // Replace with your image path
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new GridBagLayout()); // Use GridBagLayout to center the buttons

        // Main menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1, 10, 10)); // 3 rows, 1 column, with spacing
        menuPanel.setOpaque(false); // Make the panel transparent

        // Create buttons
        startButton = new JButton("Start Game");
        creditsButton = new JButton("Credits");
        quitButton = new JButton("Quit");

        // Style buttons
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        creditsButton.setFont(new Font("Arial", Font.BOLD, 24));
        quitButton.setFont(new Font("Arial", Font.BOLD, 24));

        // Add buttons to the menu panel
        menuPanel.add(startButton);
        menuPanel.add(creditsButton);
        menuPanel.add(quitButton);

        // Add the menu panel to the background label
        backgroundLabel.add(menuPanel);

        // Add the background label to the frame
        add(backgroundLabel, BorderLayout.CENTER);

        // Event handlers
        startButton.addActionListener(e -> startGame());
        creditsButton.addActionListener(e -> showCredits());
        quitButton.addActionListener(e -> quitGame());
    }

    private void startGame() {
        // Step 1: Ask for player name
        String playerName = JOptionPane.showInputDialog(this, "Enter your name:", "Player Name", JOptionPane.PLAIN_MESSAGE);

        // If the player cancels the name input, stop here
        if (playerName == null || playerName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty. Please try again.");
            return;
        }

        // Step 2: Start the game
        JOptionPane.showMessageDialog(this, 
            "Starting game for " + playerName + ".");
        // Logic to start the game will go here
    }

    private void showCredits() {
        JOptionPane.showMessageDialog(this, 
            "Credits:\n" +
            "John Doe (ID: 12345)\n" +
            "Jane Smith (ID: 67890)\n" +
            "Group Project 3 - Ambulance Maze Game");
    }

    private void quitGame() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to quit?", "Quit Game", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainApplication().setVisible(true);
        });
    }
}
