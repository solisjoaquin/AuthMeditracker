package com.example.authbeta;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests to validate password.
 */
public class PasswordValidatorTest {

    // Declare class variable(s).
    private Boolean result = false;

    @Test
    public void validatePasswordSuccess() {
        PasswordValidatorTest pass = new PasswordValidatorTest();
        assertTrue(pass.isValidPassword("tEst17"));
    }

    @Test
    public void validatePasswordEmpty() {
        PasswordValidatorTest pass = new PasswordValidatorTest();
        assertFalse(pass.isValidPassword(""));
    }

    public boolean isValidPassword(String password) {

        // Conditional statement that evaluates password to null and empty.
        if (password != null && !password.equals("")) {
            result = true;
        }

        // Return result.
        return result;
    }
}
