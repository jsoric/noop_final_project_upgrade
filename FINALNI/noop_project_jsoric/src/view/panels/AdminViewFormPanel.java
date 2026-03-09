package view.panels;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Form panel in the admin view used for adding new employees and team leaders.
 */
public class AdminViewFormPanel extends JPanel {

    private JTextField emailField = new JTextField();
    private JTextField nameField = new JTextField();
    private JTextField surnameField = new JTextField();
    private JTextField phoneField = new JTextField();

    private JButton saveButton = new JButton("Add Employee");
    private JButton addTeamLeaderButton = new JButton("Add Team Leader");

    /**
     * Creates the form panel and initializes its UI components.
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
        gbc.insets = new Insets(0, 8, 10, 8);
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

        gbc.gridy++;
        gbc.insets = new Insets(5, 8, 5, 8);
        add(addTeamLeaderButton, gbc);
    }

    /**
     * Returns the text entered in the email field.
     *
     * @return the email text
     */
    public String getEmail() {
        return emailField.getText();
    }

    /**
     * Returns the text entered in the first name field.
     *
     * @return the first name text
     */
    public String getName() {
        return nameField.getText();
    }

    /**
     * Returns the text entered in the surname field.
     *
     * @return the surname text
     */
    public String getSurname() {
        return surnameField.getText();
    }

    /**
     * Returns the text entered in the phone field.
     *
     * @return the phone text
     */
    public String getPhone() {
        return phoneField.getText();
    }

    /**
     * Returns the add employee button.
     *
     * @return the add employee button
     */
    public JButton getSaveButton() {
        return saveButton;
    }

    /**
     * Returns the add team leader button.
     *
     * @return the add team leader button
     */
    public JButton getAddTeamLeaderButton() {
        return addTeamLeaderButton;
    }

    /**
     * Returns the email text field.
     *
     * @return the email field
     */
    public JTextField getEmailField() {
        return emailField;
    }

    /**
     * Returns the first name text field.
     *
     * @return the first name field
     */
    public JTextField getNameField() {
        return nameField;
    }

    /**
     * Returns the surname text field.
     *
     * @return the surname field
     */
    public JTextField getSurnameField() {
        return surnameField;
    }

    /**
     * Returns the phone text field.
     *
     * @return the phone field
     */
    public JTextField getPhoneField() {
        return phoneField;
    }
}