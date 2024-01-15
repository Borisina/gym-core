package com.kolya.gym.validation;

public class CommonValidation {
    static public void validateId(long id) throws IllegalArgumentException{
        if (id<=0){
            throw new IllegalArgumentException("Wrong parameter 'id': cant be <=0");
        }
    }


}
