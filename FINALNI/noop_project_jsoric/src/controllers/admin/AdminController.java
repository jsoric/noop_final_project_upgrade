package controllers.admin;

import commands.DeleteRowCommand;
import entities.Employee;
import manager.CommandManager;
import repositories.TaskRepository;
import repositories.UserRepository;
import service.EmployeeOnboardingService;
import strategies.CsvExportStrategy;
import strategies.ExportContext;
import strategies.JsonExportStrategy;
import strategies.TxtExportStrategy;
import view.AddTaskView;
import view.LoginView;
import view.employee.EmployeeDetailsView;
import view.panels.AdminViewBottomPanel;
import view.panels.AdminViewFormPanel;
import view.panels.AdminViewTablePanel;
import view.panels.AdminViewTopPanel;
import interfaces.Refreshable;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Controller responsible for handling actions in the admin view.
 * <p>
 * It connects user interface actions such as adding employees, deleting rows,
 * undo/redo operations, exporting data, searching, task management and sign-out
 * functionality with the corresponding repository and service operations.
 * </p>
 */
public class AdminController implements Refreshable {

    private AdminViewTablePanel tablePanel;
    private AdminViewFormPanel formPanel;
    private AdminViewBottomPanel bottomPanel;
    private AdminViewTopPanel topPanel;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    private CommandManager commandManager = new CommandManager();
    private TableRowSorter<DefaultTableModel> sorter;

    /**
     * Creates the admin controller and initializes all required listeners and data.
     *
     * @param tablePanel panel that displays employee data in a table
     * @param formPanel panel containing employee input fields
     * @param bottomPanel panel containing action buttons
     * @param userRepository repository used for employee persistence
     * @param topPanel panel containing top-level controls such as search and sign out
     */
    public AdminController(AdminViewTablePanel tablePanel,
                           AdminViewFormPanel formPanel,
                           AdminViewBottomPanel bottomPanel,
                           UserRepository userRepository,
                           TaskRepository taskRepository,
                           AdminViewTopPanel topPanel) {

        this.tablePanel = tablePanel;
        this.formPanel = formPanel;
        this.bottomPanel = bottomPanel;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.topPanel = topPanel;
        this.sorter = (TableRowSorter<DefaultTableModel>)
                tablePanel.getTable().getRowSorter();

        init();
        loadEmployees();
    }

