package commands;

import entities.Employee;
import helpers.TableModelHelper;
import interfaces.Command;
import repositories.UserRepository;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Command implementation responsible for deleting a selected employee row.
 * <p>
 * The command stores the deleted employee so the operation can be undone
 * and later executed again as a redo without depending on the current
 * table selection.
 */
public class DeleteRowCommand implements Command {

    private DefaultTableModel defaultTableModel;
    private JTable table;
    private Employee deletedEmployee;
    private UserRepository userRepository;

    /**
     * Creates a command for deleting an employee from the table and repository.
     *
     * @param defaultTableModel table model that contains employee data
     * @param table table used to determine the currently selected row
     * @param userRepository repository used for delete and restore operations
     */
    public DeleteRowCommand(DefaultTableModel defaultTableModel,
                            JTable table,
                            UserRepository userRepository) {
        this.defaultTableModel = defaultTableModel;
        this.table = table;
        this.userRepository = userRepository;
    }

    /**
     * Executes the delete operation.
     * <p>
     * During the first execution, the currently selected employee is loaded
     * from the repository and stored so the action can later be undone.
     * The employee is then removed from the repository and the table model
     * is refreshed.
     * </p>
     * <p>
     * If the command is executed again after an undo, the stored employee
     * is deleted again without relying on the current table selection.
     * </p>
     *
     * @return {@code true} if the delete operation was performed successfully;
     * {@code false} if no row was selected during the initial execution
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
     * The employee is restored using the snapshot saved during the initial
     * execution, including all original field values.
     * After restoration, the table model is refreshed so the change is visible
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