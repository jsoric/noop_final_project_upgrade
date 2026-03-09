package entities;

import enums.Roles;

/**
 * Represents a team leader in the system.
 * <p>
 * A team leader can supervise multiple employees.
 */
public class TeamLeader extends User {

    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;

    /**
     * Creates a team leader with the default team leader role.
     */
    public TeamLeader() {
        super(Roles.teamleader.toString());
    }

    /**
     * Creates a team leader with email and password.
     *
     * @param email the team leader email
     * @param password the team leader password
     */
    public TeamLeader(String email, String password) {
        super(Roles.teamleader.toString(), email, password);
    }

    /**
     * Returns the team leader ID.
     *
     * @return the team leader ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the team leader ID.
     *
     * @param id the team leader ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the team leader's first name.
     *
     * @return the first name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the team leader's first name.
     *
     * @param name the first name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the team leader's surname.
     *
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the team leader's surname.
     *
     * @param surname the surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Returns the team leader's phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the team leader's phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return name + " " + surname + " (" + getEmail() + ")";
    }
}