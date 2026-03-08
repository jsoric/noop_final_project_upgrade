package helpers;

import entities.Employee;
import repositories.UserRepository;

import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Helper utilities for working with {@link DefaultTableModel} and employee data.
 */
public class TableModelHelper {

    /**
     * Clears the table model and repopulates it from the repository.
     *
     * @param defaultTableModel table model to refresh
     * @param userRepository repository providing the employee list
     */
    public static void refreshTableModel(
            DefaultTableModel defaultTableModel,
            UserRepository userRepository) {

        defaultTableModel.setRowCount(0);

        List<Employee> employeeList = userRepository.getAllEmployees();

        for (Employee employee : employeeList) {
            defaultTableModel.addRow(new Object[]{
                    employee.getId(),
                    employee.getName(),
                    employee.getSurname(),
                    employee.getEmail(),
                    employee.getPhoneNumber(),
                    employee.isVerified()
            });
        }
    }
}