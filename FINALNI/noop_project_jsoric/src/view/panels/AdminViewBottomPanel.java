package view.panels;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 * Bottom panel in the admin view that contains action buttons.
 */
public class AdminViewBottomPanel extends JPanel {

    private JButton addTaskBtn = new JButton("Add Task");
    private JButton viewDetailsBtn = new JButton("View Details");
    private JButton deleteBtn = new JButton("Delete");
    private JButton undoBtn = new JButton("Undo");
    private JButton redoBtn = new JButton("Redo");
    private JButton exportBtn = new JButton("Export");
    private JButton assignTeamLeaderBtn = new JButton("Assign Team Leader");

    /**
     * Creates the bottom panel and adds all action buttons.
     */
    public AdminViewBottomPanel() {
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        leftPanel.add(viewDetailsBtn);
        leftPanel.add(addTaskBtn);
        leftPanel.add(assignTeamLeaderBtn);
        rightPanel.add(exportBtn);
        rightPanel.add(deleteBtn);
        rightPanel.add(undoBtn);
        rightPanel.add(redoBtn);
    }

    /**
     * Returns the add task button.
     *
     * @return the add task button
     */
    public JButton getAddTaskBtn() {
        return addTaskBtn;
    }

    /**
     * Returns the view details button.
     *
     * @return the view details button
     */
    public JButton getViewDetailsBtn() {
        return viewDetailsBtn;
    }

    /**
     * Returns the delete button.
     *
     * @return the delete button
     */
    public JButton getDeleteBtn() {
        return deleteBtn;
    }

    /**
     * Returns the undo button.
     *
     * @return the undo button
     */
    public JButton getUndoBtn() {
        return undoBtn;
    }

    /**
     * Returns the redo button.
     *
     * @return the redo button
     */
    public JButton getRedoBtn() {
        return redoBtn;
    }

    /**
     * Returns the export button.
     *
     * @return the export button
     */
    public JButton getExportBtn() {
        return exportBtn;
    }

    /**
     * Returns the assign team leader button.
     *
     * @return the assign team leader button
     */
    public JButton getAssignTeamLeaderBtn() {
        return assignTeamLeaderBtn;
    }
}