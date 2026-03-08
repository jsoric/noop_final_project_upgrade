package controllers;

import entities.Employee;
import entities.Task;
import repositories.TaskRepository;
import repositories.UserRepository;
import view.AddTaskView;

import javax.swing.*;

/**
 * Controller for AddTaskView.
 * Handles loading employees, validation and saving tasks.
 */
public class AddTaskController {

    private final AddTaskView view;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public AddTaskController(AddTaskView view,
                             UserRepository userRepository,
                             TaskRepository taskRepository) {
        this.view = view;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;

        init();
        loadEmployees();
    }

    private void init() {
        view.getSaveButton().addActionListener(e -> saveTask());
        view.getCancelButton().addActionListener(e -> view.dispose());
    }

    private void loadEmployees() {
        view.setEmployees(userRepository.getAllEmployees());
    }

    private void saveTask() {
        String title = view.getTaskTitle();
        String description = view.getTaskDescription();
        var status = view.getSelectedStatus();
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

        Task task = new Task(
                title,
                description,
                status,
                selectedEmployee.getId()
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