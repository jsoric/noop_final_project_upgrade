package view;

import repositories.UserRepository;
import view.panels.*;
import controllers.AdminController;

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
     * Creates the Admin view and wires it to {@link AdminController}.
     *
     * @param userRepository repository for employee operations and sign-out flow
     */
    public AdminView(UserRepository userRepository) {

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

        new AdminController(
                tablePanel,
                formPanel,
                bottomPanel,
                userRepository,
                topPanel
        );
    }
}