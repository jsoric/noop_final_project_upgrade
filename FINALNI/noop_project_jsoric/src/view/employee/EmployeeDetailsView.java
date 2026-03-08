package view.employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Pure MVC view that shows employee details and assigned tasks.
 */
public class EmployeeDetailsView extends JFrame {

    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JLabel verifiedLabel;

    private JTable tasksTable;
    private DefaultTableModel tableModel;

    private JButton sendLoginEmailBtn;
    private JButton changeStatusBtn;
    private JButton refreshBtn;
    private JButton closeBtn;

    public EmployeeDetailsView() {
        setTitle("Employee Details");
        setSize(900, 550);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    }

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

    public void setEmployeeData(String name, String email, String phone, String verified) {
        nameLabel.setText(name);
        emailLabel.setText(email);
        phoneLabel.setText(phone);
        verifiedLabel.setText(verified);
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JTable getTasksTable() {
        return tasksTable;
    }

    public JButton getSendLoginEmailBtn() {
        return sendLoginEmailBtn;
    }

    public JButton getChangeStatusBtn() {
        return changeStatusBtn;
    }

    public JButton getRefreshBtn() {
        return refreshBtn;
    }

    public JButton getCloseBtn() {
        return closeBtn;
    }
}