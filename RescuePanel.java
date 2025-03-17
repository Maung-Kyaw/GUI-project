package Project3_6581147;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class RescuePanel extends JFrame {
    public RescuePanel() {
        setTitle("Vet Rescue");
        setSize(1000, 760); // Adjusted size for better visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create and add the custom JPanel
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new RescuePanel();
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {
    private int tileSize = 40; // Size of each grid tile
    private int rows = 19; // Number of rows in the grid
    private int cols = 25; // Number of columns in the grid
    private int vanX = 0; // Van's starting X position
    private int vanY = 0; // Van's starting Y position
    private int speed = 5; // Movement speed in pixels per update
    private char direction = ' '; // Van's current direction
    private Image vanImage;
    private Image patientImage;
    private Timer timer;
    private HashMap<Character, Image> tileImages;
    
    private int patientX = 13 * tileSize; // Patient's X position (on grass area)
    private int patientY = 7 * tileSize; // Patient's Y position (on grass area)

    // Tile map
    private String[] tileMap = {
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWWWWWWGGGGGGGGGGGGGGWWW",
    "WWWWWWWWWWGGGGGGGGGGGGGGW",
    "WWWWWWWWWWWWWWGGGGGGGGGGW",
    "WWWWWWWWWWWWWWWGGGGGGGGGW",
    "WWWWWWWWWWWWWWWGGGGGGGGGW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW"
};

    public GamePanel() {
        // Load images
        tileImages = new HashMap<>();
        tileImages.put('W', new ImageIcon("src/main/java/Project3_6581147/Assets/water.png").getImage());
        tileImages.put('G', new ImageIcon("src/main/java/Project3_6581147/Assets/grass.png").getImage());
        //tileImages.put('Q', new ImageIcon("src/main/java/Project3_6581147/Assets/grassq.png").getImage());
        tileImages.put(' ', null); // Empty space

        // Load van image
        vanImage = new ImageIcon("src/main/java/Project3_6581147/Assets/van.png").getImage();
        patientImage= new ImageIcon("src/main/java/Project3_6581147/Assets/patient.png").getImage();

        // Set up the panel
        setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        // Initialize timer for smooth movement
        timer = new Timer(50, this); // Update every 50ms (20 FPS)
        timer.start();
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
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * tileSize, i * tileSize, tileSize, tileSize);
                }
            }
        }

        // Draw the van
        g.drawImage(vanImage, vanX, vanY, tileSize, tileSize, this);
        g.drawImage(patientImage, patientX, patientY, tileSize, tileSize, this);

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
        // Update van position based on direction
        switch (direction) {
            case 'U': // Up
                if (vanY > 0) vanY -= speed;
                break;
            case 'D': // Down
                if (vanY < rows * tileSize - tileSize) vanY += speed;
                break;
            case 'L': // Left
                if (vanX > 0) vanX -= speed;
                break;
            case 'R': // Right
                if (vanX < cols * tileSize - tileSize) vanX += speed;
                break;
        }
        
        /*if (Math.abs(vanX - patientX) < tileSize && Math.abs(vanY - patientY) < tileSize) {
            new TaskFrame();
        }*/

        // Repaint the panel
        repaint();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
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
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    
}
