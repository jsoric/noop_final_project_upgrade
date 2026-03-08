package interfaces;

import entities.Employee;
import java.io.File;
import java.util.List;

/**
 * Strategy interface for exporting employee data to a file.
 */
public interface ExportDataStrategy {

    /**
     * Exports employee data to the given file.
     *
     * @param employees employees to export
     * @param file output file
     * @throws Exception if export fails
     */
    void exportData(List<Employee> employees, File file) throws Exception;

}