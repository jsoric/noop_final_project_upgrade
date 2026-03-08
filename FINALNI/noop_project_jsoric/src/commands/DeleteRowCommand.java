package commands;

import entities.Employee;
import helpers.TableModelHelper;
import interfaces.Command;
import repositories.UserRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Command implementation that deletes a selected employee row.
 * <p>
 * The command stores the deleted row data so the operation can be undone
 * and re-executed (redo) without relying on the current table selection.
 * </p>
 */
public class DeleteRowCommand implements Command {

    private DefaultTableModel defaultTableModel;
    private JTable table;
    private Employee deletedEmployee;
    private UserRepository userRepository;

    /**
     * Creates a delete-row command operating on the given table and repository.
     *
     * @param defaultTableModel table model containing employee data
     * @param table JTable used to determine the selected row
     * @param userRepository repository used for persistence operations
     */
    public DeleteRowCommand(DefaultTableModel defaultTableModel,
                            JTable table,
                            UserRepository userRepository) {
        this.defaultTableModel = defaultTableModel;
        this.table = table;
        this.userRepository = userRepository;
    }

    /**
     * Executes the delete command.
     * <p>
     * On the first execution, the currently selected employee is retrieved from the
     * database and stored as a snapshot so the operation can be undone later.
     * The employee is then deleted from the database and the table model is refreshed.
     * </p>
     * <p>
     * If the command is executed again (redo operation), the previously stored
     * employee identifier is used to perform the deletion without relying on the
     * current table selection.
     * </p>
     *
     * @return {@code true} if the operation was executed successfully,
     *         {@code false} if no table row was selected
     */
    @Override
    public boolean execute() {

        if (deletedEmployee != null) {
            userRepository.deleteUser(deletedEmployee.getId());
            TableModelHelper.refreshTableModel(defaultTableModel, userRepository);
            return true;
        }

        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            return false;
        }

        Long id = (Long) defaultTableModel.getValueAt(selectedRow, 0);

        deletedEmployee = userRepository.getEmployeeById(id);

        userRepository.deleteUser(id);

        TableModelHelper.refreshTableModel(defaultTableModel, userRepository);

        return true;
    }
    /**
     * Undoes the delete operation by restoring the previously removed employee.
     * <p>
     * The employee is restored using the original identifier and field values.
     * Verification status is also restored if applicable.
     * </p>
     */


    /**
     * Reverts the delete operation by restoring the previously removed employee.
     * <p>
     * The method uses the stored employee snapshot created during the initial
     * execution of the command. The employee is reinserted into the database
     * with the original data including identifier, password and verification status.
     * </p>
     * <p>
     * After restoration, the table model is refreshed so the change becomes visible
     * in the user interface.
     * </p>
     */
    @Override
    public void undo() {

        if (deletedEmployee == null) {
            return;
        }

        userRepository.restoreEmployee(deletedEmployee);

        TableModelHelper.refreshTableModel(defaultTableModel, userRepository);
    }
}

