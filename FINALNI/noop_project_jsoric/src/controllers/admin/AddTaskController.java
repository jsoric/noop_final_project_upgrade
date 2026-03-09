package controllers.admin;

import entities.Employee;
import entities.Task;
import enums.TaskStatus;
import repositories.TaskRepository;
import repositories.UserRepository;
import view.AddTaskView;

import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Controller responsible for handling actions in {@link AddTaskView}.
 * <p>
 * It loads available employees, validates user input and saves newly created
 * tasks through the repository layer.
 */
public class AddTaskController {

    private static final DateTimeFormatter DEADLINE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final AddTaskView view;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    /**
     * Creates a controller for the add-task view.
     *
     * @param view view used for task input and user interaction
     * @param userRepository repository used to load employees
     * @param taskRepository repository used to persist tasks
     */
    public AddTaskController(AddTaskView view,
                             UserRepository userRepository,
                             TaskRepository taskRepository) {
        this.view = view;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;

        init();
        loadEmployees();
    }

    /**
     * Initializes view actions.
     */
    private void init() {
        view.getSaveButton().addActionListener(e -> saveTask());
        view.getCancelButton().addActionListener(e -> view.dispose());
    }

    /**
     * Loads all employees and displays them in the view.
     */
    private void loadEmployees() {
        view.setEmployees(userRepository.getAllEmployees());
    }

    /**
     * Validates form input and attempts to save a new task.
     * <p>
     * The method checks that the title is provided, an employee is selected,
     * and that the deadline is in the expected format when entered.
     * If validation passes, the task is created and stored through the repository.
     * </p>
     */
    private void saveTask() {
        String title = view.getTaskTitle();
        String description = view.getTaskDescription();
        TaskStatus status = TaskStatus.IN_PROGRESS;

        String deadlineText = view.getDeadlineText();
        LocalDateTime deadline = null;
        Employee selectedEmployee = view.getSelectedEmployee();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(
                    view,
                    "Task title is required.",
                    "Validation error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (selectedEmployee == null) {
            JOptionPane.showMessageDialog(
                    view,
                    "Please select an employee.",
                    "Validation error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!deadlineText.isEmpty()) {
            try {
                deadline = java.time.LocalDate.parse(deadlineText, DEADLINE_FORMATTER).atStartOfDay();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(
                        view,
                        "Deadline format is invalid.\nUse: yyyy-MM-dd",
                        "Validation error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }

        Task task = new Task(
                title,
                description,
                status,
                selectedEmployee.getId(),
                deadline
        );

        boolean success = taskRepository.createTask(task);

        if (success) {
            JOptionPane.showMessageDialog(
                    view,
                    "Task created successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            view.dispose();
        } else {
            JOptionPane.showMessageDialog(
                    view,
                    "Failed to create task.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}