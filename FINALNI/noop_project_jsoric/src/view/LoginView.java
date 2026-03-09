package view;

import repositories.UserRepository;

import javax.swing.*;
import java.awt.*;

/**
 * Login screen for both administrators and employees.
 * <p>
 * Allows users to log in using email/username and password or verification code.
 * Authentication logic is delegated externally to a controller.
 * </p>
 */
public class LoginView extends JFrame {

    /**
     * Input field for username or email.
     */
    private final JTextField usernameField = new JTextField(20);

    /**
     * Input field for password or verification code.
     */
    private final JPasswordField passwordField = new JPasswordField(20);

    /**
     * Login button.
     */
    private final JButton loginButton = new JButton("Login");

    /**
     * Repository reference kept for compatibility if needed elsewhere.
     */
    private final UserRepository userRepository;

    /**
     * Creates the login window.
     *
     * @param userRepository repository used by the application
     */
    public LoginView(UserRepository userRepository) {
        this.userRepository = userRepository;

        setTitle("Login Page");
        setSize(350, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    /**
     * Initializes the graphical layout and components of the login screen.
     * <p>
     * Uses {@link GridBagLayout} for structured form placement.
     * </p>
     */
    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        gbc.insets = new Insets(30, 8, 5, 8);
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(new JLabel("Username / Email"), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 8, 10, 8);
        gbc.anchor = GridBagConstraints.CENTER;
        usernameField.setPreferredSize(new Dimension(250, 30));
        panel.add(usernameField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 8, 5, 8);
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(new JLabel("Password / Verification Code"), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 8, 20, 8);
        gbc.anchor = GridBagConstraints.CENTER;
        passwordField.setPreferredSize(new Dimension(250, 30));
        panel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        loginButton.setPreferredSize(new Dimension(100, 28));
        panel.add(loginButton, gbc);

        gbc.gridy++;
        gbc.weighty = 1;
        panel.add(Box.createVerticalGlue(), gbc);

        add(panel);
    }

    /**
     * Returns the email/username input field.
     *
     * @return email input field
     */
    public JTextField getEmailField() {
        return usernameField;
    }

    /**
     * Returns the password or verification code input field.
     *
     * @return password field
     */
    public JPasswordField getPasswordField() {
        return passwordField;
    }

    /**
     * Returns the login button.
     *
     * @return login button
     */
    public JButton getLoginButton() {
        return loginButton;
    }
}