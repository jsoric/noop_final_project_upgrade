package view.employee;

import entities.Employee;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * View that allows an employee to verify their personal data.
 */
public class EmployeeVerificationView extends JFrame {

    private final Employee employee;
    private final JButton acceptBtn = new JButton("I accept data accuracy");

    /**
     * Creates the employee verification view.
     *
     * @param employee the employee whose data is displayed
     */
    public EmployeeVerificationView(Employee employee) {
        this.employee = employee;

        setTitle("Data Verification");
        setSize(450, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    }

    /**
     * Initializes and arranges all UI components.
     */
    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("Please confirm the accuracy of your data");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        gbc.gridy++;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        JTextField emailField = new JTextField(employee.getEmail());
        emailField.setEditable(false);
        emailField.setPreferredSize(new Dimension(250, 30));
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("First name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(employee.getName());
        nameField.setEditable(false);
        nameField.setPreferredSize(new Dimension(250, 30));
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Last name:"), gbc);
        gbc.gridx = 1;
        JTextField surnameField = new JTextField(employee.getSurname());
        surnameField.setEditable(false);
        surnameField.setPreferredSize(new Dimension(250, 30));
        panel.add(surnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        acceptBtn.setPreferredSize(new Dimension(220, 32));
        acceptBtn.setBackground(new Color(180, 230, 180));
        panel.add(acceptBtn, gbc);

        add(panel);
    }

    /**
     * Returns the accept button used to confirm data accuracy.
     *
     * @return the accept button
     */
    public JButton getAcceptBtn() {
        return acceptBtn;
    }

    /**
     * Returns the employee associated with this view.
     *
     * @return the employee being verified
     */
    public Employee getEmployee() {
        return employee;
    }
}