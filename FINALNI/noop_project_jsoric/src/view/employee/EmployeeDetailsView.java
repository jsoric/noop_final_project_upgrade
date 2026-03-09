package view.employee;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

/**
 * View that displays employee details and assigned tasks.
 */
public class EmployeeDetailsView extends JFrame {

    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JLabel verifiedLabel;
    private JLabel teamLeaderLabel;

    private JTable tasksTable;
    private DefaultTableModel tableModel;

    private JButton sendLoginEmailBtn;
    private JButton changeStatusBtn;
    private JButton refreshBtn;
    private JButton closeBtn;

    /**
     * Creates the employee details view.
     */
    public EmployeeDetailsView() {
        setTitle("Employee Details");
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

        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
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

        infoPanel.add(new JLabel("Team Leader:"));
        teamLeaderLabel = new JLabel("-");
        infoPanel.add(teamLeaderLabel);

        add(infoPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Title", "Description", "Status", "Created At", "Updated At"};

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
        scrollPane.setBorder(BorderFactory.createTitledBorder("Assigned Tasks"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        sendLoginEmailBtn = new JButton("Send Login Email");
        changeStatusBtn = new JButton("Change Status");
        refreshBtn = new JButton("Refresh");
        closeBtn = new JButton("Close");

        buttonPanel.add(sendLoginEmailBtn);
        buttonPanel.add(changeStatusBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets the employee information displayed in the form.
     *
     * @param name the employee name
     * @param email the employee email
     * @param phone the employee phone number
     * @param verified the verification status text
     * @param teamLeader the team leader display text
     */
    public void setEmployeeData(String name, String email, String phone, String verified, String teamLeader) {
        nameLabel.setText(name);
        emailLabel.setText(email);
        phoneLabel.setText(phone);
        verifiedLabel.setText(verified);
        teamLeaderLabel.setText(teamLeader);
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
     * Returns the task table.
     *
     * @return the task table
     */
    public JTable getTasksTable() {
        return tasksTable;
    }

    /**
     * Returns the button for sending the login email.
     *
     * @return the send login email button
     */
    public JButton getSendLoginEmailBtn() {
        return sendLoginEmailBtn;
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
     * Returns the close button.
     *
     * @return the close button
     */
    public JButton getCloseBtn() {
        return closeBtn;
    }
}