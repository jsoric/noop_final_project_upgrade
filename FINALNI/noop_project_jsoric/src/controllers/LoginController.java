package controllers;

import controllers.employee.EmployeeController;
import controllers.employee.EmployeeTaskController;
import entities.Employee;
import enums.Roles;
import repositories.UserRepository;
import view.AdminView;
import view.employee.EmployeeView;
import view.LoginView;
import view.employee.EmployeeTaskView;

import javax.swing.*;

/**
 * Controller responsible for login flow.
 * <p>
 * Verifies credentials, routes admin to {@link AdminView}, and employees to verification flow.
 * </p>
 */
public class LoginController {

    private LoginView loginView;
    private UserRepository userRepository;

    /**
     * Creates a controller tied to the given login view.
     *
     * @param loginView login UI
     * @param userRepository repository for authentication and user retrieval
     */
    public LoginController(LoginView loginView,
                           UserRepository userRepository) {
        this.loginView = loginView;
        this.userRepository = userRepository;
    }

    /**
     * Creates a controller without binding a view.
     *
     * @param userRepository repository for authentication and user retrieval
     */
    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Attempts to log in using credentials from the view.
     * <p>
     * Admin users are routed to the admin UI. Employees who are not verified are routed to
     * the verification UI. Verified employees are shown an informational message.
     * </p>
     */
    public void login() {

        var email = loginView.getEmailField().getText();
        var password = loginView.getPasswordField().getText();

        if (!userRepository.verifyCreds(email, password)) {
            JOptionPane.showMessageDialog(loginView, "Wrong credentials");
            return;
        }

        var user = userRepository.getUserByEmail(email);

        if (user.getRole().equals(Roles.admin.toString())) {
            AdminView adminView = new AdminView(userRepository);
            adminView.setVisible(true);
            loginView.dispose();
            return;
        }

        Employee employee = (Employee) user;

        if (!employee.isVerified()) {
            EmployeeView verificationView = new EmployeeView(employee);
            new EmployeeController(verificationView, userRepository);

            verificationView.setVisible(true);
            loginView.dispose();
            return;
        }

        EmployeeTaskView employeeTaskView = new EmployeeTaskView();
        new EmployeeTaskController(
                employeeTaskView,
                employee,
                userRepository,
                new repositories.TaskRepository(configuration.DatabaseConnection.getConnection())
        );
        employeeTaskView.setVisible(true);
        loginView.dispose();
    }
}