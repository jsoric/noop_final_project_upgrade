package enums;

/**
 * Status values for employee tasks.
 */
public enum TaskStatus {
    IN_PROGRESS("In Progress"),
    IN_REVIEW("In Review"),
    DONE("Done");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns a user-friendly text for UI display.
     *
     * @return display name
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Converts database string value to enum.
     *
     * @param text database value
     * @return matching task status
     * @throws IllegalArgumentException if no match is found
     */
    public static TaskStatus fromString(String text) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.name().equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No TaskStatus constant found for value: " + text);
    }
}