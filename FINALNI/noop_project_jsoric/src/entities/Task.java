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

    /**
     * Default constructor.
     */
    public Task() {
    }

    /**
     * Constructor for creating a new task before saving to database.
     *
     * @param title task title
     * @param description task description
     * @param status task status
     * @param employeeId assigned employee id
     */
    public Task(String title, String description, TaskStatus status, Long employeeId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.employeeId = employeeId;
    }

    /**
     * Constructor for loading a task from database.
     *
     * @param id task id
     * @param title task title
     * @param description task description
     * @param status task status
     * @param employeeId assigned employee id
     * @param createdAt creation timestamp
     * @param updatedAt last update timestamp
     */
    public Task(Long id, String title, String description, TaskStatus status,
                Long employeeId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.employeeId = employeeId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * @return task id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id task id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return task title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title task title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description task description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return task status
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * @param status task status
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * @return assigned employee id
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId assigned employee id
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return creation time
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt creation time
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return last update time
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt last update time
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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