package controllers.admin;

import entities.Employee;
import entities.Task;
import entities.TeamLeader;
import enums.TaskStatus;
import interfaces.Refreshable;
import repositories.TaskRepository;
import repositories.UserRepository;
import service.EmployeeOnboardingService;
import view.employee.EmployeeDetailsView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Controller responsible for handling actions in {@link EmployeeDetailsView}.
 * <p>
 * It loads employee information, displays assigned tasks, allows task status
 * updates and supports sending a login email with a new access code.
 * </p>
 */
public class EmployeeDetailsController implements Refreshable {

    private final EmployeeDetailsView view;
    private final Long employeeId;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    /**
     * Creates a controller for the employee details view.
     *
     * @param view view used to display employee details and tasks
     * @param employeeId identifier of the employee whose details are shown
     * @param userRepository repository used to load employee and team leader data
     * @param taskRepository repository used to load and update employee tasks
     */
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

    /**
     * Registers all view listeners.
     */
    private void init() {
        view.getSendLoginEmailBtn().addActionListener(e -> sendLoginEmail());
        view.getChangeStatusBtn().addActionListener(e -> changeTaskStatus());
        view.getRefreshBtn().addActionListener(e -> refresh());
        view.getCloseBtn().addActionListener(e -> view.dispose());
    }

    /**
     * Loads employee details and displays them in the view.
     * <p>
     * If the employee has an assigned team leader, the team leader information
     * is also displayed. If the employee does not exist, the view is closed.
     * </p>
     */
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

        String teamLeaderText = "Not assigned";

        if (employee.getTeamLeaderId() != null) {
            TeamLeader teamLeader = userRepository.getTeamLeaderById(employee.getTeamLeaderId());

            if (teamLeader != null) {
                teamLeaderText = teamLeader.getName() + " " + teamLeader.getSurname()
                        + " (" + teamLeader.getEmail() + ")";
            }
        }

        view.setEmployeeData(
                employee.getName() + " " + employee.getSurname(),
                employee.getEmail(),
                employee.getPhoneNumber() != null ? employee.getPhoneNumber() : "-",
                employee.isVerified() ? "Yes" : "No",
                teamLeaderText
        );
    }

    /**
     * Loads all tasks assigned to the employee into the task table.
     */
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
                    task.getCreatedAt() != null ? task.getCreatedAt().toLocalDate().toString() : "-",
                    task.getUpdatedAt() != null ? task.getUpdatedAt().toLocalDate().toString() : "-"
            });
        }
    }

    /**
     * Changes the status of the selected task.
     * <p>
     * The user must first select a task from the table and then choose a new
     * status from the available {@link TaskStatus} values.
     * </p>
     */
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

    /**
     * Sends a login email with a newly generated access code to the employee.
     */
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

    @Override
    public void refresh() {
        loadEmployeeData();
        loadTasks();
    }
}