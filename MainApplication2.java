package Project3_6581147;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StartFrame(); // Open the start screen
        });
    }
}

class StartFrame extends JFrame {
    public StartFrame() {
        setTitle("Enter to Start");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set a background panel
        setContentPane(new BackgroundPanel());

        // Add key listener to start the game on "Enter"
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    dispose(); // Close the start screen
                    new RescuePanel(); // Open the main game window
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
        setVisible(true);
    }
}


class BackgroundPanel extends JPanel {
    private Image background;

    public BackgroundPanel() {
        // Load the background image
        background = new ImageIcon("src/main/java/Project3_6581147/Assets/background.png").getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}
