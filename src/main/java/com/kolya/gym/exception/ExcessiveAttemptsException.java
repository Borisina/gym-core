package com.kolya.gym.exception;

import org.springframework.security.core.AuthenticationException;

public class ExcessiveAttemptsException extends AuthenticationException {
    public ExcessiveAttemptsException(String msg) {
        super(msg);
    }
}
