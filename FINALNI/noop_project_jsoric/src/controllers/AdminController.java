package controllers;

import commands.DeleteRowCommand;
import configuration.DatabaseConnection;
import entities.Employee;
import manager.CommandManager;
import repositories.TaskRepository;
import repositories.UserRepository;
import service.EmployeeOnboardingService;
import view.LoginView;
import view.panels.*;
import strategies.*;
import view.AddTaskView;
import view.employee.EmployeeDetailsView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Controller for the Admin view.
 * <p>
 * Wires UI actions (add, delete, undo, export, sign out, search filter, task actions)
 * to repository operations.
 * </p>
 */
public class AdminController {

    private AdminViewTablePanel tablePanel;
    private AdminViewFormPanel formPanel;
    private AdminViewBottomPanel bottomPanel;
    private AdminViewTopPanel topPanel;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    private CommandManager commandManager = new CommandManager();
    private TableRowSorter<DefaultTableModel> sorter;

    /**
     * Creates an Admin controller and initializes UI listeners.
     *
     * @param tablePanel table/list panel
     * @param formPanel input form panel
     * @param bottomPanel bottom actions panel
     * @param userRepository repository for persistence
     * @param topPanel top/search panel
     */
    public AdminController(AdminViewTablePanel tablePanel,
                           AdminViewFormPanel formPanel,
                           AdminViewBottomPanel bottomPanel,
                           UserRepository userRepository,
                           AdminViewTopPanel topPanel) {

        this.tablePanel = tablePanel;
        this.formPanel = formPanel;
        this.bottomPanel = bottomPanel;
        this.userRepository = userRepository;
        this.topPanel = topPanel;
        this.taskRepository = new TaskRepository(DatabaseConnection.getConnection());
        this.sorter = (TableRowSorter<DefaultTableModel>)
                tablePanel.getTable().getRowSorter();

        init();
        loadEmployees();
    }

