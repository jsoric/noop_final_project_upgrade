package entities;

import enums.Roles;
import enums.VerificationStatus;

/**
 * Represents an employee user in the system.
 *
 * Employee extends {@link User} and adds personal and verification data.
 */
public class Employee extends User {

    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private VerificationStatus verificationStatus;

    /**
     * Creates an employee with default role and pending verification status.
     */
    public Employee() {
        super(Roles.employee.toString());
        this.verificationStatus = VerificationStatus.pending;
    }

    /**
     * Returns the employee's database ID.
     *
     * @return employee ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the employee's database ID.
     *
     * @param id employee ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the employee's first name.
     *
     * @return first name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the employee's first name.
     *
     * @param name first name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the employee's surname.
     *
     * @return surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the employee's surname.
     *
     * @param surname surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Returns the employee's phone number.
     *
     * @return phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the employee's phone number.
     *
     * @param phoneNumber phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the verification status.
     *
     * @return verification status
     */
    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    /**
     * Sets the verification status.
     *
     * @param verificationStatus verification status
     */
    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    /**
     * Returns true if the employee is verified.
     *
     * @return true if verified, false otherwise
     */
    public boolean isVerified() {
        return verificationStatus == VerificationStatus.verified;
    }
}