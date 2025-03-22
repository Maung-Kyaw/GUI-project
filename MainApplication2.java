package Project3_6581147;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StartFrame());
    }
}

class StartFrame extends JFrame {
    public StartFrame() {
        setTitle("Start Screen");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set a background panel
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel, BorderLayout.CENTER);

        // Create the button
        JButton startButton = new JButton(new ImageIcon("src/main/java/Project3_6581147/Assets/button.png"));
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);

        // Center the button
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(startButton, gbc);

        // Button action listener
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the start screen
                new RescuePanel(); // Open the main game window
            }
        });

        setVisible(true);
    }
}

class BackgroundPanel extends JPanel {
    private Image background;

    public BackgroundPanel() {
        //background = new ImageIcon("src/main/java/Project3_6581147/Assets/background.png").getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}
