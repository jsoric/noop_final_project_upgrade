package repositories;

import entities.Admin;
import entities.Employee;
import entities.User;
import enums.Roles;
import enums.VerificationStatus;
import factory.AdminFactory;
import factory.EmployeeFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for CRUD operations on users (admins and employees) stored in H2.
 */
public class UserRepository {

    private final Connection connection;

    /**
     * Creates a repository using an existing JDBC connection and ensures schema exists.
     *
     * @param connection JDBC connection
     */
    public UserRepository(Connection connection) {
        this.connection = connection;
        try {
            createTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the USERS table if it does not exist and ensures a default admin exists.
     *
     * @throws SQLException if SQL execution fails
     */
    public void createTable() throws SQLException {

        String createSql = """
            CREATE TABLE IF NOT EXISTS USERS (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                email VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(255),
                user_role VARCHAR(50) NOT NULL,
                employee_name VARCHAR(255),
                employee_surname VARCHAR(255),
                verification_status VARCHAR(30) NOT NULL,
                phone_number VARCHAR(30)
            )
        """;

        try (Statement st = connection.createStatement()) {
            st.execute(createSql);
        }

        ensureAdminExists();
    }

    /**
     * Ensures at least one admin user exists; inserts a default admin if none found.
     *
     * @throws SQLException if SQL execution fails
     */
    private void ensureAdminExists() throws SQLException {

        String checkSql = "SELECT COUNT(*) FROM USERS WHERE user_role = ?";

        try (PreparedStatement ps = connection.prepareStatement(checkSql)) {
            ps.setString(1, Roles.admin.toString());
            ResultSet rs = ps.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0) {

                String insertSql = """
                    INSERT INTO USERS
                    (email, password, user_role, verification_status)
                    VALUES (?, ?, ?, ?)
                """;

                try (PreparedStatement insertPs =
                             connection.prepareStatement(insertSql)) {

                    insertPs.setString(1, "admin");
                    insertPs.setString(2, "admin");
                    insertPs.setString(3, Roles.admin.toString());
                    insertPs.setString(4, VerificationStatus.verified.name());
                    insertPs.executeUpdate();
                }
            }
        }
    }

    /**
     * Saves a new employee with PENDING verification status.
     *
     * @param email employee email
     * @param name employee first name
     * @param surname employee last name
     * @param phone_number employee phone
     */
    public void saveEmployee(String email, String name, String surname, String phone_number) {

        String sql = """
            INSERT INTO USERS
            (email, employee_name, employee_surname, user_role, verification_status, phone_number)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, name);
            ps.setString(3, surname);
            ps.setString(4, Roles.employee.toString());
            ps.setString(5, VerificationStatus.pending.name());
            ps.setString(6, phone_number);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
/*
    /**
     * Saves a new employee with a specified ID (used by undo restore).
     * If the email already exists, this method returns without inserting.
     *
     * @param id employee id
     * @param email employee email
     * @param name employee first name
     * @param surname employee last name
     * @param phone_number employee phone
     */

/*
    public void saveEmployeeWithId(
            Long id,
            String email,
            String name,
            String surname,
            String phone_number
    ) {

        if (emailExists(email)) return;

        String sql = """
        INSERT INTO USERS
        (id, email, employee_name, employee_surname, phone_number, user_role, verification_status)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.setString(2, email);
            ps.setString(3, name);
            ps.setString(4, surname);
            ps.setString(5, phone_number);
            ps.setString(6, Roles.employee.toString());
            ps.setString(7, VerificationStatus.pending.name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
*/
    /**
     * Retrieves a single employee from the database using the given identifier.
     * <p>
     * The method executes a parameterized SQL query that selects a row from the
     * USERS table where the ID matches the provided value and the role is employee.
     * If a matching record is found, it is mapped to an {@link Employee} object.
     * </p>
     *
     * @param id unique identifier of the employee
     * @return an {@link Employee} instance containing the retrieved data,
     *         or {@code null} if no employee with the given ID exists
     */
    public Employee getEmployeeById(Long id) {

        String sql = "SELECT * FROM USERS WHERE id = ? AND user_role = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.setString(2, Roles.employee.toString());

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            Employee e = new Employee();
            e.setId(rs.getLong("id"));
            e.setEmail(rs.getString("email"));
            e.setPassword(rs.getString("password"));
            e.setName(rs.getString("employee_name"));
            e.setSurname(rs.getString("employee_surname"));
            e.setPhoneNumber(rs.getString("phone_number"));
            e.setVerificationStatus(
                    VerificationStatus.valueOf(rs.getString("verification_status"))
            );

            return e;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Restores a previously deleted employee in the database.
     * <p>
     * This method is primarily used by undo operations. It inserts the employee
     * back into the USERS table using the original identifier and all stored
     * attributes including email, password, verification status and phone number.
     * </p>
     *
     * @param e employee instance containing the data to be restored
     */
    public void restoreEmployee(Employee e) {

        String sql = """
        INSERT INTO USERS
        (id, email, password, user_role, employee_name, employee_surname, verification_status, phone_number)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, e.getId());
            ps.setString(2, e.getEmail());
            ps.setString(3, e.getPassword());
            ps.setString(4, Roles.employee.toString());
            ps.setString(5, e.getName());
            ps.setString(6, e.getSurname());
            ps.setString(7, e.getVerificationStatus().name());
            ps.setString(8, e.getPhoneNumber());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    /**
     * Returns all employees from the database.
     *
     * @return list of employees
     */
    public List<Employee> getAllEmployees() {

        List<Employee> list = new ArrayList<>();
        EmployeeFactory factory = new EmployeeFactory();

        String sql = "SELECT * FROM USERS WHERE user_role = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, Roles.employee.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Employee e = (Employee) factory.createUser();
                e.setId(rs.getLong("id"));
                e.setEmail(rs.getString("email"));
                e.setName(rs.getString("employee_name"));
                e.setSurname(rs.getString("employee_surname"));
                e.setVerificationStatus(
                        VerificationStatus.valueOf(
                                rs.getString("verification_status"))
                );
                e.setPhoneNumber(rs.getString("phone_number"));
                list.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * Returns a user (Admin or Employee) by email, or {@code null} if not found.
     *
     * @param email user email
     * @return user instance or {@code null}
     */
    public User getUserByEmail(String email) {

        String sql = "SELECT * FROM USERS WHERE email = ?";
        AdminFactory adminFactory = new AdminFactory();
        EmployeeFactory employeeFactory = new EmployeeFactory();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            String role = rs.getString("user_role");

            if (role.equals(Roles.admin.toString())) {
                Admin admin = (Admin) adminFactory.createUser();
                admin.setEmail(email);
                admin.setRole(role);
                return admin;
            }

            Employee e = (Employee) employeeFactory.createUser();
            e.setId(rs.getLong("id"));
            e.setEmail(email);
            e.setName(rs.getString("employee_name"));
            e.setSurname(rs.getString("employee_surname"));
            e.setPhoneNumber(rs.getString("phone_number"));
            e.setRole(role);
            e.setVerificationStatus(
                    VerificationStatus.valueOf(
                            rs.getString("verification_status"))
            );
            return e;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets/updates the password for the given email.
     *
     * @param email user email
     * @param password password to set
     */
    public void setPassword(String email, String password) {

        String sql = "UPDATE USERS SET password = ? WHERE email = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, password);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Marks the employee as verified for the given email.
     *
     * @param email employee email
     */
    public void markVerified(String email) {

        String sql = """
            UPDATE USERS
            SET verification_status = ?
            WHERE email = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, VerificationStatus.verified.name());
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a user row by ID.
     *
     * @param id user id
     */
    public void deleteUser(Long id) {

        String sql = "DELETE FROM USERS WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies credentials by checking whether a row exists with matching email and password.
     *
     * @param email email
     * @param password password
     * @return {@code true} if credentials match; {@code false} otherwise
     */
    public boolean verifyCreds(String email, String password) {

        String sql = """
            SELECT COUNT(*)
            FROM USERS
            WHERE email = ? AND password = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Checks whether a user with the given email exists.
     *
     * @param email email to check
     * @return {@code true} if exists; {@code false} otherwise
     */
    public boolean emailExists(String email) {

        String sql = "SELECT COUNT(*) FROM USERS WHERE email = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}