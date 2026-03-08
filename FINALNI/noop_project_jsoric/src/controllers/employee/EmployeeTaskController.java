package controllers.employee;

import entities.Employee;
import entities.Task;
import enums.TaskStatus;
import repositories.TaskRepository;
import repositories.UserRepository;
import view.LoginView;
import view.employee.EmployeeTaskView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Controller for EmployeeTaskView.
 */
public class EmployeeTaskController {

    private final EmployeeTaskView view;
    private final Employee employee;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

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

    private void init() {
        view.getChangeStatusBtn().addActionListener(e -> changeMyTaskStatus());
        view.getRefreshBtn().addActionListener(e -> loadTasks());
        view.getSignOutBtn().addActionListener(e -> signOut());
    }

    private void loadEmployeeInfo() {
        view.setEmployeeInfo(
                employee.getName() + " " + employee.getSurname(),
                employee.getEmail(),
                employee.getPhoneNumber() != null ? employee.getPhoneNumber() : "-",
                employee.isVerified() ? "Yes" : "No"
        );
    }

    private void loadTasks() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0);

        List<Task> tasks = taskRepository.getTasksByEmployeeId(employee.getId());

        for (Task task : tasks) {
            tableModel.addRow(new Object[]{
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus().getDisplayName(),
                    task.getCreatedAt() != null ? task.getCreatedAt().toString() : "-",
                    task.getUpdatedAt() != null ? task.getUpdatedAt().toString() : "-"
            });
        }
    }

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

    private void signOut() {
        view.dispose();
        new LoginView(userRepository).setVisible(true);
    }
}