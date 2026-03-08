package service;

import java.security.SecureRandom;

/**
 * Password utility service.
 */
public class PasswordService {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a 6-digit numeric PIN as a string.
     *
     * @return 6-digit PIN
     */
    public static String generate6DigitPin() {
        int pin = 100000 + random.nextInt(900000);
        return String.valueOf(pin);
    }
}