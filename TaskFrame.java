package Project3_6581147;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskFrame extends JFrame implements ActionListener {
    private JTextField petNameField;
    private JCheckBox[] animalCheckBoxes;
    private JComboBox<String> locationComboBox;
    private JList<String> vetList;
    private JButton submitButton;

    public TaskFrame() {
        setTitle("Rescue Task");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1));

        // Pet Name Field
        petNameField = new JTextField("Enter Pet Name");
        add(petNameField);

        // Animal CheckBoxes (5 items)
        animalCheckBoxes = new JCheckBox[5];
        String[] animals = {"Dog", "Cat", "Bird", "Rabbit", "Hamster"};
        for (int i = 0; i < 5; i++) {
            animalCheckBoxes[i] = new JCheckBox(animals[i]);
            add(animalCheckBoxes[i]);
        }

        // Location ComboBox (5 items)
        String[] locations = {"Park", "Street", "Forest", "Beach", "Farm"};
        locationComboBox = new JComboBox<>(locations);
        add(locationComboBox);

        // Vet List (5 items)
        String[] vets = {"Dr. Smith (General)", "Dr. Jones (Surgery)", "Dr. Lee (Dentistry)", "Dr. Patel (Cardiology)", "Dr. Brown (Orthopedics)"};
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
            // Handle submission (e.g., save data, close frame)
            JOptionPane.showMessageDialog(this, "Rescue Task Completed!");
            dispose(); // Close the task frame
        }
    }
}
