package view.employee;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

/**
 * View that displays the employee task dashboard.
 */
public class EmployeeTaskView extends JFrame {

    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JLabel verifiedLabel;

    private JTable tasksTable;
    private DefaultTableModel tableModel;

    private JButton changeStatusBtn;
    private JButton refreshBtn;
    private JButton signOutBtn;

    /**
     * Creates the employee task dashboard view.
     */
    public EmployeeTaskView() {
        setTitle("My Tasks");
        setSize(900, 550);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    }

    /**
     * Initializes and arranges all UI components.
     */
    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Employee Information"));

        infoPanel.add(new JLabel("Name:"));
        nameLabel = new JLabel("-");
        infoPanel.add(nameLabel);

        infoPanel.add(new JLabel("Email:"));
        emailLabel = new JLabel("-");
        infoPanel.add(emailLabel);

        infoPanel.add(new JLabel("Phone:"));
        phoneLabel = new JLabel("-");
        infoPanel.add(phoneLabel);

        infoPanel.add(new JLabel("Verified:"));
        verifiedLabel = new JLabel("-");
        infoPanel.add(verifiedLabel);

        add(infoPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Title", "Description", "Status",
                "Deadline", "Created At", "Updated At"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tasksTable = new JTable(tableModel);
        tasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasksTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tasksTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("My Assigned Tasks"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        changeStatusBtn = new JButton("Change Status");
        refreshBtn = new JButton("Refresh");
        signOutBtn = new JButton("Sign Out");

        buttonPanel.add(changeStatusBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(signOutBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets the employee information displayed in the view.
     *
     * @param name the employee name
     * @param email the employee email
     * @param phone the employee phone number
     * @param verified the verification status text
     */
    public void setEmployeeInfo(String name, String email, String phone, String verified) {
        nameLabel.setText(name);
        emailLabel.setText(email);
        phoneLabel.setText(phone);
        verifiedLabel.setText(verified);
    }

    /**
     * Returns the task table.
     *
     * @return the task table
     */
    public JTable getTasksTable() {
        return tasksTable;
    }

    /**
     * Returns the table model used for the task table.
     *
     * @return the task table model
     */
    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    /**
     * Returns the button for changing task status.
     *
     * @return the change status button
     */
    public JButton getChangeStatusBtn() {
        return changeStatusBtn;
    }

    /**
     * Returns the refresh button.
     *
     * @return the refresh button
     */
    public JButton getRefreshBtn() {
        return refreshBtn;
    }

    /**
     * Returns the sign out button.
     *
     * @return the sign out button
     */
    public JButton getSignOutBtn() {
        return signOutBtn;
    }
}