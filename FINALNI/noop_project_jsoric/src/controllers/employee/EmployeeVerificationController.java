package controllers.employee;

import entities.Employee;
import repositories.UserRepository;
import view.LoginView;
import view.employee.EmployeeVerificationView;

import javax.swing.JOptionPane;

/**
 * Controller responsible for handling actions in {@link EmployeeVerificationView}.
 * <p>
 * It allows the employee to confirm their data and marks the employee as verified
 * in the repository.
 * </p>
 */
public class EmployeeVerificationController {

    private final EmployeeVerificationView view;
    private final UserRepository repo;

    /**
     * Creates a controller for the employee verification view.
     *
     * @param view view used to display employee verification data
     * @param repo repository used to update verification status
     */
    public EmployeeVerificationController(EmployeeVerificationView view, UserRepository repo) {
        this.view = view;
        this.repo = repo;
        init();
    }

    /**
     * Registers all view listeners.
     */
    private void init() {
        view.getAcceptBtn().addActionListener(e -> accept());
    }

    /**
     * Marks the current employee as verified, displays a confirmation message
     * and returns the user to the login view.
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