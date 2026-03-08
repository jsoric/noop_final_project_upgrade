package view.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Top panel in Admin view containing search field and sign out button.
 */
public class AdminViewTopPanel extends JPanel {

    private JTextField searchField = new JTextField(15);
    private JButton signOutBtn = new JButton("Sign Out");

    /**
     * Builds the top panel UI (search input on left, welcome + sign out on right).
     */
    public AdminViewTopPanel() {

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 55));
        setBorder(new EmptyBorder(10, 15, 10, 15));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel searchLabel = new JLabel("Search:");
        searchField.setPreferredSize(new Dimension(250, 30));

        leftPanel.add(searchLabel);
        leftPanel.add(searchField);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.add(new JLabel("Welcome, Admin!"));
        rightPanel.add(signOutBtn);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    /**
     * @return search field
     */
    public JTextField getSearchField() {
        return searchField;
    }

    /**
     * @return sign out button
     */
    public JButton getSignOutBtn() {
        return signOutBtn;
    }
}