package repositories;

import entities.Task;
import enums.TaskStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for TASKS table initialization and task CRUD operations.
 */
public class TaskRepository {

    private final Connection connection;

    /**
     * Creates repository using an existing JDBC connection and ensures TASKS table exists.
     *
     * @param connection JDBC connection
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
     * @throws SQLException if SQL execution fails
     */
    public void createTable() throws SQLException {
        String createSql = """
            CREATE TABLE IF NOT EXISTS TASKS (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                description CLOB,
                status VARCHAR(50) NOT NULL,
                employee_id BIGINT NOT NULL,
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
     * @param task task to save
     * @return true if inserted successfully, false otherwise
     */
    public boolean createTask(Task task) {
        String sql = """
            INSERT INTO TASKS (title, description, status, employee_id, created_at, updated_at)
            VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus().name());
            ps.setLong(4, task.getEmployeeId());

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
     * Returns a task by its id.
     *
     * @param id task id
     * @return task or null if not found
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
     * @param employeeId employee id
     * @return list of tasks
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
     * Returns all tasks from the database.
     *
     * @return list of all tasks
     */
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT * FROM TASKS ORDER BY created_at DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tasks.add(mapTask(rs));
            }

            return tasks;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all tasks.", e);
        }
    }

    /**
     * Updates only the task status by task id.
     *
     * @param taskId task id
     * @param newStatus new task status
     * @return true if updated successfully, false otherwise
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
     * Updates task status only if the task belongs to the given employee.
     * This is useful for securing employee-side status changes.
     *
     * @param taskId task id
     * @param employeeId employee id
     * @param newStatus new status
     * @return true if updated successfully, false otherwise
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
     * Deletes a task by id.
     *
     * @param taskId task id
     * @return true if deleted, false otherwise
     */
    public boolean deleteTask(Long taskId) {
        String sql = "DELETE FROM TASKS WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, taskId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete task.", e);
        }
    }

    /**
     * Maps a result set row to a Task object.
     *
     * @param rs result set
     * @return mapped task
     * @throws SQLException if mapping fails
     */
    private Task mapTask(ResultSet rs) throws SQLException {
        Timestamp createdTimestamp = rs.getTimestamp("created_at");
        Timestamp updatedTimestamp = rs.getTimestamp("updated_at");

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
                createdAt,
                updatedAt
        );
    }
}