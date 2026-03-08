package factory;

import entities.Admin;
import entities.User;

/**
 * Factory for creating {@link Admin} instances.
 */
public class AdminFactory extends UserFactory {

    /**
     * Creates a new {@link Admin}.
     *
     * @return new admin instance as {@link User}
     */
    @Override
    public User createUser() {
        return new Admin();
    }
}