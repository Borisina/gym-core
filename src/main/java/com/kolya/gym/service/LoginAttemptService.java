package com.kolya.gym.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kolya.gym.exception.ExcessiveAttemptsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    @Value("${brute-force-protector.max-attempt}")
    private int MAX_ATTEMPT;

    @Value("${brute-force-protector.block-time-minute}")
    private int BLOCK_TIME;
    private LoadingCache<String, Integer> attemptsCache;


    @PostConstruct
    public void postConstruct(){
        attemptsCache = CacheBuilder.newBuilder().
                expireAfterWrite(BLOCK_TIME, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }

    public void loginFailed(String key) {
        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }

    public void isUserBLocked(String username) throws ExcessiveAttemptsException{
        if (isBlocked(username)){
            throw new ExcessiveAttemptsException("You are blocked for " + BLOCK_TIME+" minutes due to many failed login attempts");
        }
    }
}
