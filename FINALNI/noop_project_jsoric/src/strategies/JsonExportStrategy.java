package strategies;

import entities.Employee;
import interfaces.ExportDataStrategy;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * JSON export strategy for employee data.
 */
public class JsonExportStrategy implements ExportDataStrategy {

    /**
     * Exports employees to a simple JSON array.
     *
     * @param employees employees to export
     * @param file output file
     * @throws Exception if writing fails
     */
    @Override
    public void exportData(List<Employee> employees, File file) throws Exception {
        try (FileWriter writer = new FileWriter(file)) {

            writer.write("[\n");

            for (int i = 0; i < employees.size(); i++) {
                Employee e = employees.get(i);

                writer.write("  {\n");
                writer.write("    \"id\": " + e.getId() + ",\n");
                writer.write("    \"name\": \"" + e.getName() + "\",\n");
                writer.write("    \"surname\": \"" + e.getSurname() + "\",\n");
                writer.write("    \"email\": \"" + e.getEmail() + "\",\n");
                writer.write("    \"verified\": " + e.isVerified() + "\n");
                writer.write("  }");

                if (i < employees.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }

            writer.write("]");
        }
    }
}