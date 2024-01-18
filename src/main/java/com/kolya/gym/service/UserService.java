package com.kolya.gym.service;

import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.ChangePasswordData;
import com.kolya.gym.data.UserData;
import com.kolya.gym.domain.User;
import com.kolya.gym.exception.ExcessiveAttemptsException;
import com.kolya.gym.repo.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LoginAttemptService loginAttemptService;

    private User generateUser(UserData userData){
        User user = new User();
        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        return user;
    }

    public User generateUserForCreate(UUID transactionId, UserData userData){
        logger.info("Transaction ID: {}, Generating user for create from UserData: {}", transactionId, userData);
        User user = generateUser(userData);
        user.setUsername(generateUsername(transactionId, userData.getFirstName(),userData.getLastName()));
        user.setPassword(generatePassword());
        user.setActive(true);
        logger.info("Transaction ID: {}, User was returned {}", transactionId, user);
        return user;
    }

    public User generateUserForUpdate(UUID transactionId, UserData userData){
        logger.info("Transaction ID: {}, Generating user for update from UserData: {}", transactionId, userData);
        User user = generateUser(userData);
        user.setActive(userData.isActive());
        logger.info("Transaction ID: {}, User was returned {}", transactionId, user);
        return user;
    }

    public String generateUsername(UUID transactionId, String firstName, String lastName){
        logger.info("Transaction ID: {}, Generating username (firstName = {}, lastName = {})", transactionId, firstName, lastName);
        String username = firstName+"."+ lastName;
        long count = userRepo.countDuplicates(firstName,lastName);
        if (count!=0){
            username=username+count;
        }
        logger.info("Transaction ID: {}, Username generated: {}", transactionId, username);
        return username;
    }


    private String generatePassword() {
        return RandomStringUtils.random(10, true, true);
    }

    public void change(User user, User updatedUser){
            user.setLastName(updatedUser.getLastName());
            user.setFirstName(updatedUser.getFirstName());
            user.setActive(updatedUser.isActive());
    }

    public void changePassword(UUID transactionId, ChangePasswordData changePasswordData) throws IllegalArgumentException, UsernameNotFoundException{
        logger.info("Transaction ID: {}, Changing password for user {}", transactionId, changePasswordData.getUsername());
        User user = (User) loadUserByUsername(changePasswordData.getUsername());
        if (!passwordEncoder.matches(changePasswordData.getOldPassword(),user.getPassword())){
            throw new IllegalArgumentException("Wrong password.");
        }
        user.setPassword(encodePassword(changePasswordData.getNewPassword()));
        logger.info("Transaction ID: {}, Password was changed for user {}", transactionId, changePasswordData.getUsername());
        userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("Where is no user with username = "+username));
    }

    public UserDetails authorize(AuthData authData) throws IllegalArgumentException, ExcessiveAttemptsException{
        UserDetails userDetails = loadUserByUsername(authData.getUsername());
        loginAttemptService.isUserBLocked(userDetails.getUsername());
        if (!passwordEncoder.matches(authData.getPassword(),userDetails.getPassword())){
            loginAttemptService.loginFailed(userDetails.getUsername());
            throw new IllegalArgumentException("Wrong password.");
        }
        return userDetails;
    }

    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

    public void validateChangePasswordData(ChangePasswordData data) throws IllegalArgumentException{
        validateUsername(data.getUsername());
        validatePassword(data.getNewPassword());
        validatePassword(data.getOldPassword());
    }

    public void validateUsername(String username) throws IllegalArgumentException{
        if (StringUtils.isBlank(username)){
            throw new IllegalArgumentException("Wrong parameter 'username': cant be empty");
        }
    }

    public void validatePassword(String password) throws IllegalArgumentException{
        if (StringUtils.isBlank(password)){
            throw new IllegalArgumentException("Wrong parameter 'password': cant be empty");
        }
    }
}
