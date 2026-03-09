package interfaces;

/**
 * Defines a contract for controllers that can refresh their view data.
 */
public interface Refreshable {

    /**
     * Reloads data and refreshes the user interface.
     */
    void refresh();
}