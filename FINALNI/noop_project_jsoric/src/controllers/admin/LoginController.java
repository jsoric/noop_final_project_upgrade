package controllers.admin;

import controllers.employee.EmployeeTaskController;
import controllers.employee.EmployeeVerificationController;
import controllers.teamleader.TeamLeaderController;
import entities.Employee;
import entities.TeamLeader;
import enums.Roles;
import repositories.TaskRepository;
import repositories.UserRepository;
import view.AdminView;
import view.LoginView;
import view.employee.EmployeeTaskView;
import view.employee.EmployeeVerificationView;
import view.teamleader.TeamLeaderView;

import javax.swing.JOptionPane;

/**
 * Controller responsible for handling the login flow.
 * <p>
 * It validates user credentials and redirects authenticated users to the
 * appropriate view based on their role and verification status.
 * </p>
 */
public class LoginController {

    private LoginView loginView;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    /**
     * Creates a controller bound to the given login view.
     *
     * @param loginView login view used for user input
     * @param userRepository repository used for authentication and user retrieval
     * @param taskRepository repository used for task-related operations
     */
    public LoginController(LoginView loginView,
                           UserRepository userRepository,
                           TaskRepository taskRepository) {
        this.loginView = loginView;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;

        init();
    }

    /**
     * Registers all listeners for the login view.
     */
    private void init() {
        loginView.getLoginButton().addActionListener(e -> login());
        loginView.getPasswordField().addActionListener(e -> login());
    }

    /**
     * Attempts to authenticate the user and open the corresponding application view.
     * <p>
     * Admin users are redirected to the admin view, team leaders to the team leader
     * dashboard, unverified employees to the verification view, and verified
     * employees to the employee task view.
     * </p>
     */
    public void login() {
        var email = loginView.getEmailField().getText();
        String password = new String(loginView.getPasswordField().getPassword());

        if (!userRepository.verifyCreds(email, password)) {
            JOptionPane.showMessageDialog(loginView, "Wrong credentials");
            return;
        }

        var user = userRepository.getUserByEmail(email);

        if (user.getRole().equals(Roles.admin.toString())) {
            AdminView adminView = new AdminView();

            new AdminController(
                    adminView.getTablePanel(),
                    adminView.getFormPanel(),
                    adminView.getBottomPanel(),
                    userRepository,
                    taskRepository,
                    adminView.getTopPanel()
            );

            adminView.setVisible(true);
            loginView.dispose();
            return;
        }

        if (user.getRole().equals(Roles.teamleader.toString())) {
            TeamLeader teamLeader = (TeamLeader) user;

            TeamLeaderView dashboardView = new TeamLeaderView();
            new TeamLeaderController(
                    dashboardView,
                    teamLeader,
                    userRepository,
                    taskRepository
            );

            dashboardView.setVisible(true);
            loginView.dispose();
            return;
        }

        Employee employee = (Employee) user;

        if (!employee.isVerified()) {
            EmployeeVerificationView verificationView = new EmployeeVerificationView(employee);
            new EmployeeVerificationController(verificationView, userRepository, taskRepository);

            verificationView.setVisible(true);
            loginView.dispose();
            return;
        }

        EmployeeTaskView employeeTaskView = new EmployeeTaskView();
        new EmployeeTaskController(
                employeeTaskView,
                employee,
                userRepository,
                taskRepository
        );

        employeeTaskView.setVisible(true);
        loginView.dispose();
    }
}