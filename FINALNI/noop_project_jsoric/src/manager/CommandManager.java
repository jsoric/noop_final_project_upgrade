package manager;

import interfaces.Command;

import java.util.Stack;

/**
 * Manages execution and undo/redo stacks for commands.
 */
public class CommandManager {

    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    /**
     * Creates a new manager with empty undo/redo stacks.
     */
    public CommandManager() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    /**
     * Executes a command and, if successful, pushes it onto the undo stack and clears redo.
     *
     * @param command command to execute
     */
    public void executeCommand(Command command) {

        if (command.execute()) {
            undoStack.push(command);
            redoStack.clear();
        }
    }

    /**
     * Undoes the last executed command (if any) and pushes it onto the redo stack.
     */
    public void undo() {
        if (undoStack.isEmpty())
            return;

        var undoCommand = undoStack.pop();
        undoCommand.undo();

        redoStack.push(undoCommand);
    }

    /**
     * Re-executes the last undone command (if any) and pushes it back onto the undo stack.
     */
    public void redo() {
        if (redoStack.isEmpty())
            return;

        var redoCommand = redoStack.pop();

        if (redoCommand.execute()) {
            undoStack.push(redoCommand);
        }
    }
}