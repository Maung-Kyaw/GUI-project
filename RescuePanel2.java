package Project3_6581147;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;


public class RescuePanel extends JFrame {
    public RescuePanel() {
        setTitle("Farmland Rush");
        setSize(1000, 760); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new RescuePanel();
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {
    private int tileSize = 40; 
    private int rows = 19; 
    private int cols = 25; 
    private int vanX = 3*tileSize; 
    private int vanY = 1*tileSize; 
    private int speed = 5; 
    private char direction = ' '; 
    private Image vanImage;
    private Image doorImage;
    private Image tree;
    private Timer timer;
    private HashMap<Character, Image> tileImages;
    private boolean gameStarted = false; // Tracks if game has started
    private Image menuImage; // Holds menu.png
    private boolean[] isPatientRescued = new boolean[5]; // Track rescued patients
    protected static boolean isDialogueActive = false; // Track if dialogue is showing
    private Image dialogueImage; // Store the dialogue image
    private Image[] patientImages = new Image[5];

    private int[] patientXarray = {13 * tileSize, 8 * tileSize, 21 * tileSize, 21 * tileSize, 3 * tileSize};
    private int[]patientYarray = {7 * tileSize, 16 * tileSize, 15 * tileSize, 2 * tileSize, 11 * tileSize};
    private boolean[] isPatientFollowingarray = new boolean[5];
    private int prevVanX, prevVanY;
    
    private boolean isPatientFollowing = false;
    private int followSpeed = 1; 
    
    int doorX = 19 * tileSize;
    int doorY = 8 * tileSize;
    
    private int currentPatientIndex = -1;
    private static boolean[] isPatientCorrectlyTreated = new boolean[5];
    public static void setPatientTreated(int index, boolean isCorrect) {
        isPatientCorrectlyTreated[index] = isCorrect;
    }
    
    private int patientX = 13 * tileSize; 
    private int patientY = 7 * tileSize; 
    
    private int countdownTime = 30; 
    
    private JLabel timerLabel; 
    private Timer countdownTimer; 

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
    "WWGGGGGGGGGGGGGGGGGGGGGWW",
    "WWGGGGGGGGGGGGGGGGGGGGGWW",
    "WWWGGGGGGGGWWWWGGGGGGGGWW",
    "WWWWWGGGGGWWWWWGGGGGGGGWW",
    "WWWWWWGGGGWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWWWWWWWWWWWWWWWWWWWWWWW"
};

    public GamePanel() {
        tileImages = new HashMap<>();
         menuImage = new ImageIcon("src/main/java/Project3_6581147/Assets/menu.png").getImage();
        tileImages.put('W', new ImageIcon("src/main/java/Project3_6581147/Assets/water.png").getImage());
        tileImages.put('G', new ImageIcon("src/main/java/Project3_6581147/Assets/grass.png").getImage());
        //tileImages.put('q', new ImageIcon("src/main/java/Project3_6581147/Assets/grassq.png").getImage());
        tileImages.put(' ', null); 
        
        vanImage = new ImageIcon("src/main/java/Project3_6581147/Assets/van.png").getImage();
        
        patientImages[0] = new ImageIcon("src/main/java/Project3_6581147/Assets/patient1.png").getImage();
        patientImages[1] = new ImageIcon("src/main/java/Project3_6581147/Assets/patient2.png").getImage();
        patientImages[2] = new ImageIcon("src/main/java/Project3_6581147/Assets/patient3.png").getImage();
        patientImages[3] = new ImageIcon("src/main/java/Project3_6581147/Assets/patient4.png").getImage();
        patientImages[4] = new ImageIcon("src/main/java/Project3_6581147/Assets/patient5.png").getImage();
        tree= new ImageIcon("src/main/java/Project3_6581147/Assets/tree.png").getImage();
        dialogueImage = new ImageIcon("src/main/java/Project3_6581147/Assets/dialogue.png").getImage();


        setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
        setBackground(new Color(156,212,200));
        addKeyListener(this);
        addMouseListener(this); 
        setFocusable(true);
       
        timer = new Timer(50, this);
        
        timerLabel = new JLabel("02:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerLabel.setForeground(Color.WHITE);
        add(timerLabel);



    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
            if (!gameStarted) {
                int width = menuImage.getWidth(this);
                int height = menuImage.getHeight(this);

                int imageWidth = width * 4;
                int imageHeight = height * 4;

                int centerX = (getWidth() - imageWidth) / 2;
                int centerY = (getHeight() - imageHeight) / 2;

                g.drawImage(menuImage, centerX, centerY, imageWidth, imageHeight, this);

                // Set text properties
                g.setFont(new Font("Arial", Font.BOLD, 32));
                g.setColor(Color.WHITE);

                // Draw text on the menu screen
                String title = "Welcome to Farmland Rush!\n"
                        + "Your mission is to rescue the animals to the hospital within the time\n"
                        + "Are you ready?";
                String instruction = "Press ENTER to Start";

                int titleX = centerX + imageWidth / 2 - g.getFontMetrics().stringWidth(title) / 2;
                int titleY = centerY + 50;

                int instructionX = centerX + imageWidth / 2 - g.getFontMetrics().stringWidth(instruction) / 2;
                int instructionY = centerY + imageHeight - 50;

                g.drawString(title, titleX, titleY);
                g.drawString(instruction, instructionX, instructionY);

                return; 
            }



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
        
        if (isDialogueActive) {
            int dialogWidth = 300; 
            int dialogHeight = 150; 
            int x = (getWidth() - dialogWidth) / 2; 
            int y = (getHeight() - dialogHeight) / 2; 
            g.drawImage(dialogueImage, x, y, dialogWidth, dialogHeight, this);
            
            String message = "";
            if (currentPatientIndex == 1) {
                message = "Help me, please! \nI'm ";
            } else if (currentPatientIndex == 0) {
                message = "I'm Whiskers. \nI've been scratching for hours.";
            } else if (currentPatientIndex == 2) {
                message = "My leg hurts!";
            } else if (currentPatientIndex == 3) {
                message = "My teeth hurt!";
            } else {
                message = "Thank you for coming!";
            }
            String[] lines = message.split("\n");
            int lineHeight=20;
            int lineY=350;
            for (String line : lines) {
                g.drawString(line, 400, lineY);
                lineY += lineHeight; 
            }
        }
    }
    
private void calculateFinalScore() {
    int correctRescues = 0;
    for (int i = 0; i < 5; i++) {
        if (isPatientRescued[i] && isPatientCorrectlyTreated[i]) {
            correctRescues++;
        }
    }
    
    JOptionPane.showMessageDialog(null, "You have rescued " + correctRescues + " pets !");
}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (!gameStarted && key == KeyEvent.VK_ENTER) {
            gameStarted = true;
            timer.start();
            startCountdown(); 
            repaint();
            return;
        }
        
        if (isDialogueActive && key == KeyEvent.VK_ENTER) {
            TaskFrame taskFrame=new TaskFrame(currentPatientIndex);
            taskFrame.setLocation(950, 300);
            repaint();
    }

        if (key == KeyEvent.VK_W) direction = 'U'; 
        if (key == KeyEvent.VK_S) direction = 'D'; 
        if (key == KeyEvent.VK_A) direction = 'L'; 
        if (key == KeyEvent.VK_D) direction = 'R'; 
    }
    
    private void startCountdown() {
        countdownTimer = new Timer(1000, e -> {
            countdownTime--;
            if (countdownTime >= 0) {
                int minutes = countdownTime / 60;
                int seconds = countdownTime % 60;
                timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
            } else {
                countdownTimer.stop();
                calculateFinalScore();
                System.exit(0);
            }
            repaint();
        });
        countdownTimer.start();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_S || key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
            direction = ' '; 
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

    switch (direction) {
        case 'U': if (vanY > 0) newY -= speed; break;
        case 'D': if (vanY < rows * tileSize - tileSize) newY += speed; break;
        case 'L': if (vanX > 0) newX -= speed; break;
        case 'R': if (vanX < cols * tileSize - tileSize) newX += speed; break;
    }

    if (isOnGrass(newX, newY)) {
        vanX = newX;
        vanY = newY;
    }

    for (int i = 0; i < patientXarray.length; i++) {
        if (isPatientFollowingarray[i]) {
            patientXarray[i] = prevVanX;
            patientYarray[i] = prevVanY;

            if (Math.abs(patientXarray[i] - doorX) < tileSize && 
                Math.abs(patientYarray[i] - doorY) < tileSize && isDialogueActive == false) {

                JOptionPane.showMessageDialog(this, "This pet is in vet's undercare!");
                isPatientFollowingarray[i] = false;

                if (isPatientCorrectlyTreated[i]) {
                    isPatientRescued[i] = true;
                }
            }

        }
    }

    prevVanX = currentVanX;
    prevVanY = currentVanY;

    repaint();
}


private boolean isOnGrass(int x, int y) {
    int leftCol = x / tileSize;
    int rightCol = (x + tileSize - 1) / tileSize;
    int topRow = y / tileSize;
    int bottomRow = (y + tileSize - 1) / tileSize;

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

    // If a patient is already following, don't allow selecting another one
    for (boolean following : isPatientFollowingarray) {
        if (following) {
            return;
        }
    }

    // Check if a patient was clicked and start following
    for (int i = 0; i < patientXarray.length; i++) {
        if (isPatientRescued[i]) {
            continue; // Skip rescued patients
        }
        boolean isVanNear = Math.abs(vanX - patientXarray[i]) <= tileSize &&
                            Math.abs(vanY - patientYarray[i]) <= tileSize;
        boolean clickedOnPatient = mouseX >= patientXarray[i] && mouseX < patientXarray[i] + tileSize &&
                                   mouseY >= patientYarray[i] && mouseY < patientYarray[i] + tileSize;

        if (isVanNear && clickedOnPatient) {
            currentPatientIndex = i;
            isPatientFollowingarray[i] = true;
            isDialogueActive = true;
            repaint();
            break;
        }
    }
}

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
   
    
}
