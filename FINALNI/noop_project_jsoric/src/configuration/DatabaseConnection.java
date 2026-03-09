package configuration;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Provides a singleton-like JDBC connection to the embedded H2 database.
 * <p>
 * Creates (or reuses) a single {@link Connection} instance targeting {@code ./employeedb}.
 * </p>
 */
public class DatabaseConnection {

    private static final String DB_URL = "jdbc:h2:./employeedb;AUTO_SERVER=TRUE";
    private static final String DB_USER = "sa";
    private static final String DB_PWD = "";

    private static Connection connection;

    /**
     * Returns an active H2 database connection.
     * <p>
     * If the stored connection is {@code null} or closed, the H2 driver is loaded and a new
     * connection is created.
     * </p>
     *
     * @return active {@link Connection}
     * @throws RuntimeException if a connection cannot be established
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.h2.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to connect to db", ex);
        }

        return connection;
    }
}