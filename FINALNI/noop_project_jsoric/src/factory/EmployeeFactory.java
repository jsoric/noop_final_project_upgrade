package factory;

import entities.Employee;
import entities.User;

/**
 * Factory for creating {@link Employee} instances.
 */
public class EmployeeFactory extends UserFactory {

    /**
     * Creates a new {@link Employee}.
     *
     * @return new employee instance as {@link User}
     */
    @Override
    public User createUser() {
        return new Employee();
    }
}