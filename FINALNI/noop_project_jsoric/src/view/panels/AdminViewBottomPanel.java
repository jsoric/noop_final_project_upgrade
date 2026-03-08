package view.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Bottom panel in Admin view holding action buttons.
 */
public class AdminViewBottomPanel extends JPanel {

    private JButton addTaskBtn = new JButton("Add Task");
    private JButton viewDetailsBtn = new JButton("View Details");
    private JButton deleteBtn = new JButton("Delete");
    private JButton undoBtn = new JButton("Undo");
    private JButton redoBtn = new JButton("Redo");
    private JButton exportBtn = new JButton("Export");

    /**
     * Builds the bottom panel layout and adds buttons.
     */
    public AdminViewBottomPanel() {
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);



        leftPanel.add(viewDetailsBtn);
        leftPanel.add(addTaskBtn);
        rightPanel.add(exportBtn);
        rightPanel.add(deleteBtn);
        rightPanel.add(undoBtn);
        rightPanel.add(redoBtn);
    }

    /**
     * @return add task button
     */
    public JButton getAddTaskBtn() {
        return addTaskBtn;
    }

    /**
     * @return view details button
     */
    public JButton getViewDetailsBtn() {
        return viewDetailsBtn;
    }

    /**
     * @return delete button
     */
    public JButton getDeleteBtn() {
        return deleteBtn;
    }

    /**
     * @return undo button
     */
    public JButton getUndoBtn() {
        return undoBtn;
    }

    /**
     * @return redo button
     */
    public JButton getRedoBtn() {
        return redoBtn;
    }

    /**
     * @return export button
     */
    public JButton getExportBtn() {
        return exportBtn;
    }
}