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
    private static boolean[] isPatientCorrectlyTreated = new boolean[5]; 

    static {
        correctAnswers.put(0, new String[]{"Whiskers", "Cow", "Skin Infection", "Dr. Patel (Dermatology)"}); 
        correctAnswers.put(1, new String[]{"Chirpy", "Chick", "Tooth Decay", "Dr. Lee (Dentistry)"}); 
        correctAnswers.put(2, new String[]{"Oinky", "Pig", "Ankle Dislocation", "Dr. Brown (Orthopedics)"}); 
        correctAnswers.put(3, new String[]{"Coco", "Sheep", "Food Poisonous", "Dr. Jones (Toxicology)"}); 
        correctAnswers.put(4, new String[]{"Clucky", "Duck", "Heart Condition", "Dr. Smith (ER vet, Cardiology)"}); 
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
        String[] locations = {"Heart Condition", "Tooth Decay", "Skin Infection", "Food Poisonous", "Ankle Dislocation"};
        locationComboBox = new JComboBox<>(locations);
        add(locationComboBox);

        // Vet List
        String[] vets = {"Dr. Smith (ER vet, Cardiology)", "Dr. Jones (Toxicology)", "Dr. Lee (Dentistry)", "Dr. Patel (Dermatology)", "Dr. Brown (Orthopedics)"};
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
