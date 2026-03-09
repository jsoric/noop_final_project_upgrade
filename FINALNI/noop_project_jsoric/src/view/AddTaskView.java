package view;

import entities.Employee;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

/**
 * View used for creating a new task.
 */
public class AddTaskView extends JFrame {

    private final JTextField titleField = new JTextField();
    private final JTextArea descriptionArea = new JTextArea(4, 20);
    private final JComboBox<EmployeeItem> employeeComboBox = new JComboBox<>();
    private final JTextField deadlineField = new JTextField();

    private final JButton saveButton = new JButton("Save");
    private final JButton cancelButton = new JButton("Cancel");

    /**
     * Creates the add task view.
     *
     * @param parentFrame the parent frame used for centering the window
     */
    public AddTaskView(JFrame parentFrame) {
        setTitle("Add New Task");
        setSize(480, 330);
        setResizable(false);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    }

    /**
     * Initializes and arranges all UI components.
     */
    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        Dimension fieldSize = new Dimension(250, 28);
        Dimension areaSize = new Dimension(250, 80);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titleField.setPreferredSize(fieldSize);
        mainPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setPreferredSize(areaSize);
        mainPanel.add(descriptionScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(new JLabel("Deadline (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        deadlineField.setPreferredSize(fieldSize);
        mainPanel.add(deadlineField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Assign to:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        employeeComboBox.setPreferredSize(fieldSize);
        mainPanel.add(employeeComboBox, gbc);

        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        saveButton.setPreferredSize(new Dimension(90, 28));
        cancelButton.setPreferredSize(new Dimension(90, 28));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Populates the employee combo box.
     *
     * @param employees the list of employees available for assignment
     */
    public void setEmployees(List<Employee> employees) {
        employeeComboBox.removeAllItems();

        for (Employee employee : employees) {
            employeeComboBox.addItem(new EmployeeItem(employee));
        }
    }

    /**
     * Returns the entered task title.
     *
     * @return the task title
     */
    public String getTaskTitle() {
        return titleField.getText().trim();
    }

    /**
     * Returns the entered task description.
     *
     * @return the task description
     */
    public String getTaskDescription() {
        return descriptionArea.getText().trim();
    }

    /**
     * Returns the selected employee.
     *
     * @return the selected employee, or {@code null} if none is selected
     */
    public Employee getSelectedEmployee() {
        EmployeeItem item = (EmployeeItem) employeeComboBox.getSelectedItem();
        return item != null ? item.getEmployee() : null;
    }

    /**
     * Returns the save button.
     *
     * @return the save button
     */
    public JButton getSaveButton() {
        return saveButton;
    }

    /**
     * Returns the cancel button.
     *
     * @return the cancel button
     */
    public JButton getCancelButton() {
        return cancelButton;
    }

    /**
     * Returns the entered deadline text.
     *
     * @return the deadline text
     */
    public String getDeadlineText() {
        return deadlineField.getText().trim();
    }

    /**
     * Wrapper class used for displaying employees in the combo box.
     */
    private static class EmployeeItem {

        private final Employee employee;

        /**
         * Creates a new employee item.
         *
         * @param employee the employee to wrap
         */
        public EmployeeItem(Employee employee) {
            this.employee = employee;
        }

        /**
         * Returns the wrapped employee.
         *
         * @return the employee
         */
        public Employee getEmployee() {
            return employee;
        }

        @Override
        public String toString() {
            return employee.getName() + " " + employee.getSurname()
                    + " (" + employee.getEmail() + ")";
        }
    }
}