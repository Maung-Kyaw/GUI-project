package project3_6581178;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskFrame extends JFrame implements ActionListener {
    private JTextField petNameField;
    private JCheckBox[] animalCheckBoxes;
    private JComboBox<String> sicknessComboBox;
    private JList<String> vetList;
    private JButton submitButton;
    private Image backgroundImage;
    private int patientIndex;
    private GamePanel parent;


    public TaskFrame(int index, GamePanel parentPanel) {
        this.patientIndex=index;
        this.parent=parentPanel;
        setTitle("Vet Diagnosis");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load the pixel background image
        backgroundImage = new ImageIcon("src/main/java/project3_6581178/Assets/taskframe_pixel.jpg").getImage();

        // Create a custom panel to draw the background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(new GridLayout(6, 1));
        setContentPane(backgroundPanel);

        // Add UI components
        petNameField = new JTextField("Enter Pet Name");
        backgroundPanel.add(petNameField);

        animalCheckBoxes = new JCheckBox[5];
        String[] animals = {"Cow", "Dog", "Bird", "Pig", "Chicken"};
        for (int i = 0; i < 5; i++) {
            animalCheckBoxes[i] = new JCheckBox(animals[i]);
            backgroundPanel.add(animalCheckBoxes[i]);
        }

        String[] sickness = {"Heart Condition", "Skin Infection", "Tooth Decay", "Ankle Dislocation", "Food Poisonous"};
        sicknessComboBox = new JComboBox<>(sickness);
        backgroundPanel.add(sicknessComboBox);

        String[] vets = {"Dr. Smith (General)", "Dr. Jones (Gastroenterology)", "Dr. Lee (Dentistry)", "Dr. Patel (Cardiology)", "Dr. Brown (Orthopedics)"};
        vetList = new JList<>(vets);
        backgroundPanel.add(new JScrollPane(vetList));

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        backgroundPanel.add(submitButton);

        setVisible(true);
    }

    @Override
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == submitButton) {
        String name = petNameField.getText();

        StringBuilder speciesBuilder = new StringBuilder();
        for (JCheckBox cb : animalCheckBoxes) {
            if (cb.isSelected()) {
                speciesBuilder.append(cb.getText()).append(" ");
            }
        }
        String selectedSpecies = speciesBuilder.toString().trim();

        String cause = (String) sicknessComboBox.getSelectedItem();
        String doctor = vetList.getSelectedValue();

        // Send answers to GamePanel
        parent.savePlayerInput(patientIndex, name, selectedSpecies, cause, doctor);
        parent.isPatientFollowingarray[patientIndex] = true;  // Patient starts following
        parent.isPatientRescued[patientIndex] = true;  // Mark as rescued


        JOptionPane.showMessageDialog(this, "Rescue Task Completed!");
        dispose();
    }
}

}
