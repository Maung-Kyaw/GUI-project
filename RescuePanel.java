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
    Image vanImage;
    private Image doorImage;
    private Image woodenhouseImage;
    private Image flowerImage;
    private Image tree;
    private Timer timer;
    private Image waterImage;
    private Image decoImage;
    private HashMap<Character, Image> tileImages;
    private boolean gameStarted = false; 
    private Image menuImage; 
    private boolean[] isPatientRescued = new boolean[5]; 
    protected static boolean isDialogueActive = false; 
    private Image dialogueImage; 
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
    
    private int countdownTime = 180; 
    
    private JLabel timerLabel; 
    private Timer countdownTimer; 
    
     private String[] instructions = {
        "Welcome to Farmland Rush!",
        "",
        "",
        "Your mission is to take the",
        "injured farmland animals ",
        "to the vet within the time",
        "",
        "",
        "Are you ready?",
        "",
        "Press Enter to start"
    };

    private String[] tileMap = {
    "WWWWWWWWWWWWWWWWWWWWWWWWW",
    "WWWGGGGGWWWWWWWWWWGGGGGWW",
    "WWWGGGGGWWWWWWWWWWGGGGGWW",
    "WWWGGGGGWWWWWWWWWWGGGGGWW",
    "WWWGGGGGWWWWWWGGGGGGWWWWW",
    "WWWWWWGGGGGGGGGGGWWWWWGGW",
    "WWWWWWWWWWGGGGGGGWWWWWGGW",
    "WWWWWWWWWWWWWGGGGWWGWWGGW",
    "WWWWGGGGWWWWWWWGGWWGWWGGW",
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
        
        timerLabel = new JLabel("03:00", SwingConstants.CENTER);
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
                
                drawInstructions(g);

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
        
        waterImage= new ImageIcon("src/main/java/Project3_6581147/Assets/water.png").getImage();
        g.drawImage(waterImage, 20*tileSize, 10*tileSize, tileSize, tileSize, this);
        decoImage= new ImageIcon("src/main/java/Project3_6581147/Assets/bridge.png").getImage();
        g.drawImage(decoImage, 20*tileSize, 388, 32, 70, this);
        decoImage= new ImageIcon("src/main/java/Project3_6581147/Assets/fence8.png").getImage();
        g.drawImage(decoImage, 21*tileSize, 3*tileSize, tileSize, tileSize, this);
        decoImage= new ImageIcon("src/main/java/Project3_6581147/Assets/fence1.png").getImage();
        g.drawImage(decoImage, 22*tileSize, 3*tileSize, tileSize, tileSize, this);
        decoImage= new ImageIcon("src/main/java/Project3_6581147/Assets/fence2.png").getImage();
        g.drawImage(decoImage, 22*tileSize, 2*tileSize, tileSize, tileSize, this);
        decoImage= new ImageIcon("src/main/java/Project3_6581147/Assets/fence3.png").getImage();
        g.drawImage(decoImage, 22*tileSize, 1*tileSize, tileSize, tileSize, this);
        decoImage= new ImageIcon("src/main/java/Project3_6581147/Assets/fence4.png").getImage();
        g.drawImage(decoImage, 21*tileSize, 1*tileSize, tileSize, tileSize, this);
        g.drawImage(decoImage, 20*tileSize, 1*tileSize, tileSize, tileSize, this);
        g.drawImage(decoImage, 19*tileSize, 1*tileSize, tileSize, tileSize, this);
        decoImage= new ImageIcon("src/main/java/Project3_6581147/Assets/fence5.png").getImage();
        g.drawImage(decoImage, 18*tileSize, 1*tileSize, tileSize, tileSize, this);
        decoImage= new ImageIcon("src/main/java/Project3_6581147/Assets/fence6.png").getImage();
        g.drawImage(decoImage, 18*tileSize, 2*tileSize, tileSize, tileSize, this);
        decoImage= new ImageIcon("src/main/java/Project3_6581147/Assets/fence7.png").getImage();
        g.drawImage(decoImage, 18*tileSize, 3*tileSize, tileSize, tileSize, this);
        decoImage= new ImageIcon("src/main/java/Project3_6581147/Assets/Paths.png").getImage();
        g.drawImage(decoImage, 6*tileSize, 11*tileSize, tileSize, tileSize, this);
        g.drawImage(decoImage, 15*tileSize, 5*tileSize, tileSize, tileSize, this);
        decoImage= new ImageIcon("src/main/java/Project3_6581147/Assets/dirt.png").getImage();
        g.drawImage(decoImage, 17*tileSize, 13*tileSize, 120, 100, this);
        decoImage= new ImageIcon("src/main/java/Project3_6581147/Assets/chickenhouse.png").getImage();
        g.drawImage(decoImage, 3*tileSize, 13*tileSize, 90, 90, this);
        
        woodenhouseImage= new ImageIcon("src/main/java/Project3_6581147/Assets/woodencorner.png").getImage();
        g.drawImage(woodenhouseImage, 21*tileSize, 8*tileSize, tileSize, tileSize, this);
        woodenhouseImage= new ImageIcon("src/main/java/Project3_6581147/Assets/woodencorner1.png").getImage();
        g.drawImage(woodenhouseImage, 17*tileSize, 8*tileSize, tileSize, tileSize, this);
        woodenhouseImage= new ImageIcon("src/main/java/Project3_6581147/Assets/wooden3.png").getImage();
        g.drawImage(woodenhouseImage, 21*tileSize, 7*tileSize, tileSize, tileSize, this);
        g.drawImage(woodenhouseImage, 21*tileSize, 6*tileSize, tileSize, tileSize, this);
        woodenhouseImage= new ImageIcon("src/main/java/Project3_6581147/Assets/woodencorner2.png").getImage();
        g.drawImage(woodenhouseImage, 21*tileSize, 5*tileSize, tileSize, tileSize, this);
        woodenhouseImage= new ImageIcon("src/main/java/Project3_6581147/Assets/wooden4.png").getImage();
        g.drawImage(woodenhouseImage, 20*tileSize, 5*tileSize, tileSize, tileSize, this);
        g.drawImage(woodenhouseImage, 19*tileSize, 5*tileSize, tileSize, tileSize, this);
        g.drawImage(woodenhouseImage, 18*tileSize, 5*tileSize, tileSize, tileSize, this);
        woodenhouseImage= new ImageIcon("src/main/java/Project3_6581147/Assets/woodencorner3.png").getImage();
        g.drawImage(woodenhouseImage, 17*tileSize, 5*tileSize, tileSize, tileSize, this);
        woodenhouseImage= new ImageIcon("src/main/java/Project3_6581147/Assets/wooden5.png").getImage();
        g.drawImage(woodenhouseImage, 17*tileSize, 6*tileSize, tileSize, tileSize, this);
        g.drawImage(woodenhouseImage, 17*tileSize, 7*tileSize, tileSize, tileSize, this);
        woodenhouseImage= new ImageIcon("src/main/java/Project3_6581147/Assets/floor.png").getImage();
        g.drawImage(woodenhouseImage, 18*tileSize, 7*tileSize, tileSize, tileSize, this);
        g.drawImage(woodenhouseImage, 19*tileSize, 7*tileSize, tileSize, tileSize, this);
        g.drawImage(woodenhouseImage, 20*tileSize, 7*tileSize, tileSize, tileSize, this);
        g.drawImage(woodenhouseImage, 18*tileSize, 6*tileSize, tileSize, tileSize, this);
        g.drawImage(woodenhouseImage, 19*tileSize, 6*tileSize, tileSize, tileSize, this);
        g.drawImage(woodenhouseImage, 20*tileSize, 6*tileSize, tileSize, tileSize, this);
        g.drawImage(woodenhouseImage, 20*tileSize, 8*tileSize, tileSize, tileSize, this);
        g.drawImage(woodenhouseImage, 19*tileSize, 8*tileSize, tileSize, tileSize, this);
        g.drawImage(woodenhouseImage, 18*tileSize, 8*tileSize, tileSize, tileSize, this);
        
        doorImage = new ImageIcon("src/main/java/Project3_6581147/Assets/Doors.png").getImage();
        g.drawImage(doorImage, 19*tileSize, 8*tileSize, tileSize, tileSize, this);
        
        woodenhouseImage= new ImageIcon("src/main/java/Project3_6581147/Assets/woodenwindow.png").getImage();
        g.drawImage(woodenhouseImage, 18*tileSize, 8*tileSize, tileSize, tileSize, this);
        g.drawImage(woodenhouseImage, 20*tileSize, 8*tileSize, tileSize, tileSize, this);
        
        flowerImage= new ImageIcon("src/main/java/Project3_6581147/Assets/sunflower.png").getImage();
        g.drawImage(flowerImage, 4*tileSize, 10*tileSize, 30, 60,this);
        g.drawImage(flowerImage, 7*tileSize, 2*tileSize, 30, 60,this);
        g.drawImage(flowerImage, 15*tileSize, 11*tileSize, 30, 60,this);
        g.drawImage(flowerImage, 23*tileSize, 5*tileSize, 30, 60,this);
        flowerImage= new ImageIcon("src/main/java/Project3_6581147/Assets/rock.png").getImage();
        g.drawImage(flowerImage, 13*tileSize, 10*tileSize,30,20,this);
        g.drawImage(flowerImage, 2*tileSize, 5*tileSize,30,20,this);
        flowerImage= new ImageIcon("src/main/java/Project3_6581147/Assets/lotus.png").getImage();
        g.drawImage(flowerImage, 3*tileSize, 6*tileSize, 30, 30,this);
        g.drawImage(flowerImage, 14*tileSize, 2*tileSize, 30, 30,this);
        g.drawImage(flowerImage, 20*tileSize, 17*tileSize, 30, 30,this);
        flowerImage= new ImageIcon("src/main/java/Project3_6581147/Assets/plant1.png").getImage();
        g.drawImage(flowerImage, 19*tileSize, 14*tileSize,26, 26,this);
        g.drawImage(flowerImage, 17*tileSize, 14*tileSize,26, 26,this);
        flowerImage= new ImageIcon("src/main/java/Project3_6581147/Assets/plant2.png").getImage();
        g.drawImage(flowerImage, 19*tileSize, 13*tileSize,26, 26,this);
        g.drawImage(flowerImage, 18*tileSize, 13*tileSize,26, 26,this);
        flowerImage= new ImageIcon("src/main/java/Project3_6581147/Assets/m.png").getImage();
        g.drawImage(flowerImage, 22*tileSize, 14*tileSize,28, 28,this);
        g.drawImage(flowerImage, 5*tileSize, 8*tileSize,28, 28,this);
        flowerImage= new ImageIcon("src/main/java/Project3_6581147/Assets/flower.png").getImage();
        g.drawImage(flowerImage, 8*tileSize, 9*tileSize,25, 25,this);
        g.drawImage(flowerImage, 5*tileSize, 15*tileSize,25, 25,this);
        g.drawImage(flowerImage, 22*tileSize, 7*tileSize,25, 25,this);
        flowerImage= new ImageIcon("src/main/java/Project3_6581147/Assets/flower2.png").getImage();
        g.drawImage(flowerImage, 7*tileSize, 10*tileSize,25, 25,this);
        g.drawImage(flowerImage, 3*tileSize, 4*tileSize,25, 25,this);
        flowerImage= new ImageIcon("src/main/java/Project3_6581147/Assets/flower3.png").getImage();
        g.drawImage(flowerImage, 10*tileSize, 6*tileSize,25, 25,this);
        g.drawImage(flowerImage, 6*tileSize, 16*tileSize,25, 25,this);
        
        tree= new ImageIcon("src/main/java/Project3_6581147/Assets/tree.png").getImage();
        g.drawImage(tree, 9*tileSize, 15*tileSize, 60, 60,this);
        g.drawImage(tree, 6*tileSize, 14*tileSize, 60, 60,this);
        tree= new ImageIcon("src/main/java/Project3_6581147/Assets/apple.png").getImage();
        g.drawImage(tree, 8*tileSize, 10*tileSize, 60, 60,this);
        g.drawImage(tree, 4*tileSize, 7*tileSize, 60, 60,this);
        g.drawImage(tree, 22*tileSize, 12*tileSize, 60, 60,this);
        
        for (int i = 0; i < 5; i++) {
            g.drawImage(patientImages[i], patientXarray[i], patientYarray[i], tileSize, tileSize, this);
        }
        
        g.drawImage(vanImage, vanX, vanY, tileSize, tileSize, this);
        
        if (isDialogueActive) {
            int dialogWidth = 300; 
            int dialogHeight = 150; 
            int patientX = patientXarray[currentPatientIndex];
            int patientY = patientYarray[currentPatientIndex];


    int dialogX = patientX + 30;  
    int dialogY = patientY - 50;  
     if (dialogY < 0) {
        dialogY = patientY + tileSize + 10;
    }

    if (dialogX + dialogWidth > getWidth()) {
        dialogX = patientX - dialogWidth - 10;
    }

    if (dialogX < 0) {
        dialogX = 10;
    }

    g.drawImage(dialogueImage, dialogX, dialogY, dialogWidth, dialogHeight, this);

            String message = "";
            if (currentPatientIndex == 1) {
                message = "Help me, please! \nI'm Chirpy.\nMy teeth hurt when I chew.";
            } else if (currentPatientIndex == 0) {
                message = "I'm Whiskers. \nI've been scratching for hours.";
            } else if (currentPatientIndex == 2) {
                message = "I'm Oinky.\nMy leg hurts!\nMight be from jumping the fence.";
            } else if (currentPatientIndex == 3) {
                message = "I'm Coco.\nMy tummy hurts from bad food!";
            } else {
                message = "Hello, I'm Clucky.\nI feel dizzy when I flap my wings!";
            }
            String[] lines = message.split("\n");
            int lineHeight=20;
            int textX = dialogX + 35;
            int textY = dialogY + 55;
             g.setColor(Color.BLACK);

            for (String line : lines) {
                 g.drawString(line, textX, textY);
                 textY += lineHeight;
            }
        }
    }
    
    private void drawInstructions(Graphics g) {

        //g.setColor(new Color(163,123,94)); 
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20)); 

        int boxX = 250;
        int boxY = 300; 
        int boxWidth = 500; 
        int boxHeight = 150; 

        FontMetrics fm = g.getFontMetrics();
        int lineHeight = fm.getHeight();

        int totalTextHeight = instructions.length * lineHeight;
        int textY = boxY + (boxHeight - totalTextHeight) / 2 + fm.getAscent();

        for (String line : instructions) {
            int lineWidth = fm.stringWidth(line);
            int textX = boxX + (boxWidth - lineWidth) / 2;
            g.drawString(line, textX, textY);
            textY += lineHeight;
        }
}
    
