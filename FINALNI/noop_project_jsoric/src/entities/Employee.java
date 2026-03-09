package entities;

import enums.Roles;
import enums.VerificationStatus;

/**
 * Represents an employee in the system.
 * <p>
 * Extends {@link User} with personal data, verification status,
 * and the assigned team leader ID.
 */
public class Employee extends User {

    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private VerificationStatus verificationStatus;
    private Long teamLeaderId;

    /**
     * Creates an employee with the default employee role
     * and pending verification status.
     */
    public Employee() {
        super(Roles.employee.toString());
        this.verificationStatus = VerificationStatus.pending;
    }

    /**
     * Returns the employee ID.
     *
     * @return the employee ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the employee ID.
     *
     * @param id the employee ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the employee's first name.
     *
     * @return the first name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the employee's first name.
     *
     * @param name the first name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the employee's surname.
     *
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the employee's surname.
     *
     * @param surname the surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Returns the employee's phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the employee's phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the employee's verification status.
     *
     * @return the verification status
     */
    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    /**
     * Sets the employee's verification status.
     *
     * @param verificationStatus the verification status
     */
    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    /**
     * Checks whether the employee is verified.
     *
     * @return {@code true} if the employee is verified, otherwise {@code false}
     */
    public boolean isVerified() {
        return verificationStatus == VerificationStatus.verified;
    }

    /**
     * Returns the assigned team leader ID.
     *
     * @return the team leader ID
     */
    public Long getTeamLeaderId() {
        return teamLeaderId;
    }

    /**
     * Sets the assigned team leader ID.
     *
     * @param teamLeaderId the team leader ID
     */
    public void setTeamLeaderId(Long teamLeaderId) {
        this.teamLeaderId = teamLeaderId;
    }
}