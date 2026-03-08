package strategies;

import entities.Employee;
import interfaces.ExportDataStrategy;

import java.io.File;
import java.util.List;

/**
 * Context for exporting employee data using a selected {@link ExportDataStrategy}.
 */
public class ExportContext {

    private ExportDataStrategy strategy;

    /**
     * Sets the export strategy.
     *
     * @param strategy strategy implementation
     */
    public void setStrategy(ExportDataStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Exports employees using the currently set strategy.
     *
     * @param employees employees to export
     * @param file output file
     * @throws Exception if export fails
     */
    public void export(List<Employee> employees, File file) throws Exception {
        strategy.exportData(employees, file);
    }
}