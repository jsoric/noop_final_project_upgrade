package controllers.teamleader;

import entities.Employee;
import entities.Task;
import entities.TeamLeader;
import repositories.TaskRepository;
import repositories.UserRepository;
import view.LoginView;
import view.teamleader.TeamLeaderView;

import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Controller responsible for handling actions in {@link TeamLeaderView}.
 * <p>
 * It loads team leader information, displays assigned employees and tasks,
 * supports refreshing the dashboard data and allows signing out.
 * </p>
 */
public class TeamLeaderController {

    private final TeamLeaderView view;
    private final TeamLeader teamLeader;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    /**
     * Creates a controller for the team leader view.
     *
     * @param view view used to display team leader data
     * @param teamLeader currently logged-in team leader
     * @param userRepository repository used to load assigned employees
     * @param taskRepository repository used to load task data and statistics
     */
    public TeamLeaderController(TeamLeaderView view,
                                TeamLeader teamLeader,
                                UserRepository userRepository,
                                TaskRepository taskRepository) {
        this.view = view;
        this.teamLeader = teamLeader;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;

        init();
        loadTeamLeaderInfo();
        loadEmployees();
        loadTasks();
    }

    /**
     * Registers all view listeners.
     */
    private void init() {
        view.getRefreshBtn().addActionListener(e -> refresh());
        view.getSignOutBtn().addActionListener(e -> signOut());
    }

    /**
     * Loads team leader information and task statistics into the view.
     */
    private void loadTeamLeaderInfo() {
        String stats = taskRepository.getTaskStatsByTeamLeaderId(teamLeader.getId());

        view.setTeamLeaderInfo(
                teamLeader.getName() + " " + teamLeader.getSurname(),
                teamLeader.getEmail(),
                teamLeader.getPhoneNumber() != null ? teamLeader.getPhoneNumber() : "-",
                stats
        );
    }

    /**
     * Loads all employees assigned to the current team leader into the table.
     */
    private void loadEmployees() {
        DefaultTableModel tableModel = view.getEmployeesTableModel();
        tableModel.setRowCount(0);

        List<Employee> employees = userRepository.getEmployeesByTeamLeaderId(teamLeader.getId());

        for (Employee employee : employees) {
            tableModel.addRow(new Object[]{
                    employee.getId(),
                    employee.getName(),
                    employee.getSurname(),
                    employee.getEmail(),
                    employee.getPhoneNumber(),
                    employee.isVerified()
            });
        }
    }

    /**
     * Loads all tasks associated with the current team leader into the table.
     */
    private void loadTasks() {
        DefaultTableModel tableModel = view.getTasksTableModel();
        tableModel.setRowCount(0);

        List<Task> tasks = taskRepository.getTasksByTeamLeaderId(teamLeader.getId());

        for (Task task : tasks) {
            tableModel.addRow(new Object[]{
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus().getDisplayName(),
                    task.getEmployeeId(),
                    task.getDisplayDeadline(),
                    task.getCreatedAt() != null ? task.getCreatedAt().toLocalDate().toString() : "-",
                    task.getUpdatedAt() != null ? task.getUpdatedAt().toLocalDate().toString() : "-"
            });
        }
    }

    /**
     * Reloads all dashboard data.
     */
    private void refresh() {
        loadTeamLeaderInfo();
        loadEmployees();
        loadTasks();
    }

    /**
     * Closes the team leader view and returns to the login screen.
     */
    private void signOut() {
        view.dispose();
        new LoginView(userRepository).setVisible(true);
    }
}