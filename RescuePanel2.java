package Project3_6581147;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class MainApplication extends JFrame {
    public MainApplication() {
        setTitle("Vet Rescue");
        setSize(1000, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create and add the custom JPanel
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainApplication();
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {
    private int tileSize = 40; // Size of each grid tile
    private int rows = 19; // Number of rows in the grid
    private int cols = 25; // Number of columns in the grid
    private int vanX = 3 * tileSize; // Van's starting X position
    private int vanY = 1 * tileSize; // Van's starting Y position
    private int speed = 5; // Movement speed in pixels per update
    private char direction = ' '; // Van's current direction
    private Image vanImage;
    private Image patientImage;
    private Image doorImage;
    private Timer timer;
    private HashMap<Character, Image> tileImages;

    private boolean isPatientFollowing = false; // Tracks if patient follows the van
    private int followSpeed = 5; // Speed at which the patient moves

    private int patientX = 13 * tileSize; // Patient's X position (on grass area)
    private int patientY = 7 * tileSize; // Patient's Y position (on grass area)

    // Countdown timer variables
    private int countdownTime = 120; // 2 minutes in seconds
    private JLabel timerLabel; // Label to display the timer
    private Timer countdownTimer; // Timer for the countdown

    // Tile map
    private String[] tileMap = {
        "WWWWWWWWWWWWWWWWWWWWWWWWW",
        "WWWGGGGGWWWWWWWWWGGGGGGGW",
        "WWWGGGGGWWWWWWWGGGGGGGGWW",
        "WWWGGGGGWWWWWGGGGGGGGWWWW",
        "WWWGGGGGGGGGGGGGWWWWWWWWW",
        "WWWWWWWWWGGGGGWWWWWWWWWWW",
        "WWWWWWWWWWGGGGGGGGGWWWWWW",
        "WWWWWWWGGGGGGGGGGGGGGGWWW",
        "WWWWGGGGGWWWWWWWWWWGGGWWW",
        "WWWWGGGGGWWWWWWWWWWWWWWWW",
        "WWWWWWGGGGGGGGGWWWWWWWWWW",
        "WWWWWGGGGGGGGGGGWWWWWWWWW",
        "WWWWWGGGGGGGGGGGGGGWWWWWW",
        "GGGGGGGGWWWWWWGGGGGGGGGGG",
        "GGGGGGGGWWWWWWWWWWGGGGGGG",
        "WWWWWWWWWWWWWWWWWWGGGGGGG",
        "WWWWWWWWWWWWWWWWWWWWWWWWW",
        "WWWWWWWWWWWWWWWWWWWWWWWWW",
        "WWWWWWWWWWWWWWWWWWWWWWWWW"
    };

    public GamePanel() {
        // Load images
        tileImages = new HashMap<>();
        tileImages.put('W', new ImageIcon("src/main/java/Project3_6581147/water.png").getImage());
        tileImages.put('G', new ImageIcon("src/main/java/Project3_6581147/grass.png").getImage());
        tileImages.put(' ', null); // Empty space

        // Load van image
        vanImage = new ImageIcon("src/main/java/Project3_6581147/van.png").getImage();
        patientImage = new ImageIcon("src/main/java/Project3_6581147/patient.png").getImage();

        // Set up the panel
        setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
        setBackground(Color.BLACK);
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);

        // Initialize timer for smooth movement
        timer = new Timer(50, this); // Update every 50ms (20 FPS)
        timer.start();

        // Initialize countdown timer
        timerLabel = new JLabel("02:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerLabel.setForeground(Color.WHITE);
        add(timerLabel);

        countdownTimer = new Timer(1000, e -> {
            countdownTime--;
            if (countdownTime >= 0) {
                int minutes = countdownTime / 60;
                int seconds = countdownTime % 60;
                timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
            } else {
                countdownTimer.stop();
                JOptionPane.showMessageDialog(this, "Time's up! Game Over.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });
        countdownTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char tile = tileMap[i].charAt(j);
                Image tileImage = tileImages.get(tile);

                if (tileImage != null) {
                    g.drawImage(tileImage, j * tileSize, i * tileSize, tileSize, tileSize, this);
                } else {
                    g.setColor(Color.BLACK);
                }
            }
        }

        // Draw the van
        g.drawImage(vanImage, vanX, vanY, tileSize, tileSize, this);
        g.drawImage(patientImage, patientX, patientY, tileSize, tileSize, this);

        // Draw the door
        doorImage = new ImageIcon("src/main/java/Project3_6581147/Doors.png").getImage();
        g.drawImage(doorImage, 19 * tileSize, 8 * tileSize, tileSize, tileSize, this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Update direction based on key press
        if (key == KeyEvent.VK_W) direction = 'U'; // Up
        if (key == KeyEvent.VK_S) direction = 'D'; // Down
        if (key == KeyEvent.VK_A) direction = 'L'; // Left
        if (key == KeyEvent.VK_D) direction = 'R'; // Right
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // Stop movement when keys are released
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_S || key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
            direction = ' '; // Stop moving
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        int newX = vanX;
        int newY = vanY;

        // Update new position based on direction
        switch (direction) {
            case 'U': if (vanY > 0) newY -= speed; break;
            case 'D': if (vanY < rows * tileSize - tileSize) newY += speed; break;
            case 'L': if (vanX > 0) newX -= speed; break;
            case 'R': if (vanX < cols * tileSize - tileSize) newX += speed; break;
        }

        // Check if new position is on grass before updating
        if (isOnGrass(newX, newY)) {
            vanX = newX;
            vanY = newY;
        }

        // Check if van reaches the door
        int doorX = 19 * tileSize;
        int doorY = 8 * tileSize;

        if (Math.abs(vanX - doorX) < tileSize && Math.abs(vanY - doorY) < tileSize) {
            countdownTimer.stop();
            JOptionPane.showMessageDialog(this, "Congratulations! You finished the game!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        // Patient follows the van if following is enabled
        if (isPatientFollowing) {
            if (patientX < vanX) patientX += followSpeed;
            else if (patientX > vanX) patientX -= followSpeed;

            if (patientY < vanY) patientY += followSpeed;
            else if (patientY > vanY) patientY -= followSpeed;
        }

        repaint();
    }

    // Method to check if the van is on grass ('G')
    private boolean isOnGrass(int x, int y) {
        int leftCol = x / tileSize;
        int rightCol = (x + tileSize - 1) / tileSize;
        int topRow = y / tileSize;
        int bottomRow = (y + tileSize - 1) / tileSize;

        // Ensure all four corners of the van are on grass
        return isTileGrass(topRow, leftCol) &&
               isTileGrass(topRow, rightCol) &&
               isTileGrass(bottomRow, leftCol) &&
               isTileGrass(bottomRow, rightCol);
    }

    private boolean isTileGrass(int row, int col) {
        if (row >= 0 && row < tileMap.length && col >= 0 && col < tileMap[row].length()) {
            return tileMap[row].charAt(col) == 'G';
        }
        return false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        boolean isVanNear = Math.abs(vanX - patientX) <= tileSize && Math.abs(vanY - patientY) <= tileSize;
        boolean clickedOnPatient = mouseX >= patientX && mouseX < patientX + tileSize &&
                                   mouseY >= patientY && mouseY < patientY + tileSize;
        if (isVanNear && clickedOnPatient) {
            new TaskFrame();
        }
    }

    // Unused MouseListener methods (required for implementation)
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
