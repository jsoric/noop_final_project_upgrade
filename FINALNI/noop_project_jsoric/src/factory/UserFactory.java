package factory;

import entities.User;

/**
 * Abstract factory base for creating user objects.
 */
public abstract class UserFactory {

    /**
     * Creates a new user instance.
     *
     * @return created user as {@link User}
     */
    public abstract User createUser();
}