    /**
     * Registers all UI listeners.
     */
    private void init() {
        formPanel.getSaveButton().addActionListener(e -> saveEmployee());
        bottomPanel.getDeleteBtn().addActionListener(e -> delete());
        bottomPanel.getUndoBtn().addActionListener(e -> undo());
        bottomPanel.getRedoBtn().addActionListener(e -> redo());
        bottomPanel.getExportBtn().addActionListener(e -> exportData());
        bottomPanel.getAddTaskBtn().addActionListener(e -> openAddTaskView());
        bottomPanel.getViewDetailsBtn().addActionListener(e -> openEmployeeDetailsView());
        topPanel.getSignOutBtn().addActionListener(e -> signOut());

        topPanel.getSearchField().getDocument().addDocumentListener(
                new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) { filter(); }
                    public void removeUpdate(DocumentEvent e) { filter(); }
                    public void changedUpdate(DocumentEvent e) { filter(); }

                    private void filter() {
                        String text = topPanel.getSearchField().getText().trim();
                        if (text.isEmpty()) {
                            sorter.setRowFilter(null);
                        } else {
                            sorter.setRowFilter(
                                    RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text), 1, 2, 3)
                            );
                        }
                    }
                }
        );
    }

    /**
     * Saves a new employee based on form input.
     */
    private void saveEmployee() {
        JButton saveBtn = formPanel.getSaveButton();

        try {
            String email = formPanel.getEmail().trim();
            String phone_number = formPanel.getPhone().trim();
            String name = formPanel.getName().trim();
            String surname = formPanel.getSurname().trim();

            if (email.isEmpty() || name.isEmpty() || surname.isEmpty() || phone_number.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "All fields must be filled.",
                        "Invalid input",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(
                        null,
                        "Please enter a valid email address (example: user@example.com).",
                        "Invalid email format",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (userRepository.emailExists(email)) {
                JOptionPane.showMessageDialog(
                        null,
                        "An employee with this email already exists!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (!isValidPhone(phone_number)) {
                JOptionPane.showMessageDialog(
                        null,
                        "Phone number format is invalid.\nExample: +385991234567",
                        "Invalid phone",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            saveBtn.setEnabled(false);

            userRepository.saveEmployee(email, name, surname, phone_number);
            loadEmployees();

            int choice = JOptionPane.showConfirmDialog(
                    null,
                    "Employee was successfully added.\nDo you want to send an verification email?",
                    "Send onboarding email",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                EmployeeOnboardingService onboarding =
                        new EmployeeOnboardingService(userRepository);
                onboarding.onboardEmployee(name, email);
            }

            clearForm();

            JOptionPane.showMessageDialog(
                    null,
                    "Operation completed.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            saveBtn.setEnabled(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "An error occurred while adding the employee.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            saveBtn.setEnabled(true);
        }
    }

    /**
     * Loads all employees from repository into the table model.
     */
    private void loadEmployees() {
        DefaultTableModel model = tablePanel.getModel();
        model.setRowCount(0);

        for (Employee e : userRepository.getAllEmployees()) {
            model.addRow(new Object[]{
                    e.getId(),
                    e.getName(),
                    e.getSurname(),
                    e.getEmail(),
                    e.getPhoneNumber(),
                    e.isVerified()
            });
        }
    }

    /**
     * Public refresh method for other views.
     */
    public void refreshTable() {
        loadEmployees();
    }

    /**
     * Executes delete row via command manager.
     */
    private void delete() {
        commandManager.executeCommand(
                new DeleteRowCommand(
                        tablePanel.getModel(),
                        tablePanel.getTable(),
                        userRepository
                )
        );
    }

    /**
     * Undoes the last executed command.
     */
    private void undo() {
        commandManager.undo();
    }

    /**
     * Re-executes the last undone command.
     */
    private void redo() {
        commandManager.redo();
    }

    /**
     * Opens add task window.
     * Temporary placeholder until AddTaskView is implemented.
     */
    private void openAddTaskView() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(bottomPanel);

        AddTaskView addTaskView = new AddTaskView(parent);
        new AddTaskController(addTaskView, userRepository, taskRepository);

        addTaskView.setVisible(true);
    }
    /**
     * Opens employee details window.
     * Temporary placeholder until EmployeeDetailsView is implemented.
     */
    private void openEmployeeDetailsView() {
        int selectedRow = tablePanel.getTable().getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    null,
                    "Please select an employee first.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int modelRow = tablePanel.getTable().convertRowIndexToModel(selectedRow);
        Long employeeId = (Long) tablePanel.getModel().getValueAt(modelRow, 0);

        EmployeeDetailsView detailsView = new EmployeeDetailsView();
        new EmployeeDetailsController(detailsView, employeeId, userRepository, taskRepository);

        detailsView.setVisible(true);
    }

    /**
     * Closes the admin window and returns to login.
     */
    private void signOut() {
        SwingUtilities.getWindowAncestor(topPanel).dispose();
        new LoginView(userRepository).setVisible(true);
    }

    /**
     * Exports employees to a chosen format.
     */
    private void exportData() {
        try {
            String[] formats = {"CSV", "TXT", "JSON"};

            String selected = (String) JOptionPane.showInputDialog(
                    null,
                    "Choose export format:",
                    "Export",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    formats,
                    formats[0]
            );

            if (selected == null) return;

            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

            ExportContext context = new ExportContext();

            switch (selected) {
                case "CSV" -> context.setStrategy(new CsvExportStrategy());
                case "TXT" -> context.setStrategy(new TxtExportStrategy());
                case "JSON" -> context.setStrategy(new JsonExportStrategy());
            }

            context.export(
                    userRepository.getAllEmployees(),
                    chooser.getSelectedFile()
            );

            JOptionPane.showMessageDialog(
                    null,
                    "Export successful!",
                    "Export",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Export failed!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Validates email using a simple regex.
     *
     * @param email email string
     * @return true if email matches expected format
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }

    /**
     * Validates a phone number using a simple regular expression.
     *
     * @param phone_number phone number string
     * @return true if phone number format is valid
     */
    private boolean isValidPhone(String phone_number) {
        String phoneRegex = "^(\\+\\d{1,3})?\\d{6,12}$";
        return phone_number != null && phone_number.matches(phoneRegex);
    }

    /**
     * Clears form fields.
     */
    private void clearForm() {
        formPanel.getEmailField().setText("");
        formPanel.getNameField().setText("");
        formPanel.getSurnameField().setText("");
        formPanel.getPhoneField().setText("");

        formPanel.getEmailField().requestFocusInWindow();
    }
}