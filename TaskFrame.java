package Project3_6581147;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class TaskFrame extends JFrame implements ActionListener {
    private JTextField petNameField;
    private JRadioButton[] animalRadioButtons;
    private JComboBox<String> locationComboBox;
    private JList<String> vetList;
    private JButton submitButton;
    private int currentPatientIndex;  // Tracks which patient this task frame is for
    private boolean[] isPatientRescued; // Array to track rescued patients
    private static int rescueScore = 0; // Tracks successful rescues

    // Define correct answers for each patient
    private static final Map<Integer, String[]> correctAnswers = new HashMap<>();

    static {
        correctAnswers.put(0, new String[]{"Whiskers", "Cow", "Forest", "Dr. Patel (Dermatology)"}); 
        correctAnswers.put(1, new String[]{"Bella", "Duck", "Hills", "Dr. Jones (Surgery)"}); 
        correctAnswers.put(2, new String[]{"Max", "Sheep", "Desert", "Dr. Brown (Orthopedics)"}); 
        correctAnswers.put(3, new String[]{"Coco", "Chick", "Chicken House", "Dr. Lee (Toxicology)"}); 
        correctAnswers.put(4, new String[]{"Rocky", "Pig", "Beach", "Dr. Smith (ER Vet)"}); 
    }

    public TaskFrame(int patientIndex, boolean[] isPatientRescued) {
        this.currentPatientIndex = patientIndex;
        this.isPatientRescued = isPatientRescued;
        
        setTitle("Rescue Task");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1));

        // Pet Name Field
        petNameField = new JTextField("Enter Pet Name");
        add(petNameField);

        // Animal RadioButtons (5 items)
        animalRadioButtons = new JRadioButton[5];
        String[] animals = {"Duck", "Cow", "Sheep", "Chick", "Pig"};
        ButtonGroup animalGroup = new ButtonGroup();

        for (int i = 0; i < 5; i++) {
            animalRadioButtons[i] = new JRadioButton(animals[i]);
            animalGroup.add(animalRadioButtons[i]);
            add(animalRadioButtons[i]);
        }

        // Location ComboBox (5 items)
        String[] locations = {"Desert", "Chicken House", "Forest", "Beach", "Hills"};
        locationComboBox = new JComboBox<>(locations);
        add(locationComboBox);

        // Vet List (5 items)
        String[] vets = {"Dr. Smith (ER Vet)", "Dr. Jones (Surgery)", "Dr. Lee (Toxicology)", "Dr. Patel (Dermatology)", "Dr. Brown (Orthopedics)"};
        vetList = new JList<>(vets);
        add(new JScrollPane(vetList));

        // Submit Button
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        add(submitButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            // Get user inputs
            String enteredName = petNameField.getText().trim();
            String selectedAnimal = getSelectedAnimal();
            String selectedLocation = (String) locationComboBox.getSelectedItem();
            String selectedVet = vetList.getSelectedValue();

            // Get correct answers for the current patient
            String[] correct = correctAnswers.get(currentPatientIndex);

            // Validate user inputs
            boolean isCorrect = enteredName.equalsIgnoreCase(correct[0]) &&
                                selectedAnimal.equals(correct[1]) &&
                                selectedLocation.equals(correct[2]) &&
                                selectedVet.equals(correct[3]);

            if (isCorrect) {
                JOptionPane.showMessageDialog(this, "Correct! Patient Rescued!");
                isPatientRescued[currentPatientIndex] = true;
                rescueScore++; // Increment score for successful rescue
            } else {
                JOptionPane.showMessageDialog(this, "Wrong answers! The patient was not rescued.");
                isPatientRescued[currentPatientIndex] = false;
            }

            dispose(); // Close task frame
        }
    }

    private String getSelectedAnimal() {
        for (JRadioButton button : animalRadioButtons) {
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return "";
    }

    public static int getRescueScore() {
        return rescueScore;
    }

    public static void showFinalScore(int totalPatients) {
        JOptionPane.showMessageDialog(null, "Game Over! You rescued " + rescueScore + " out of " + totalPatients + " patients.");
    }
}
