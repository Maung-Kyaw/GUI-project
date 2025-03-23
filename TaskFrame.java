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
    private int currentPatientIndex;

    private static final Map<Integer, String[]> correctAnswers = new HashMap<>();
    private static boolean[] isPatientCorrectlyTreated = new boolean[5]; // Track task completion

    static {
        correctAnswers.put(0, new String[]{"Whiskers", "Cow", "Forest", "Dr. Patel (Dermatology)"}); 
        correctAnswers.put(1, new String[]{"Bella", "Duck", "Hills", "Dr. Jones (Surgery)"}); 
        correctAnswers.put(2, new String[]{"Max", "Sheep", "Desert", "Dr. Brown (Orthopedics)"}); 
        correctAnswers.put(3, new String[]{"Coco", "Chick", "Chicken House", "Dr. Lee (Toxicology)"}); 
        correctAnswers.put(4, new String[]{"Rocky", "Pig", "Beach", "Dr. Smith (ER Vet)"}); 
    }

    public TaskFrame(int patientIndex) {
        this.currentPatientIndex = patientIndex;

        setTitle("Rescue Task");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1));

        petNameField = new JTextField("Enter Pet Name");
        add(petNameField);

        // Animal RadioButtons
        animalRadioButtons = new JRadioButton[5];
        String[] animals = {"Duck", "Cow", "Sheep", "Chick", "Pig"};
        ButtonGroup animalGroup = new ButtonGroup();

        for (int i = 0; i < 5; i++) {
            animalRadioButtons[i] = new JRadioButton(animals[i]);
            animalGroup.add(animalRadioButtons[i]);
            add(animalRadioButtons[i]);
        }

        // Location ComboBox
        String[] locations = {"Desert", "Chicken House", "Forest", "Beach", "Hills"};
        locationComboBox = new JComboBox<>(locations);
        add(locationComboBox);

        // Vet List
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

        // Retrieve correct answers from map
        String[] correct = correctAnswers.get(currentPatientIndex);
        if (correct != null 
            && enteredName.equalsIgnoreCase(correct[0]) 
            && selectedAnimal.equalsIgnoreCase(correct[1]) 
            && selectedLocation.equalsIgnoreCase(correct[2]) 
            && selectedVet.equalsIgnoreCase(correct[3])) {
            
            GamePanel.setPatientTreated(currentPatientIndex, true); // Mark as correctly treated
        }

        JOptionPane.showMessageDialog(this, "Let's take this pet to the vet!");
        GamePanel.isDialogueActive = false;
        dispose(); 
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

    private boolean checkCorrectAnswers(String name, String animal, String location, String vet) {
        String[] correct = correctAnswers.get(currentPatientIndex);
        return correct[0].equalsIgnoreCase(name) &&
               correct[1].equalsIgnoreCase(animal) &&
               correct[2].equalsIgnoreCase(location) &&
               correct[3].equalsIgnoreCase(vet);
    }
}
