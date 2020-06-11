package com.example.authbeta;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test to evaluate the validity of the entered email.
 */
public class EmailValidatorTest {

    // Declare class variable(s).
    private Boolean result = false;

    @Test
    public void isValidEmailAddress() {
        EmailValidatorTest test = new EmailValidatorTest();
        assertTrue(test.EmailTest("test@email.com"));
    }

    @Test
    public void isInvalidEmailAddress() {
        EmailValidatorTest test = new EmailValidatorTest();
        assertFalse(test.EmailTest("test-email-com"));
    }

    public boolean EmailTest(String email) {

        // Conditional statement evaluating whether String contains @ and (.).
        if (email.contains("@") && email.contains(".")) {
            result = true;
        }

        // Return result.
        return result;
    }
}
