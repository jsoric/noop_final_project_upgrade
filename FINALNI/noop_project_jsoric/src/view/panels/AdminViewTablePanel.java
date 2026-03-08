package view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Table panel in Admin view that displays the employee list.
 */
public class AdminViewTablePanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    /**
     * Builds a non-editable JTable with employee columns and a row sorter.
     */
    public AdminViewTablePanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Surname", "Email", "Phone",  "Verified"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);

        table.setRowSorter(new javax.swing.table.TableRowSorter<>(model));

        add(new JLabel("Employee List"), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * @return JTable instance
     */
    public JTable getTable() {
        return table;
    }

    /**
     * @return table model instance
     */
    public DefaultTableModel getModel() {
        return model;
    }
}