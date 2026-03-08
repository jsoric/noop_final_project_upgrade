package view.employee;

import entities.Employee;

import javax.swing.*;
import java.awt.*;

/**
 * GUI window that allows an employee to verify the correctness of their personal data.
 * <p>
 * This view displays read-only employee information (email, first name, last name)
 * and provides a single action button for accepting data accuracy.
 * </p>
 */
public class EmployeeView extends JFrame {

    /**
     * Employee whose data is displayed and verified.
     */
    private final Employee employee;

    /**
     * Button used by the employee to accept and confirm data accuracy.
     */
    private final JButton acceptBtn = new JButton("I accept data accuracy");

    /**
     * Creates the employee verification window.
     *
     * @param employee employee whose data will be displayed
     */
    public EmployeeView(Employee employee) {
        this.employee = employee;

        setTitle("Data Verification");
        setSize(450, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    }

    /**
     * Initializes the graphical user interface and layout.
     * <p>
     * Uses {@link GridBagLayout} to display employee data in a structured form.
     * All fields are non-editable to ensure data integrity.
     * </p>
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
     * Returns the accept button used for confirming data accuracy.
     *
     * @return accept button
     */
    public JButton getAcceptBtn() {
        return acceptBtn;
    }

    /**
     * Returns the employee associated with this view.
     *
     * @return employee being verified
     */
    public Employee getEmployee() {
        return employee;
    }
}