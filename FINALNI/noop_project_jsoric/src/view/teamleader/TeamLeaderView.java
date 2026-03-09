package view.teamleader;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

/**
 * View that displays the team leader dashboard.
 */
public class TeamLeaderView extends JFrame {

    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JLabel statsLabel;

    private JTable employeesTable;
    private DefaultTableModel employeesTableModel;

    private JTable tasksTable;
    private DefaultTableModel tasksTableModel;

    private JButton refreshBtn;
    private JButton signOutBtn;

    /**
     * Creates the team leader dashboard view.
     */
    public TeamLeaderView() {
        setTitle("Team Leader Dashboard");
        setSize(1100, 650);
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
        infoPanel.setBorder(BorderFactory.createTitledBorder("Team Leader Information"));

        infoPanel.add(new JLabel("Name:"));
        nameLabel = new JLabel("-");
        infoPanel.add(nameLabel);

        infoPanel.add(new JLabel("Email:"));
        emailLabel = new JLabel("-");
        infoPanel.add(emailLabel);

        infoPanel.add(new JLabel("Phone:"));
        phoneLabel = new JLabel("-");
        infoPanel.add(phoneLabel);

        infoPanel.add(new JLabel("Team Task Stats:"));
        statsLabel = new JLabel("-");
        infoPanel.add(statsLabel);

        add(infoPanel, BorderLayout.NORTH);

        employeesTableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Surname", "Email", "Phone", "Verified"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeesTable = new JTable(employeesTableModel);
        employeesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tasksTableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Description", "Status", "Employee ID", "Deadline", "Created At", "Updated At"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tasksTable = new JTable(tasksTableModel);
        tasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel employeesWrapper = new JPanel(new BorderLayout());
        employeesWrapper.setBorder(BorderFactory.createTitledBorder("My Employees"));
        employeesWrapper.add(new JScrollPane(employeesTable), BorderLayout.CENTER);

        JPanel tasksWrapper = new JPanel(new BorderLayout());
        tasksWrapper.setBorder(BorderFactory.createTitledBorder("Team Tasks"));
        tasksWrapper.add(new JScrollPane(tasksTable), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, employeesWrapper, tasksWrapper);
        splitPane.setDividerLocation(250);

        add(splitPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshBtn = new JButton("Refresh");
        signOutBtn = new JButton("Sign Out");

        buttonPanel.add(refreshBtn);
        buttonPanel.add(signOutBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets the team leader information displayed in the view.
     *
     * @param name the team leader name
     * @param email the team leader email
     * @param phone the team leader phone number
     * @param stats the team task statistics text
     */
    public void setTeamLeaderInfo(String name, String email, String phone, String stats) {
        nameLabel.setText(name);
        emailLabel.setText(email);
        phoneLabel.setText(phone);
        statsLabel.setText(stats);
    }

    /**
     * Returns the table model used for the employees table.
     *
     * @return the employees table model
     */
    public DefaultTableModel getEmployeesTableModel() {
        return employeesTableModel;
    }

    /**
     * Returns the table model used for the tasks table.
     *
     * @return the tasks table model
     */
    public DefaultTableModel getTasksTableModel() {
        return tasksTableModel;
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