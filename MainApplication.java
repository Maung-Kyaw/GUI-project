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

        // Create buttons
        JButton startButton = new JButton(new ImageIcon("src/main/java/Project3_6581147/Assets/button.png"));
        JButton teamButton = new JButton("Team Members");
        JButton exitButton = new JButton("Exit");

        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        
        teamButton.setPreferredSize(new Dimension(150, 40));
        exitButton.setPreferredSize(new Dimension(150, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); 

        gbc.gridy = 0;
        backgroundPanel.add(startButton, gbc);

        gbc.gridy = 1;
        backgroundPanel.add(teamButton, gbc);

        gbc.gridy = 2;
        backgroundPanel.add(exitButton, gbc);

        startButton.addActionListener(e -> {
            dispose();
            new RescuePanel(); 
        });

        teamButton.addActionListener(e -> new TeamFrame()); 

        exitButton.addActionListener(e -> System.exit(0)); 

        setVisible(true);
    }
}

class TeamFrame extends JFrame {
    public TeamFrame() {
        setTitle("Team Members");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel member1 = new JLabel("6581147 Trinnaya Damrongpatharawat");
        JLabel member2 = new JLabel("6581053 Jinjutha Yolsirivat");
        JLabel member3 = new JLabel("6581178 Kyaw Zin Thant");
        JLabel member4 = new JLabel("6481227 Chartwut Piriyapanyaporn");
        JLabel member5 = new JLabel("66802511 Phurilap Kitlertpaisan");

        panel.add(member1);
        panel.add(member2);
        panel.add(member3);
        panel.add(member4);
        panel.add(member5);

        add(panel);
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
