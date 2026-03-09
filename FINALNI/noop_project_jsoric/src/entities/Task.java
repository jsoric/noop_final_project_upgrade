package entities;

import enums.TaskStatus;

import java.time.LocalDateTime;

/**
 * Represents a task assigned to an employee.
 */
public class Task {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long employeeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deadline;

    /**
     * Creates an empty task instance.
     */
    public Task() {
    }

    /**
     * Creates a new task before it is saved to the database.
     *
     * @param title the task title
     * @param description the task description
     * @param status the task status
     * @param employeeId the assigned employee ID
     * @param deadline the task deadline
     */
    public Task(String title, String description, TaskStatus status, Long employeeId, LocalDateTime deadline) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.employeeId = employeeId;
        this.deadline = deadline;
    }

    /**
     * Creates a task loaded from the database.
     *
     * @param id the task ID
     * @param title the task title
     * @param description the task description
     * @param status the task status
     * @param employeeId the assigned employee ID
     * @param deadline the task deadline
     * @param createdAt the creation timestamp
     * @param updatedAt the last update timestamp
     */
    public Task(Long id, String title, String description, TaskStatus status,
                Long employeeId, LocalDateTime deadline, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.employeeId = employeeId;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Returns the task ID.
     *
     * @return the task ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the task ID.
     *
     * @param id the task ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the task title.
     *
     * @return the task title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the task title.
     *
     * @param title the task title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the task description.
     *
     * @param description the task description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the task status.
     *
     * @return the task status
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Sets the task status.
     *
     * @param status the task status
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Returns the assigned employee ID.
     *
     * @return the assigned employee ID
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the assigned employee ID.
     *
     * @param employeeId the assigned employee ID
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Returns the creation timestamp.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the last update timestamp.
     *
     * @return the last update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp.
     *
     * @param updatedAt the last update timestamp
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Returns the task deadline.
     *
     * @return the task deadline
     */
    public LocalDateTime getDeadline() {
        return deadline;
    }

    /**
     * Sets the task deadline.
     *
     * @param deadline the task deadline
     */
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    /**
     * Checks whether the task is overdue.
     *
     * @return {@code true} if the task deadline date has passed and the task is not done,
     * otherwise {@code false}
     */
    public boolean isOverdue() {
        return deadline != null
                && status != TaskStatus.DONE
                && deadline.toLocalDate().isBefore(LocalDateTime.now().toLocalDate());
    }

    /**
     * Returns the deadline text for UI display.
     * <p>
     * If the deadline exists and the task is overdue, the returned value includes
     * an overdue marker. If no deadline exists, {@code "-"} is returned.
     *
     * @return formatted deadline text for display
     */
    public String getDisplayDeadline() {
        if (deadline == null) {
            return "-";
        }

        String deadlineText = deadline.toLocalDate().toString();

        if (isOverdue()) {
            return deadlineText + " (Overdue)";
        }

        return deadlineText;
    }

    /**
     * Returns the display status of the task.
     * <p>
     * If the task is overdue, returns {@code "Overdue"}; otherwise,
     * returns the display name of the current status.
     *
     * @return the display status
     */
    public String getDisplayStatus() {
        if (isOverdue()) {
            return "Overdue";
        }
        return status.getDisplayName();
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", employeeId=" + employeeId +
                '}';
    }
}