    /**
     * Registers all user interface listeners.
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
        bottomPanel.getAssignTeamLeaderBtn().addActionListener(e -> assignTeamLeader());
        formPanel.getAddTeamLeaderButton().addActionListener(e -> saveTeamLeader());
        bottomPanel.getRefreshBtn().addActionListener(e -> refresh());

        topPanel.getSearchField().getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        filter();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        filter();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        filter();
                    }

                    /**
                     * Filters table rows based on the search field value.
                     */
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
     * Validates form input and saves a new employee.
     * <p>
     * If validation succeeds, the employee is stored in the repository,
     * the table is refreshed and the user may optionally send a verification email.
     * </p>
     */
    private void saveEmployee() {
        JButton saveBtn = formPanel.getSaveButton();

        try {
            PersonFormData data = validatePersonForm();
            if (data == null) {
                return;
            }

            saveBtn.setEnabled(false);

            userRepository.saveEmployee(
                    data.getEmail(),
                    data.getName(),
                    data.getSurname(),
                    data.getPhoneNumber()
            );

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
                onboarding.onboardEmployee(data.getName(), data.getEmail());
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
     * Loads all employees from the repository into the table model.
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
     * Refreshes the employee table.
     */
    @Override
    public void refresh() {
        loadEmployees();
    }
    /**
     * Deletes the selected employee row using the command manager.
     */
    private void delete() {
        commandManager.executeCommand(
                new DeleteRowCommand(
                        tablePanel.getModel(),
                        tablePanel.getTable(),
                        userRepository,
                        taskRepository
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
     * Opens the window for creating a new task.
     */
    private void openAddTaskView() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(bottomPanel);

        AddTaskView addTaskView = new AddTaskView(parent);
        new AddTaskController(addTaskView, userRepository, taskRepository);

        addTaskView.setVisible(true);
    }

    /**
     * Opens the selected employee details view.
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
     * Closes the admin window and opens the login view.
     */
    private void signOut() {
        SwingUtilities.getWindowAncestor(topPanel).dispose();

        LoginView loginView = new LoginView(userRepository);
        new LoginController(loginView, userRepository, taskRepository);

        loginView.setVisible(true);
    }

    /**
     * Exports employee data using the format selected by the user.
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

            if (selected == null) {
                return;
            }

            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
                return;
            }

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
     * Validates an email address using a simple regular expression.
     *
     * @param email email address to validate
     * @return {@code true} if the email matches the expected format;
     * {@code false} otherwise
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }

    /**
     * Validates a phone number using a simple regular expression.
     *
     * @param phone_number phone number to validate
     * @return {@code true} if the phone number format is valid;
     * {@code false} otherwise
     */
    private boolean isValidPhone(String phone_number) {
        String phoneRegex = "^(\\+\\d{1,3})?\\d{6,12}$";
        return phone_number != null && phone_number.matches(phoneRegex);
    }

    /**
     * Clears all form fields and places focus back on the email field.
     */
    private void clearForm() {
        formPanel.getEmailField().setText("");
        formPanel.getNameField().setText("");
        formPanel.getSurnameField().setText("");
        formPanel.getPhoneField().setText("");

        formPanel.getEmailField().requestFocusInWindow();
    }

    /**
     * Validates form input and saves a new team leader.
     * <p>
     * If validation succeeds, the team leader is stored in the repository
     * and the user may optionally send a login email.
     * </p>
     */
    private void saveTeamLeader() {
        JButton addTeamLeaderBtn = formPanel.getAddTeamLeaderButton();

        try {
            PersonFormData data = validatePersonForm();
            if (data == null) {
                return;
            }

            addTeamLeaderBtn.setEnabled(false);

            userRepository.saveTeamLeader(
                    data.getEmail(),
                    data.getName(),
                    data.getSurname(),
                    data.getPhoneNumber()
            );

            int choice = JOptionPane.showConfirmDialog(
                    null,
                    "Team leader was successfully added.\nDo you want to send a login email?",
                    "Send onboarding email",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                EmployeeOnboardingService onboarding =
                        new EmployeeOnboardingService(userRepository);
                onboarding.onboardEmployee(data.getName(), data.getEmail());
            }

            clearForm();

            JOptionPane.showMessageDialog(
                    null,
                    "Operation completed.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            addTeamLeaderBtn.setEnabled(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "An error occurred while adding the team leader.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            addTeamLeaderBtn.setEnabled(true);
        }
    }
    /**
     * Assigns the selected employee to a team leader.
     */
    private void assignTeamLeader() {
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

        Employee selectedEmployee = userRepository.getEmployeeById(employeeId);

        if (selectedEmployee == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Selected employee not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        java.util.List<entities.TeamLeader> teamLeaders = userRepository.getAllTeamLeaders();

        if (teamLeaders.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No team leaders available. Please create a team leader first.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        entities.TeamLeader selectedTeamLeader = (entities.TeamLeader) JOptionPane.showInputDialog(
                null,
                "Select team leader for employee: " + selectedEmployee.getName() + " " + selectedEmployee.getSurname(),
                "Assign Team Leader",
                JOptionPane.QUESTION_MESSAGE,
                null,
                teamLeaders.toArray(),
                teamLeaders.get(0)
        );

        if (selectedTeamLeader == null) {
            return;
        }

        boolean success = userRepository.assignEmployeeToTeamLeader(
                employeeId,
                selectedTeamLeader.getId()
        );

        if (success) {
            JOptionPane.showMessageDialog(
                    null,
                    "Employee assigned to team leader successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            loadEmployees();
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Failed to assign team leader.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Simple holder for validated form input data.
     */
    private static class PersonFormData {
        private final String email;
        private final String phoneNumber;
        private final String name;
        private final String surname;

        /**
         * Creates a validated form data object.
         *
         * @param email entered email
         * @param phoneNumber entered phone number
         * @param name entered first name
         * @param surname entered surname
         */
        public PersonFormData(String email, String phoneNumber, String name, String surname) {
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.name = name;
            this.surname = surname;
        }

        /**
         * Returns the email.
         *
         * @return the email
         */
        public String getEmail() {
            return email;
        }

        /**
         * Returns the phone number.
         *
         * @return the phone number
         */
        public String getPhoneNumber() {
            return phoneNumber;
        }

        /**
         * Returns the first name.
         *
         * @return the first name
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the surname.
         *
         * @return the surname
         */
        public String getSurname() {
            return surname;
        }
    }

    /**
     * Reads and validates the shared person form data.
     *
     * @return validated form data, or {@code null} if validation fails
     */
    private PersonFormData validatePersonForm() {
        String email = formPanel.getEmail().trim();
        String phoneNumber = formPanel.getPhone().trim();
        String name = formPanel.getName().trim();
        String surname = formPanel.getSurname().trim();

        if (email.isEmpty() || name.isEmpty() || surname.isEmpty() || phoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "All fields must be filled.",
                    "Invalid input",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(
                    null,
                    "Please enter a valid email address (example: user@example.com).",
                    "Invalid email format",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        if (userRepository.emailExists(email)) {
            JOptionPane.showMessageDialog(
                    null,
                    "A user with this email already exists!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        if (!isValidPhone(phoneNumber)) {
            JOptionPane.showMessageDialog(
                    null,
                    "Phone number format is invalid.\nExample: +385991234567",
                    "Invalid phone",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        return new PersonFormData(email, phoneNumber, name, surname);
    }
}