package strategies;

import entities.Employee;
import interfaces.ExportDataStrategy;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * TXT export strategy for employee data (human-readable format).
 */
public class TxtExportStrategy implements ExportDataStrategy {

    /**
     * Exports employees to plain text with separators.
     *
     * @param employees employees to export
     * @param file output file
     * @throws Exception if writing fails
     */
    @Override
    public void exportData(List<Employee> employees, File file) throws Exception {
        try (FileWriter writer = new FileWriter(file)) {
            for (Employee e : employees) {
                writer.write(
                        "ID: " + e.getId() + "\n" +
                                "Name: " + e.getName() + "\n" +
                                "Surname: " + e.getSurname() + "\n" +
                                "Email: " + e.getEmail() + "\n" +
                                "Verified: " + e.isVerified() + "\n" +
                                "---------------------------\n"
                );
            }
        }
    }
}