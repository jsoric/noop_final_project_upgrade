package repositories;

import entities.Task;
import enums.TaskStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for TASKS table initialization
 * and task-related database operations.
 */
public class TaskRepository {

    private final Connection connection;

    /**
     * Creates a repository using an existing JDBC connection
     * and ensures that the TASKS table exists.
     *
     * @param connection the JDBC connection
     */
    public TaskRepository(Connection connection) {
        this.connection = connection;

        try {
            createTable();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create TASKS table.", e);
        }
    }

    /**
     * Creates the TASKS table if it does not already exist.
     *
     * @throws SQLException if the SQL execution fails
     */
    public void createTable() throws SQLException {
        String createSql = """
            CREATE TABLE IF NOT EXISTS TASKS (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                description CLOB,
                status VARCHAR(50) NOT NULL,
                employee_id BIGINT NOT NULL,
                deadline TIMESTAMP,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                CONSTRAINT fk_tasks_employee
                    FOREIGN KEY (employee_id) REFERENCES USERS(id)
                    ON DELETE CASCADE
            )
            """;

        try (Statement st = connection.createStatement()) {
            st.execute(createSql);
        }
    }

    /**
     * Saves a new task to the database.
     *
     * @param task the task to save
     * @return {@code true} if the task was inserted successfully,
     * otherwise {@code false}
     */
    public boolean createTask(Task task) {
        String sql = """
            INSERT INTO TASKS (title, description, status, employee_id, deadline, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus().name());
            ps.setLong(4, task.getEmployeeId());

            if (task.getDeadline() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(task.getDeadline()));
            } else {
                ps.setTimestamp(5, null);
            }

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    task.setId(rs.getLong(1));
                }
                return true;
            }

            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create task.", e);
        }
    }

    /**
     * Returns a task by its ID.
     *
     * @param id the task ID
     * @return the matching task, or {@code null} if not found
     */
    public Task getTaskById(Long id) {
        String sql = "SELECT * FROM TASKS WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapTask(rs);
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch task by id.", e);
        }
    }

    /**
     * Returns all tasks assigned to a specific employee.
     *
     * @param employeeId the employee ID
     * @return a list of tasks assigned to the employee
     */
    public List<Task> getTasksByEmployeeId(Long employeeId) {
        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT * FROM TASKS WHERE employee_id = ? ORDER BY created_at DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, employeeId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tasks.add(mapTask(rs));
            }

            return tasks;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch tasks by employee id.", e);
        }
    }

    /**
     * Updates the status of a task by its ID.
     *
     * @param taskId the task ID
     * @param newStatus the new task status
     * @return {@code true} if the task was updated successfully,
     * otherwise {@code false}
     */
    public boolean updateTaskStatus(Long taskId, TaskStatus newStatus) {
        String sql = """
            UPDATE TASKS
            SET status = ?, updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newStatus.name());
            ps.setLong(2, taskId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update task status.", e);
        }
    }

    /**
     * Updates the task status only if the task belongs to the given employee.
     *
     * @param taskId the task ID
     * @param employeeId the employee ID
     * @param newStatus the new task status
     * @return {@code true} if the task was updated successfully,
     * otherwise {@code false}
     */
    public boolean updateTaskStatusForEmployee(Long taskId, Long employeeId, TaskStatus newStatus) {
        String sql = """
            UPDATE TASKS
            SET status = ?, updated_at = CURRENT_TIMESTAMP
            WHERE id = ? AND employee_id = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newStatus.name());
            ps.setLong(2, taskId);
            ps.setLong(3, employeeId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update employee task status.", e);
        }
    }

    /**
     * Returns all tasks assigned to employees supervised by the given team leader.
     *
     * @param teamLeaderId the team leader ID
     * @return a list of tasks for the team leader's employees
     */
    public List<Task> getTasksByTeamLeaderId(Long teamLeaderId) {
        List<Task> tasks = new ArrayList<>();

        String sql = """
            SELECT t.*
            FROM TASKS t
            JOIN USERS u ON t.employee_id = u.id
            WHERE u.team_leader_id = ?
            ORDER BY t.created_at DESC
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, teamLeaderId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tasks.add(mapTask(rs));
            }

            return tasks;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch tasks by team leader id.", e);
        }
    }

    /**
     * Returns formatted task statistics for a given team leader.
     *
     * @param teamLeaderId the team leader ID
     * @return a formatted statistics string grouped by task status
     */
    public String getTaskStatsByTeamLeaderId(Long teamLeaderId) {
        String sql = """
            SELECT status, COUNT(*) as total
            FROM TASKS t
            JOIN USERS u ON t.employee_id = u.id
            WHERE u.team_leader_id = ?
            GROUP BY status
            """;

        int inProgress = 0;
        int inReview = 0;
        int done = 0;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, teamLeaderId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String status = rs.getString("status");
                int total = rs.getInt("total");

                switch (status) {
                    case "IN_PROGRESS" -> inProgress = total;
                    case "IN_REVIEW" -> inReview = total;
                    case "DONE" -> done = total;
                }
            }

            return "In Progress: " + inProgress +
                    " | In Review: " + inReview +
                    " | Done: " + done;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch task stats by team leader id.", e);
        }
    }

    /**
     * Maps a {@link ResultSet} row to a {@link Task} object.
     *
     * @param rs the result set containing task data
     * @return the mapped task
     * @throws SQLException if reading data from the result set fails
     */
    private Task mapTask(ResultSet rs) throws SQLException {
        Timestamp createdTimestamp = rs.getTimestamp("created_at");
        Timestamp updatedTimestamp = rs.getTimestamp("updated_at");
        Timestamp deadlineTimestamp = rs.getTimestamp("deadline");

        LocalDateTime deadline = deadlineTimestamp != null
                ? deadlineTimestamp.toLocalDateTime()
                : null;

        LocalDateTime createdAt = createdTimestamp != null
                ? createdTimestamp.toLocalDateTime()
                : null;

        LocalDateTime updatedAt = updatedTimestamp != null
                ? updatedTimestamp.toLocalDateTime()
                : null;

        return new Task(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                TaskStatus.fromString(rs.getString("status")),
                rs.getLong("employee_id"),
                deadline,
                createdAt,
                updatedAt
        );
    }

    public void restoreTask(Task task) {
        String sql = """
        INSERT INTO TASKS
        (id, title, description, status, employee_id, deadline, created_at, updated_at)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, task.getId());
            ps.setString(2, task.getTitle());
            ps.setString(3, task.getDescription());
            ps.setString(4, task.getStatus().name());
            ps.setLong(5, task.getEmployeeId());

            if (task.getDeadline() != null) {
                ps.setTimestamp(6, Timestamp.valueOf(task.getDeadline()));
            } else {
                ps.setTimestamp(6, null);
            }

            if (task.getCreatedAt() != null) {
                ps.setTimestamp(7, Timestamp.valueOf(task.getCreatedAt()));
            } else {
                ps.setTimestamp(7, null);
            }

            if (task.getUpdatedAt() != null) {
                ps.setTimestamp(8, Timestamp.valueOf(task.getUpdatedAt()));
            } else {
                ps.setTimestamp(8, null);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to restore task.", e);
        }
    }
}