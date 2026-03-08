package view.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Form panel in Admin view for adding new employees.
 */
public class AdminViewFormPanel extends JPanel {

    private JTextField emailField = new JTextField();
    private JTextField nameField = new JTextField();
    private JTextField surnameField = new JTextField();
    private JTextField phoneField = new JTextField();
    private JButton saveButton = new JButton("Add Employee");


    /**
     * Builds form UI (email, first name, last name, submit).
     */
    public AdminViewFormPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.gridy = 0;
        add(new JLabel("Email:"), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 8, 10, 8);
        add(emailField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 8, 5, 8);
        add(new JLabel("First Name:"), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 8, 10, 8);
        add(nameField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 8, 5, 8);
        add(new JLabel("Last Name:"), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 8, 15, 8);
        add(surnameField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 8, 5, 8);
        add(new JLabel("Phone:"), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 8, 15, 8);
        add(phoneField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(15, 8, 5, 8);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveButton, gbc);

    }

    /**
     * @return email text
     */
    public String getEmail() {
        return emailField.getText();
    }

    /**
     * @return first name text
     */
    public String getName() {
        return nameField.getText();
    }

    /**
     * @return surname text
     */
    public String getSurname() {
        return surnameField.getText();
    }
    /**
     * @return phone text
     */
    public String getPhone() {
        return phoneField.getText();
    }

    /**
     * @return save/add employee button
     */
    public JButton getSaveButton() {
        return saveButton;
    }
    public JTextField getEmailField() {
        return emailField;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JTextField getSurnameField() {
        return surnameField;
    }

    public JTextField getPhoneField() {
        return phoneField;
    }


}