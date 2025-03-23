/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project3_6581178;

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
    
    
    // Correct answers
private final String[] correctNames = {"Chirpy", "Clucky", "MooMoo", "Barkster", "Oinky"};
private final String[] correctSpecies = {"Bird", "Chicken", "Cow", "Dog", "Pig"};
private final String[] correctSickness = {
    "Heart Condition",
    "Skin Infection",
    "Tooth Decay",
    "Ankle Dislocation",
    "Food Poisonous"
};
private final String[] correctDoctors = {
    "Dr. Patel (Cardiology)",
    "Dr. Smith (General Vet)",
    "Dr. Lee (Dentistry)",
    "Dr. Brown (Orthopedics)",
    "Dr. Jones (Gastroenterology)"
};

// Player inputs
private final String[] playerNames = new String[5];
private final String[] playerSpecies = new String[5];
private final String[] playerSickness = new String[5];
private final String[] playerDoctors = new String[5];

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
    public boolean[] isPatientRescued = new boolean[5]; // Track rescued patients
    private boolean isDialogueActive = false; // Track if dialogue is showing
    


    private Image dialogueImage; // Store the dialogue image
    private Image[] patientImages = new Image[5];

    private int[] patientXarray = {13 * tileSize, 8 * tileSize, 21 * tileSize, 21 * tileSize, 3 * tileSize};
    private int[]patientYarray = {7 * tileSize, 16 * tileSize, 15 * tileSize, 2 * tileSize, 11 * tileSize};
    public boolean[] isPatientFollowingarray = new boolean[5];
    private int prevVanX, prevVanY;
    
    private boolean isPatientFollowing = false;
    private int followSpeed = 1; 
    
    int doorX = 19 * tileSize;
    int doorY = 8 * tileSize;
    
    private int currentPatientIndex = -1;
    
    private int patientX = 13 * tileSize; 
    private int patientY = 7 * tileSize; 
    
    private int countdownTime = 120; 
    
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
    "WWWWWWWWWWWWWGGGGGGGGGGWW",
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
         menuImage = new ImageIcon("src/main/java/project3_6581178/Assets/menu.png").getImage();
        tileImages.put('W', new ImageIcon("src/main/java/project3_6581178/Assets/water.png").getImage());
        tileImages.put('G', new ImageIcon("src/main/java/project3_6581178/Assets/grass.png").getImage());
        //tileImages.put('q', new ImageIcon("src/main/java/project3_6581178/Assets/grassq.png").getImage());
        tileImages.put(' ', null); 
        
        vanImage = new ImageIcon("src/main/java/project3_6581178/Assets/van.png").getImage();
        
        patientImages[0] = new ImageIcon("src/main/java/project3_6581178/Assets/patient1.png").getImage();
        patientImages[1] = new ImageIcon("src/main/java/project3_6581178/Assets/patient2.png").getImage();
        patientImages[2] = new ImageIcon("src/main/java/project3_6581178/Assets/patient3.png").getImage();
        patientImages[3] = new ImageIcon("src/main/java/project3_6581178/Assets/patient4.png").getImage();
        patientImages[4] = new ImageIcon("src/main/java/project3_6581178/Assets/patient5.png").getImage();
        tree= new ImageIcon("src/main/java/project3_6581178/Assets/tree.png").getImage();
        dialogueImage = new ImageIcon("src/main/java/project3_6581178/Assets/dialogue.png").getImage();
        



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

            int imageWidth = width *4;
            int imageHeight = height *4;

            int centerX = (getWidth() - imageWidth) / 2;
            int centerY = (getHeight() - imageHeight) / 2;

            g.drawImage(menuImage, centerX, centerY, imageWidth, imageHeight, this);
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
        
        doorImage = new ImageIcon("src/main/java/project3_6581178/Assets/Doors.png").getImage();
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
        
        if (isDialogueActive && currentPatientIndex != -1) {
    int dialogWidth = 250;  // Set fixed width
    int dialogHeight = 100; // Set fixed height

    // Get patient coordinates
    int patientX = patientXarray[currentPatientIndex];
    int patientY = patientYarray[currentPatientIndex];

    int dialogX, dialogY;

    // Default position: Above the patient
    dialogX = patientX;
    dialogY = patientY - dialogHeight - 10;

    // If it goes off-screen at the top, place it below the patient
    if (dialogY < 0) {
        dialogY = patientY + tileSize + 10;
    }

    // If it goes off-screen at the right, move it to the left of the patient
    if (dialogX + dialogWidth > getWidth()) {
        dialogX = patientX - dialogWidth - 10;
    }

    // If it goes off-screen at the left, adjust slightly to the right
    if (dialogX < 0) {
        dialogX = 10;
    }

    // Draw the dialogue box at the calculated position
    g.drawImage(dialogueImage, dialogX, dialogY, dialogWidth, dialogHeight, this);

    // Display patient's message
    String message = "";
    switch (currentPatientIndex) {
        case 0: message = "I'm Chirpy...\nI feel dizzy!"; break;
        case 1: message = "I'm Clucky...\nI can't stop pecking!"; break;
        case 2: message = "I'm MooMoo...\nMy teeth hurt!"; break;
        case 3: message = "I'm Barkster...\nI hurt my leg!"; break;
        case 4: message = "I'm Oinky...\nMy tummy hurts!"; break;
    }

    // Split message into multiple lines
    String[] lines = message.split("\n");
    int lineHeight = 20;
    int textX = dialogX + 20;
    int textY = dialogY + 40;
    g.setColor(Color.BLACK);

    for (String line : lines) {
        g.drawString(line, textX, textY);
        textY += lineHeight;
    }
}


    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (!gameStarted && key == KeyEvent.VK_ENTER) {
            gameStarted = true;
            timer.start(); // Start game timer
            startCountdown(); // Start countdown
            repaint();
            return;
        }
        
       if (isDialogueActive && key == KeyEvent.VK_ENTER) {
    isDialogueActive = false;
   
    new TaskFrame(currentPatientIndex, this);         // ✅ Open TaskFrame with patient index
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
                JOptionPane.showMessageDialog(this, "Time is up.\nSadly, you cannot help all of them. TT", "Bye Bye", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
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

    switch (direction) {
        case 'U': newY -= speed; break;
        case 'D': newY += speed; break;
        case 'L': newX -= speed; break;
        case 'R': newX += speed; break;
    }

    if (isOnGrass(newX, newY)) {
        vanX = newX;
        vanY = newY;

        // Move all following patients to exactly match van position (overlapping)
        for (int i = 0; i < isPatientFollowingarray.length; i++) {
            if (isPatientFollowingarray[i]) {
                patientXarray[i] = vanX;  // Make patient match van's X position
                patientYarray[i] = vanY;  // Make patient match van's Y position
            }
        }
    }

    // Show dialogue if near a new patient
    boolean foundDialogue = false;
    for (int i = 0; i < patientXarray.length; i++) {
        if (isPatientRescued[i]) continue;  // Skip rescued patients

        boolean isVanNear = Math.abs(vanX - patientXarray[i]) < tileSize &&
                            Math.abs(vanY - patientYarray[i]) < tileSize;

        if (isVanNear) {
            currentPatientIndex = i;
            isDialogueActive = true;
            foundDialogue = true;
            break;
        }
    }

    if (!foundDialogue) {
        isDialogueActive = false;
        currentPatientIndex = -1;
    }

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

    for (int i = 0; i < patientXarray.length; i++) {
        if (isPatientRescued[i]) {
            continue; // Skip rescued patients
        }

        boolean clickedOnPatient = mouseX >= patientXarray[i] && mouseX < patientXarray[i] + tileSize &&
                                   mouseY >= patientYarray[i] && mouseY < patientYarray[i] + tileSize;

        if (clickedOnPatient) {
            new TaskFrame(currentPatientIndex, this);

            isPatientFollowingarray[i] = true; // Start following after task
            repaint();
            break;
        }
    }
}
public void savePlayerInput(int index, String name, String species, String cause, String doctor) {
    playerNames[index] = name;
    playerSpecies[index] = species;
    playerSickness[index] = cause;
    playerDoctors[index] = doctor;
}

private void checkWinCondition() {
    boolean allCorrect = true;
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < 5; i++) {
        boolean correct =
            playerNames[i] != null && playerNames[i].equalsIgnoreCase(correctNames[i]) &&
            playerSpecies[i] != null && playerSpecies[i].contains(correctSpecies[i]) &&
            playerSickness[i] != null && playerSickness[i].equals(correctSickness[i]) &&
            playerDoctors[i] != null && playerDoctors[i].equals(correctDoctors[i]);

        if (!correct) {
            allCorrect = false;
            result.append("❌ ").append(correctNames[i]).append(":\n")
                  .append("   Expected Doctor: ").append(correctDoctors[i]).append("\n")
                  .append("   You chose: ").append(playerDoctors[i]).append("\n\n");
        }
    }

    if (allCorrect) {
        JOptionPane.showMessageDialog(this, "🎉 All patients diagnosed correctly! You win!");
    } else {
        JOptionPane.showMessageDialog(this, "😢 Some mistakes were made:\n\n" + result.toString(), "Try Again", JOptionPane.INFORMATION_MESSAGE);
    }

    System.exit(0);
}




    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
   
    
}
