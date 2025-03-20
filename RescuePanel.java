package Project3_6581147;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class RescuePanel extends JFrame {
    public RescuePanel() {
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
        new RescuePanel();
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {
    private int tileSize = 40; // Size of each grid tile
    private int rows = 19; // Number of rows in the grid
    private int cols = 25; // Number of columns in the grid
    private int vanX = 3*tileSize; // Van's starting X position
    private int vanY = 1*tileSize; // Van's starting Y position
    private int speed = 5; // Movement speed in pixels per update
    private char direction = ' '; // Van's current direction
    private Image vanImage;
    private Image doorImage;
    private Image tree;
    private Timer timer;
    private HashMap<Character, Image> tileImages;
    
    private Image[] patientImages = new Image[5];

    private int[] patientXarray = {13 * tileSize, 8 * tileSize, 21 * tileSize, 21 * tileSize, 3 * tileSize};
    private int[]patientYarray = {7 * tileSize, 16 * tileSize, 15 * tileSize, 2 * tileSize, 11 * tileSize};
    private boolean[] isPatientFollowingarray = new boolean[5];
    
    private int prevVanX, prevVanY;
    
    private boolean isPatientFollowing = false; // Tracks if patient follows the van
    private int followSpeed = 1; // Speed at which the patient moves
    
    private int patientX = 13 * tileSize; // Patient's X position (on grass area)
    private int patientY = 7 * tileSize; // Patient's Y position (on grass area)
    
    private int countdownTime = 120; // 2 minutes in seconds
    private JLabel timerLabel; // Label to display the timer
    private Timer countdownTimer; // Timer for the countdown

    // Tile map
    private String[] tileMap = {
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWGGGGGWWWWWWWWWWGGGGGWW",
    "WWWGGGGGWWWWWWWWWWGGGGGWW",
    "WWWGGGGGWWWWWWWWWWGGGGGWW",
    "WWWGGGGGWWWWWWGGGGGGWWWWW",
    "WWWWWWGGGGGGGGGGGGGGGGWWW",
    "WWWWWWWWWWGGGGGGGGGGGGGGW",
    "WWWWWWWWWWWWWGGGGGGGGGGGW",
    "WWWWGGGGWWWWWWWGGGGGGGGGW",
    "WWWGGGGGGWWWWWWGGGGGGGGGW",
    "WWGGGGGGGWWWWWWWWWWWGWWWW",
    "WWGGGGGGGGGWWWWGGGGGGGGWW",
    "WWGGGGGGGGGWWWWGGGGGGGGWW",
    "WWGGGGGGGGGGGGGGGGGGGGGWW",
    "WWWGGGGGGGGWWWWGGGGGGGGWW",
    "WWWWWGGGGGWWWWWGGGGGGGGWW",
    "WWWWWWGGGGWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW"
};

    public GamePanel() {
        // Load images
        tileImages = new HashMap<>();
        tileImages.put('W', new ImageIcon("src/main/java/Project3_6581147/Assets/water.png").getImage());
        tileImages.put('G', new ImageIcon("src/main/java/Project3_6581147/Assets/grass.png").getImage());
        //tileImages.put('q', new ImageIcon("src/main/java/Project3_6581147/Assets/grassq.png").getImage());
        tileImages.put(' ', null); // Empty space


        vanImage = new ImageIcon("src/main/java/Project3_6581147/Assets/van.png").getImage();

        /*patient1Image= new ImageIcon("src/main/java/Project3_6581147/Assets/patient1.png").getImage();
        patient2Image= new ImageIcon("src/main/java/Project3_6581147/Assets/patient2.png").getImage();
        patient3Image= new ImageIcon("src/main/java/Project3_6581147/Assets/patient3.png").getImage();
        patient4Image= new ImageIcon("src/main/java/Project3_6581147/Assets/patient4.png").getImage();
        patient5Image= new ImageIcon("src/main/java/Project3_6581147/Assets/patient5.png").getImage();*/
        
        patientImages[0] = new ImageIcon("src/main/java/Project3_6581147/Assets/patient1.png").getImage();
        patientImages[1] = new ImageIcon("src/main/java/Project3_6581147/Assets/patient2.png").getImage();
        patientImages[2] = new ImageIcon("src/main/java/Project3_6581147/Assets/patient3.png").getImage();
        patientImages[3] = new ImageIcon("src/main/java/Project3_6581147/Assets/patient4.png").getImage();
        patientImages[4] = new ImageIcon("src/main/java/Project3_6581147/Assets/patient5.png").getImage();
        tree= new ImageIcon("src/main/java/Project3_6581147/Assets/tree.png").getImage();

        setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
        setBackground(new Color(156,212,200));
        addKeyListener(this);
        addMouseListener(this); 
        setFocusable(true);
       
        timer = new Timer(50, this);
        timer.start();
        
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
                JOptionPane.showMessageDialog(this, "Time is up.\nSadly, you cannot help all of them. TT", "Bye Bye", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });
        countdownTimer.start();
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char tile = tileMap[i].charAt(j);
                Image tileImage = tileImages.get(tile);

                if (tileImage != null) {
                    g.drawImage(tileImage, j * tileSize, i * tileSize, tileSize, tileSize, this);
                }
            }
        }

        g.drawImage(vanImage, vanX, vanY, tileSize, tileSize, this);
        
        doorImage = new ImageIcon("src/main/java/Project3_6581147/Assets/Doors.png").getImage();
        g.drawImage(doorImage, 19*tileSize, 8*tileSize, tileSize, tileSize, this);
        
        /*g.drawImage(patient2Image, 8*tileSize, 16*tileSize, tileSize, tileSize,this);
        g.drawImage(patient3Image, 21*tileSize, 15*tileSize, tileSize, tileSize,this);
        g.drawImage(patient4Image, 21*tileSize, 2*tileSize, tileSize, tileSize,this);
        g.drawImage(patient5Image, 3*tileSize, 11*tileSize, tileSize, tileSize,this);*/
       
        for (int i = 0; i < 5; i++) {
            g.drawImage(patientImages[i], patientXarray[i], patientYarray[i], tileSize, tileSize, this);
        }

        
        g.drawImage(tree, 9*tileSize, 15*tileSize, tileSize, tileSize,this);
        g.drawImage(tree, 9*tileSize, 16*tileSize, tileSize, tileSize,this);
        g.drawImage(tree, 7*tileSize, 16*tileSize, tileSize, tileSize,this);
        g.drawImage(tree, 6*tileSize, 16*tileSize, tileSize, tileSize,this);
        g.drawImage(tree, 7*tileSize, 15*tileSize, tileSize, tileSize,this);
        g.drawImage(tree, 6*tileSize, 15*tileSize, tileSize, tileSize,this);
        g.drawImage(tree, 5*tileSize, 15*tileSize, tileSize, tileSize,this);
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
    
    int currentVanX = vanX;
    int currentVanY = vanY;

    // Update new position based on direction
    switch (direction) {
        case 'U': if (vanY > 0) newY -= speed; break;
        case 'D': if (vanY < rows * tileSize - tileSize) newY += speed; break;
        case 'L': if (vanX > 0) newX -= speed; break;
        case 'R': if (vanX < cols * tileSize - tileSize) newX += speed; break;
    }

    // Check tile type at new position before updating the van's position
    if (isOnGrass(newX, newY)) {
        vanX = newX;
        vanY = newY;
    }
    
    if (isPatientFollowing) {
        patientX = prevVanX;
        patientY = prevVanY;
    }

    // Update the previous van position for the next frame
    prevVanX = currentVanX;
    prevVanY = currentVanY;


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

    for (int i = 0; i < 5; i++) {
        boolean isVanNear = Math.abs(vanX - patientXarray[i]) <= tileSize && Math.abs(vanY - patientYarray[i]) <= tileSize;
        boolean clickedOnPatient = mouseX >= patientXarray[i] && mouseX < patientXarray[i] + tileSize &&
                                   mouseY >= patientYarray[i] && mouseY < patientYarray[i]+ tileSize;

        if (isVanNear && clickedOnPatient) {
            new TaskFrame();
            isPatientFollowingarray[i] = true;
            break; 
        }
    }
}


    // Unused MouseListener methods (required for implementation)
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
   
    
}
