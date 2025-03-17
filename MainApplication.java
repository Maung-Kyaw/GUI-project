package Project3_6581147;

import javax.swing.*;
import javax.swing.*;
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
        setTitle("Vet Rescue- Start Screen");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // Add a key listener to start the game on "Enter"
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
