package com.kolya.gym.validation;

public class CommonValidation {
    static public void nullValidation(Object object) throws IllegalArgumentException{
        if (object==null){
            throw new IllegalArgumentException("Input Data is null");
        }

    }
}
