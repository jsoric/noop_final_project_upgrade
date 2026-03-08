package view;

import entities.Employee;
import enums.TaskStatus;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Pure MVC view for creating a task.
 * This class only contains UI and exposes user input to the controller.
 */
public class AddTaskView extends JFrame {

    private final JTextField titleField = new JTextField();
    private final JTextArea descriptionArea = new JTextArea(6, 20);
    private final JComboBox<TaskStatus> statusComboBox = new JComboBox<>(TaskStatus.values());
    private final JComboBox<EmployeeItem> employeeComboBox = new JComboBox<>();

    private final JButton saveButton = new JButton("Save");
    private final JButton cancelButton = new JButton("Cancel");

    public AddTaskView(JFrame parentFrame) {
        setTitle("Add New Task");
        setSize(520, 420);
        setResizable(false);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        mainPanel.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        titleField.setPreferredSize(new Dimension(280, 30));
        mainPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        mainPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        mainPanel.add(descriptionScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(statusComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        mainPanel.add(new JLabel("Assign to:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(employeeComboBox, gbc);

        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setEmployees(List<Employee> employees) {
        employeeComboBox.removeAllItems();

        for (Employee employee : employees) {
            employeeComboBox.addItem(new EmployeeItem(employee));
        }
    }

    public String getTaskTitle() {
        return titleField.getText().trim();
    }

    public String getTaskDescription() {
        return descriptionArea.getText().trim();
    }

    public TaskStatus getSelectedStatus() {
        return (TaskStatus) statusComboBox.getSelectedItem();
    }

    public Employee getSelectedEmployee() {
        EmployeeItem item = (EmployeeItem) employeeComboBox.getSelectedItem();
        return item != null ? item.getEmployee() : null;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        statusComboBox.setSelectedIndex(0);
        if (employeeComboBox.getItemCount() > 0) {
            employeeComboBox.setSelectedIndex(0);
        }
    }

    private static class EmployeeItem {
        private final Employee employee;

        public EmployeeItem(Employee employee) {
            this.employee = employee;
        }

        public Employee getEmployee() {
            return employee;
        }

        @Override
        public String toString() {
            return employee.getName() + " " + employee.getSurname() +
                    " (" + employee.getEmail() + ")";
        }
    }
}