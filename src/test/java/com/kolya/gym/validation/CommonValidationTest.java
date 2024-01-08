package com.kolya.gym.validation;

import org.junit.Test;

import static org.junit.Assert.fail;

public class CommonValidationTest {

    @Test(expected = IllegalArgumentException.class)
    public void validateId_Test_Exception() {
        CommonValidation.validateId(-1);
    }

    @Test
    public void validateId_Test() {
        try {
            CommonValidation.validateId(1);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException was thrown for not null input");
        }
    }
}