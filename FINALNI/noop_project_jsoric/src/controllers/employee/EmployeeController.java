package controllers.employee;

import entities.Employee;
import repositories.UserRepository;
import view.employee.EmployeeView;
import view.LoginView;

import javax.swing.*;

/**
 * Controller for the employee verification view.
 * <p>
 * Handles accepting verification and marking the employee as verified.
 * </p>
 */
public class EmployeeController {

    private final EmployeeView view;
    private final UserRepository repo;

    /**
     * Creates controller and attaches listeners to the provided view.
     *
     * @param view employee verification UI
     * @param repo repository for persistence
     */
    public EmployeeController(EmployeeView view, UserRepository repo) {
        this.view = view;
        this.repo = repo;
        init();
    }

    /**
     * Registers UI listeners.
     */
    private void init() {
        view.getAcceptBtn().addActionListener(e -> accept());
    }

    /**
     * Accepts data accuracy, marks employee verified, shows a confirmation, and returns to login.
     */
    private void accept() {
        Employee employee = view.getEmployee();

        repo.markVerified(employee.getEmail());

        JOptionPane.showMessageDialog(
                view,
                "Thank you. Your data has been verified.",
                "Verification successful",
                JOptionPane.INFORMATION_MESSAGE
        );

        view.dispose();

        new LoginView(repo).setVisible(true);
    }
}