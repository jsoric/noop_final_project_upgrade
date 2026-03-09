package repositories;

import entities.Admin;
import entities.Employee;
import entities.TeamLeader;
import entities.User;
import enums.Roles;
import enums.VerificationStatus;
import factory.AdminFactory;
import factory.EmployeeFactory;
import factory.TeamLeaderFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for CRUD operations on users stored in the H2 database.
 */
public class UserRepository {

    private final Connection connection;

    /**
     * Creates a repository using an existing JDBC connection
     * and ensures that the schema exists.
     *
     * @param connection the JDBC connection
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
     * Creates the USERS table if it does not already exist
     * and ensures that a default admin account exists.
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
                phone_number VARCHAR(30),
                team_leader_id BIGINT,
                CONSTRAINT fk_users_teamleader
                    FOREIGN KEY (team_leader_id) REFERENCES USERS(id)
                    ON DELETE SET NULL
            )
            """;

        try (Statement st = connection.createStatement()) {
            st.execute(createSql);
        }

        ensureAdminExists();
    }

    /**
     * Ensures that at least one admin user exists.
     * <p>
     * If no admin is found, a default admin account is inserted.
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
     * Saves a new employee with pending verification status.
     *
     * @param email the employee email
     * @param name the employee first name
     * @param surname the employee surname
     * @param phone_number the employee phone number
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

    /**
     * Returns an employee by ID.
     *
     * @param id the employee ID
     * @return the matching employee, or {@code null} if not found
     */
    public Employee getEmployeeById(Long id) {

        String sql = "SELECT * FROM USERS WHERE id = ? AND user_role = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.setString(2, Roles.employee.toString());

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            Employee e = new Employee();
            e.setId(rs.getLong("id"));
            e.setEmail(rs.getString("email"));
            e.setPassword(rs.getString("password"));
            e.setName(rs.getString("employee_name"));
            e.setSurname(rs.getString("employee_surname"));
            e.setPhoneNumber(rs.getString("phone_number"));
            e.setTeamLeaderId((Long) rs.getObject("team_leader_id"));
            e.setVerificationStatus(
                    VerificationStatus.valueOf(rs.getString("verification_status"))
            );

            return e;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Restores a previously deleted employee.
     * <p>
     * This method is primarily intended for undo operations.
     *
     * @param e the employee to restore
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
     * @return a list of employees
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
                        VerificationStatus.valueOf(rs.getString("verification_status"))
                );
                e.setPhoneNumber(rs.getString("phone_number"));
                e.setTeamLeaderId((Long) rs.getObject("team_leader_id"));
                list.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    /**
     * Returns a user by email.
     *
     * @param email the user email
     * @return the matching user, or {@code null} if not found
     */
    public User getUserByEmail(String email) {

        String sql = "SELECT * FROM USERS WHERE email = ?";
        AdminFactory adminFactory = new AdminFactory();
        EmployeeFactory employeeFactory = new EmployeeFactory();
        TeamLeaderFactory teamLeaderFactory = new TeamLeaderFactory();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            String role = rs.getString("user_role");

            if (role.equals(Roles.admin.toString())) {
                Admin admin = (Admin) adminFactory.createUser();
                admin.setEmail(email);
                admin.setRole(role);
                return admin;
            }

            if (role.equals(Roles.teamleader.toString())) {
                TeamLeader tl = (TeamLeader) teamLeaderFactory.createUser();
                tl.setId(rs.getLong("id"));
                tl.setEmail(email);
                tl.setPassword(rs.getString("password"));
                tl.setName(rs.getString("employee_name"));
                tl.setSurname(rs.getString("employee_surname"));
                tl.setPhoneNumber(rs.getString("phone_number"));
                tl.setRole(role);
                return tl;
            }

            Employee e = (Employee) employeeFactory.createUser();
            e.setId(rs.getLong("id"));
            e.setEmail(email);
            e.setName(rs.getString("employee_name"));
            e.setSurname(rs.getString("employee_surname"));
            e.setPhoneNumber(rs.getString("phone_number"));
            e.setRole(role);
            e.setTeamLeaderId((Long) rs.getObject("team_leader_id"));
            e.setVerificationStatus(
                    VerificationStatus.valueOf(rs.getString("verification_status"))
            );
            return e;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets or updates the password for the given email.
     *
     * @param email the user email
     * @param password the password to set
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
     * Marks the employee with the given email as verified.
     *
     * @param email the employee email
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
     * Deletes a user by ID.
     *
     * @param id the user ID
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
     * Verifies whether the given email and password match an existing user.
     *
     * @param email the email
     * @param password the password
     * @return {@code true} if the credentials match, otherwise {@code false}
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
     * @param email the email to check
     * @return {@code true} if the email exists, otherwise {@code false}
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

    /**
     * Returns a team leader by ID.
     *
     * @param id the team leader ID
     * @return the matching team leader, or {@code null} if not found
     */
    public TeamLeader getTeamLeaderById(Long id) {

        String sql = "SELECT * FROM USERS WHERE id = ? AND user_role = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.setString(2, Roles.teamleader.toString());

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            TeamLeader tl = new TeamLeader();
            tl.setId(rs.getLong("id"));
            tl.setEmail(rs.getString("email"));
            tl.setPassword(rs.getString("password"));
            tl.setName(rs.getString("employee_name"));
            tl.setSurname(rs.getString("employee_surname"));
            tl.setPhoneNumber(rs.getString("phone_number"));

            return tl;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns all team leaders from the database.
     *
     * @return a list of team leaders
     */
    public List<TeamLeader> getAllTeamLeaders() {

        List<TeamLeader> list = new ArrayList<>();

        String sql = "SELECT * FROM USERS WHERE user_role = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, Roles.teamleader.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TeamLeader tl = new TeamLeader();
                tl.setId(rs.getLong("id"));
                tl.setEmail(rs.getString("email"));
                tl.setName(rs.getString("employee_name"));
                tl.setSurname(rs.getString("employee_surname"));
                tl.setPhoneNumber(rs.getString("phone_number"));
                list.add(tl);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    /**
     * Assigns an employee to a team leader.
     *
     * @param employeeId the employee ID
     * @param teamLeaderId the team leader ID
     * @return {@code true} if the assignment was updated successfully,
     * otherwise {@code false}
     */
    public boolean assignEmployeeToTeamLeader(Long employeeId, Long teamLeaderId) {

        String sql = """
            UPDATE USERS
            SET team_leader_id = ?
            WHERE id = ? AND user_role = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, teamLeaderId);
            ps.setLong(2, employeeId);
            ps.setString(3, Roles.employee.toString());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns all employees assigned to a specific team leader.
     *
     * @param teamLeaderId the team leader ID
     * @return a list of employees assigned to the given team leader
     */
    public List<Employee> getEmployeesByTeamLeaderId(Long teamLeaderId) {

        List<Employee> list = new ArrayList<>();

        String sql = """
            SELECT * FROM USERS
            WHERE user_role = ? AND team_leader_id = ?
            ORDER BY employee_name, employee_surname
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, Roles.employee.toString());
            ps.setLong(2, teamLeaderId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getLong("id"));
                e.setEmail(rs.getString("email"));
                e.setPassword(rs.getString("password"));
                e.setName(rs.getString("employee_name"));
                e.setSurname(rs.getString("employee_surname"));
                e.setPhoneNumber(rs.getString("phone_number"));
                e.setTeamLeaderId((Long) rs.getObject("team_leader_id"));
                e.setVerificationStatus(
                        VerificationStatus.valueOf(rs.getString("verification_status"))
                );
                list.add(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;

    }

    /**
     * Saves a new team leader.
     *
     * @param email the team leader email
     * @param name the team leader first name
     * @param surname the team leader surname
     * @param phoneNumber the team leader phone number
     */
    public void saveTeamLeader(String email, String name, String surname, String phoneNumber) {

        String sql = """
        INSERT INTO USERS
        (email, employee_name, employee_surname, user_role, verification_status, phone_number)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, name);
            ps.setString(3, surname);
            ps.setString(4, Roles.teamleader.toString());
            ps.setString(5, VerificationStatus.verified.name());
            ps.setString(6, phoneNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}