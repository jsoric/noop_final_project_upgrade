package view;

import view.panels.AdminViewBottomPanel;
import view.panels.AdminViewFormPanel;
import view.panels.AdminViewTablePanel;
import view.panels.AdminViewTopPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Admin main window that displays employees and allows managing them.
 */
public class AdminView extends JFrame {

    private AdminViewTopPanel topPanel;
    private AdminViewTablePanel tablePanel;
    private AdminViewFormPanel formPanel;
    private AdminViewBottomPanel bottomPanel;

    /**
     * Creates the Admin view.
     */
    public AdminView() {
        setTitle("Employee Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        topPanel = new AdminViewTopPanel();
        tablePanel = new AdminViewTablePanel();
        formPanel = new AdminViewFormPanel();
        bottomPanel = new AdminViewBottomPanel();

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                tablePanel,
                formPanel
        );
        splitPane.setDividerLocation(750);
        splitPane.setEnabled(false);

        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public AdminViewTopPanel getTopPanel() {
        return topPanel;
    }

    public AdminViewTablePanel getTablePanel() {
        return tablePanel;
    }

    public AdminViewFormPanel getFormPanel() {
        return formPanel;
    }

    public AdminViewBottomPanel getBottomPanel() {
        return bottomPanel;
    }
}