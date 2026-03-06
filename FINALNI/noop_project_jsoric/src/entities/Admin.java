package entities;

import enums.Roles;

/**
 * Represents an administrator user in the system.
 *
 * An admin inherits authentication properties from {@link User}
 * and has the role set to "admin".
 */
public class Admin extends User {

    /**
     * Creates an admin with default role.
     */
    public Admin() {
        super(Roles.admin.toString());
    }

    /**
     * Creates an admin with email and password.
     *
     * @param email admin email
     * @param password admin password
     */
    public Admin(String email, String password) {
        super(Roles.admin.toString(), email, password);
    }
}