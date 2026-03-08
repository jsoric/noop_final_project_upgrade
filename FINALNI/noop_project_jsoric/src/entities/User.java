package entities;

/**
 * Base user entity that represents common properties
 * shared by all system users.
 *
 * This class stores authentication and identification data.
 */
public class User {

    private String role;
    private String email;
    private String password;

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Constructs a user with a specified role.
     *
     * @param role user role
     */
    public User(String role) {
        this.role = role;
    }

    /**
     * Constructs a user with role and email.
     *
     * @param role user role
     * @param email user email
     */
    public User(String role, String email) {
        this.role = role;
        this.email = email;
    }

    /**
     * Constructs a user with role, email and password.
     *
     * @param role user role
     * @param email user email
     * @param password user password
     */
    public User(String role, String email, String password) {
        this.role = role;
        this.email = email;
        this.password = password;
    }

    /**
     * Returns the user's role.
     *
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the user's role.
     *
     * @param role role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns the user's email.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email.
     *
     * @param email email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user's password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}