package strategies;

import entities.Employee;
import interfaces.ExportDataStrategy;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * CSV export strategy for employee data.
 */
public class CsvExportStrategy implements ExportDataStrategy {

    /**
     * Exports employees to CSV with header row.
     *
     * @param employees employees to export
     * @param file output file
     * @throws Exception if writing fails
     */
    @Override
    public void exportData(List<Employee> employees, File file) throws Exception {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,name,surname,email,verified\n");

            for (Employee e : employees) {
                writer.write(
                        e.getId() + "," +
                                e.getName() + "," +
                                e.getSurname() + "," +
                                e.getEmail() + "," +
                                e.isVerified() + "\n"
                );
            }
        }
    }
}