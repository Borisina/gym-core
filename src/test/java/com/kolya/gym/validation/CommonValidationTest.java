package com.kolya.gym.validation;

import org.junit.Test;

import static org.junit.Assert.fail;

public class CommonValidationTest {

    @Test(expected = IllegalArgumentException.class)
    public void nullValidation_NullInput_Test() {
        CommonValidation.nullValidation(null);
    }

    @Test
    public void nullValidation_NotNullInput_Test() {
        try {
            CommonValidation.nullValidation(new Object());
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException was thrown for not null input");
        }
    }
}