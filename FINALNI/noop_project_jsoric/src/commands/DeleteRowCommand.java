package commands;

import entities.Employee;
import entities.Task;
import helpers.TableModelHelper;
import interfaces.Command;
import repositories.TaskRepository;
import repositories.UserRepository;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Command implementation responsible for deleting a selected employee row.
 * <p>
 * The command captures a full snapshot of the employee and all assigned tasks
 * during the first execution so the delete operation can be undone and redone
 * reliably without depending on the current table selection.
 */
public class DeleteRowCommand implements Command {

    private final DefaultTableModel defaultTableModel;
    private final JTable table;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    private Employee deletedEmployee;
    private List<Task> deletedTasks = new ArrayList<>();
    private boolean snapshotCaptured = false;

    public DeleteRowCommand(DefaultTableModel defaultTableModel,
                            JTable table,
                            UserRepository userRepository,
                            TaskRepository taskRepository) {
        this.defaultTableModel = defaultTableModel;
        this.table = table;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public boolean execute() {
        if (!snapshotCaptured) {
            if (!captureSnapshotFromSelection()) {
                return false;
            }
        }

        if (deletedEmployee == null) {
            return false;
        }

        userRepository.deleteUser(deletedEmployee.getId());
        TableModelHelper.refreshTableModel(defaultTableModel, userRepository);
        return true;
    }

    @Override
    public void undo() {
        if (!snapshotCaptured || deletedEmployee == null) {
            return;
        }

        userRepository.restoreEmployee(deletedEmployee);

        for (Task task : deletedTasks) {
            taskRepository.restoreTask(task);
        }

        TableModelHelper.refreshTableModel(defaultTableModel, userRepository);
    }

    /**
     * Captures the employee and all related tasks from the currently selected row.
     *
     * @return true if snapshot was captured successfully, otherwise false
     */
    private boolean captureSnapshotFromSelection() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            return false;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        Long id = (Long) defaultTableModel.getValueAt(modelRow, 0);

        Employee employee = userRepository.getEmployeeById(id);
        if (employee == null) {
            return false;
        }

        deletedEmployee = employee;
        deletedTasks = new ArrayList<>(taskRepository.getTasksByEmployeeId(id));
        snapshotCaptured = true;

        return true;
    }
}