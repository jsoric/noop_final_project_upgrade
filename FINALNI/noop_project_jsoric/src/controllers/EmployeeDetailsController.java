package controllers;

import entities.Employee;
import entities.Task;
import enums.TaskStatus;
import repositories.TaskRepository;
import repositories.UserRepository;
import service.EmployeeOnboardingService;
import view.employee.EmployeeDetailsView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Controller for EmployeeDetailsView.
 */
public class EmployeeDetailsController {

    private final EmployeeDetailsView view;
    private final Long employeeId;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public EmployeeDetailsController(EmployeeDetailsView view,
                                     Long employeeId,
                                     UserRepository userRepository,
                                     TaskRepository taskRepository) {
        this.view = view;
        this.employeeId = employeeId;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;

        init();
        loadEmployeeData();
        loadTasks();
    }

    private void init() {
        view.getSendLoginEmailBtn().addActionListener(e -> sendLoginEmail());
        view.getChangeStatusBtn().addActionListener(e -> changeTaskStatus());
        view.getRefreshBtn().addActionListener(e -> {
            loadEmployeeData();
            loadTasks();
        });
        view.getCloseBtn().addActionListener(e -> view.dispose());
    }

    private void loadEmployeeData() {
        Employee employee = userRepository.getEmployeeById(employeeId);

        if (employee == null) {
            JOptionPane.showMessageDialog(
                    view,
                    "Employee not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            view.dispose();
            return;
        }

        view.setEmployeeData(
                employee.getName() + " " + employee.getSurname(),
                employee.getEmail(),
                employee.getPhoneNumber() != null ? employee.getPhoneNumber() : "-",
                employee.isVerified() ? "Yes" : "No"
        );
    }

    private void loadTasks() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0);

        List<Task> tasks = taskRepository.getTasksByEmployeeId(employeeId);

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

    private void changeTaskStatus() {
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

        boolean success = taskRepository.updateTaskStatus(taskId, selectedStatus);

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

    private void sendLoginEmail() {
        Employee employee = userRepository.getEmployeeById(employeeId);

        if (employee == null) {
            JOptionPane.showMessageDialog(
                    view,
                    "Employee not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                view,
                "Send login email with a new access code to:\n" + employee.getEmail() + "?",
                "Send Login Email",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            EmployeeOnboardingService onboarding = new EmployeeOnboardingService(userRepository);
            onboarding.onboardEmployee(employee.getName(), employee.getEmail());

            JOptionPane.showMessageDialog(
                    view,
                    "Login email sent successfully.\nA new access code has been generated.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    view,
                    "Failed to send login email.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}