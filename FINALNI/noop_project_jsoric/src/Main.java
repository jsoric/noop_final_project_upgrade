import configuration.DatabaseConnection;
import controllers.admin.LoginController;
import org.h2.tools.Server;
import repositories.TaskRepository;
import repositories.UserRepository;
import view.LoginView;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Application entry point.
 * <p>
 * Starts the embedded H2 web console and launches the login window.
 * </p>
 */
public class Main {

    /**
     * Launches the Swing application on the Event Dispatch Thread.
     * <p>
     * Initializes database connection, starts H2 console,
     * and displays the login window.
     * </p>
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082")
                            .start();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("H2 Console started at http://localhost:8082");

                Connection connection = DatabaseConnection.getConnection();

                UserRepository userRepository = new UserRepository(connection);
                TaskRepository taskRepository = new TaskRepository(connection);

                LoginView loginView = new LoginView(userRepository);
                new LoginController(loginView, userRepository, taskRepository);

                loginView.setVisible(true);
            }
        });
    }
}