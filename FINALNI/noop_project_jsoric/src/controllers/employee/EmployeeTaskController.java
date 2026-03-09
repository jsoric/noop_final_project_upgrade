package controllers.employee;

import entities.Employee;
import entities.Task;
import enums.TaskStatus;
import repositories.TaskRepository;
import repositories.UserRepository;
import view.LoginView;
import view.employee.EmployeeTaskView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Controller responsible for handling actions in {@link EmployeeTaskView}.
 * <p>
 * It displays employee information, loads assigned tasks, allows the employee
 * to update task status and supports signing out of the application.
 * </p>
 */
public class EmployeeTaskController {

    private final EmployeeTaskView view;
    private final Employee employee;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    /**
     * Creates a controller for the employee task view.
     *
     * @param view view used to display employee data and tasks
     * @param employee employee currently using the application
     * @param userRepository repository used for login redirection
     * @param taskRepository repository used to load and update tasks
     */
    public EmployeeTaskController(EmployeeTaskView view,
                                  Employee employee,
                                  UserRepository userRepository,
                                  TaskRepository taskRepository) {
        this.view = view;
        this.employee = employee;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;

        init();
        loadEmployeeInfo();
        loadTasks();
    }

    /**
     * Registers all view listeners.
     */
    private void init() {
        view.getChangeStatusBtn().addActionListener(e -> changeMyTaskStatus());
        view.getRefreshBtn().addActionListener(e -> loadTasks());
        view.getSignOutBtn().addActionListener(e -> signOut());
    }

    /**
     * Loads employee information into the view.
     */
    private void loadEmployeeInfo() {
        view.setEmployeeInfo(
                employee.getName() + " " + employee.getSurname(),
                employee.getEmail(),
                employee.getPhoneNumber() != null ? employee.getPhoneNumber() : "-",
                employee.isVerified() ? "Yes" : "No"
        );
    }

    /**
     * Loads all tasks assigned to the current employee into the table.
     */
    private void loadTasks() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0);

        List<Task> tasks = taskRepository.getTasksByEmployeeId(employee.getId());

        for (Task task : tasks) {
            tableModel.addRow(new Object[]{
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getDisplayStatus(),
                    task.getDisplayDeadline(),
                    task.getCreatedAt() != null ? task.getCreatedAt().toLocalDate().toString() : "-",
                    task.getUpdatedAt() != null ? task.getUpdatedAt().toLocalDate().toString() : "-"
            });
        }
    }

    /**
     * Changes the status of the selected task for the current employee.
     * <p>
     * The user must first select a task from the table and then choose a new
     * status from the available {@link TaskStatus} values.
     * </p>
     */
    private void changeMyTaskStatus() {
        int selectedRow = view.getTasksTable().getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    view,
                    "Please select a task first.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Long taskId = (Long) view.getTableModel().getValueAt(selectedRow, 0);

        TaskStatus selectedStatus = (TaskStatus) JOptionPane.showInputDialog(
                view,
                "Select new status:",
                "Change Task Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                TaskStatus.values(),
                TaskStatus.IN_PROGRESS
        );

        if (selectedStatus == null) {
            return;
        }

        boolean success = taskRepository.updateTaskStatusForEmployee(
                taskId,
                employee.getId(),
                selectedStatus
        );

        if (success) {
            JOptionPane.showMessageDialog(
                    view,
                    "Task status updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            loadTasks();
        } else {
            JOptionPane.showMessageDialog(
                    view,
                    "Failed to update task status.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Closes the employee view and returns to the login screen.
     */
    private void signOut() {
        view.dispose();
        new LoginView(userRepository).setVisible(true);
    }
}