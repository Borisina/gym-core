package com.kolya.gym.service;

import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LoginAttemptServiceTest {
    public static final String TEST_KEY = "key1";

    @InjectMocks
    private LoginAttemptService service;

    int MAX_ATTEMPT = 3;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void whenLoginFailed_thenAttemptsCacheCountIncreases() {
        service.loginSucceeded(TEST_KEY);
        service.loginFailed(TEST_KEY);
        assertTrue(service.isBlocked(TEST_KEY));
    }

    @Test
    public void whenLoginFailedAfterSuccess_thenIsStillBlocked() {
        service.loginSucceeded(TEST_KEY);
        service.loginFailed(TEST_KEY);
        service.loginSucceeded(TEST_KEY);

        assertTrue(service.isBlocked(TEST_KEY));
    }

    @Test
    public void whenFailedMoreThanMaxAttempt_thenIsBlocked() {
        for (int i = 0; i < MAX_ATTEMPT; i++) {
            service.loginFailed(TEST_KEY);
        }

        assertTrue(service.isBlocked(TEST_KEY));
    }
}