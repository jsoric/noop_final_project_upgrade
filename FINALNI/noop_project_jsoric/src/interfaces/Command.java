package interfaces;

/**
 * Command interface for Command pattern.
 * <p>
 * Implementations execute an operation and optionally support undo.
 * </p>
 */
public interface Command {

    /**
     * Executes the command.
     *
     * @return {@code true} if executed successfully; {@code false} otherwise
     */
    boolean execute();

    /**
     * Undoes the command operation (if possible).
     */
    void undo();
}