private void calculateFinalScore() {
    int correctRescues = 0;
    for (int i = 0; i < 5; i++) {
        if (isPatientRescued[i] && isPatientCorrectlyTreated[i]) {
            correctRescues++;
        }
    }
        if (correctRescues==5){
            JOptionPane.showMessageDialog(null, "You win! You have helped all of them");
        }
        else {
            JOptionPane.showMessageDialog(null, "You cannot help all of them. You have rescued " + correctRescues + " pets !");
        }
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

            if (Math.abs(vanX - doorX) < tileSize && 
                Math.abs(vanY - doorY) < tileSize && isDialogueActive == false) {
                //JOptionPane.showMessageDialog(this, "This pet is in vet's undercare!");
                    
                isPatientFollowingarray[i] = false;
                isPatientRescued[i] = true;
                
                if(i==0){
                    patientXarray[i]=19*tileSize;
                    patientYarray[i]=7*tileSize;
                }
                else if(i==1){
                    patientXarray[i]=20*tileSize;
                    patientYarray[i]=7*tileSize;
                }
                else if(i==2){
                    patientXarray[i]=18*tileSize;
                    patientYarray[i]=7*tileSize;
                }
                else if(i==3){
                    patientXarray[i]=18*tileSize;
                    patientYarray[i]=6*tileSize;
                }
                else if(i==4){
                    patientXarray[i]=20*tileSize;
                    patientYarray[i]=6*tileSize;
                }
               
                if(isPatientRescued[0]==true&&isPatientRescued[1]==true&&isPatientRescued[2]==true
                        &&isPatientRescued[3]==true&&isPatientRescued[4]==true){
                    calculateFinalScore();
                    System.exit(0);
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

    for (int i = 0; i < patientXarray.length; i++) {
        if (isPatientFollowingarray[i]||isPatientRescued[i]) {
            continue; 
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
