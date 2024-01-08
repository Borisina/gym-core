package com.kolya.gym.service;

import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.UserData;
import com.kolya.gym.domain.User;
import com.kolya.gym.repo.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User generateUser(UserData userData){
        String firstName = userData.getFirstName();
        String lastName = userData.getLastName();
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(generateUsername(firstName,lastName));
        user.setPassword(generatePassword());
        user.setActive(true);
        return user;
    }

    public User generateUserForUpdate(UserData userData){
        String firstName = userData.getFirstName();
        String lastName = userData.getLastName();
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

    public String generateUsername(String firstName, String lastName){
        String username = firstName+"."+ lastName;
        long count = userRepo.countDuplicates(firstName,lastName);
        if (count!=0){
            username=username+count;
        }
        return username;
    }


    private String generatePassword() {
        return RandomStringUtils.random(10, true, true);
    }

    public void change(User user, User updatedUser){
        boolean isUpdated = false;
        if (updatedUser!=null){
            if (updatedUser.getFirstName()!=null){
                user.setFirstName(updatedUser.getFirstName());
                isUpdated=true;
            }
            if (updatedUser.getLastName()!=null){
                user.setLastName(updatedUser.getLastName());
                isUpdated=true;
            }
            if (isUpdated){
                user.setUsername(generateUsername(user.getFirstName(),user.getLastName()));
            }
        }
    }

    public void changePassword(User user, String newPassword){
        user.setPassword(newPassword);
        userRepo.save(user);
    }

    public User authenticate(AuthData authData) throws AuthenticationException{
        User user = userRepo.findByUsernameAndPassword(authData.getUsername(),authData.getPassword());
        if (user==null){
            throw new AuthenticationException("Authentication Exception.");
        }
        return user;
    }

    public boolean changeActiveStatus(long id){
        User user = userRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("There is no user with id = "+id));
        boolean newStatus = !user.isActive();
        user.setActive(newStatus);
        userRepo.save(user);
        if (newStatus){
            System.out.println("User (id = "+id+") activated.");
        }else{
            System.out.println("User (id = "+id+") deactivated.");
        }
        return newStatus;
    }
}
