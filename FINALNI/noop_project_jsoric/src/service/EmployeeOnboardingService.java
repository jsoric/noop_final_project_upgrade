package service;

import enums.VerificationStatus;
import repositories.UserRepository;

/**
 * Handles onboarding of a newly created employee.
 * <p>
 * Generates a password, stores it, and sends it via email.
 * </p>
 */
public class EmployeeOnboardingService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    /**
     * Creates the onboarding service.
     *
     * @param userRepository repository used to set password
     */
    public EmployeeOnboardingService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.emailService = new EmailService();
    }

    /**
     * Onboards an employee by generating a 6-digit PIN, saving it as password,
     * and emailing it to the employee.
     *
     * @param name employee first name
     * @param email employee email
     * @return {@link VerificationStatus#pending}
     */
    public VerificationStatus onboardEmployee(String name, String email) {

        String password = PasswordService.generate6DigitPin();
        userRepository.setPassword(email, password);
        emailService.sendPassword(name, email, password);

        return VerificationStatus.pending;
    }
}