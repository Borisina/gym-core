package com.kolya.gym.service;

import com.kolya.gym.dao.UserDao;
import com.kolya.gym.domain.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User create(String firstName, String lastName) {
        User user = generateUser(firstName,lastName);
        return userDao.create(user);
    }

    public User delete(long id) {
        return userDao.delete(id);
    }

    private User generateUser(String firstName, String lastName){
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(generateUsername(firstName,lastName));
        user.setPassword(generatePassword());
        user.setActive(true);
        return user;
    }

    private String generateUsername(String firstName, String lastName){
        String username = firstName+"."+lastName;
        String usernameForRegex = firstName+"\\."+lastName;
        int count = 0;
        List<User> userList = userDao.getAll();
        for (User userFromList:userList){
            if (userFromList.getUsername().matches(usernameForRegex+"[0-9]*")){
                count++;
            }
        }
        if (count!=0){
            username=username+count;
        }
        return username;
    }


    private String generatePassword(){
        return RandomStringUtils.random(10, true, true);
    }

    public User update(String firstName, String lastName, long id) {
        User user = userDao.get(id);
        if (user==null){
            throw new IllegalArgumentException("There is no user with id = "+id);
        }
        if (!firstName.isBlank()){
            user.setFirstName(firstName);
        }
        if (!lastName.isBlank()){
            user.setLastName(lastName);
        }
        user.setUsername(generateUsername(user.getFirstName(),user.getLastName()));
        userDao.update(user);
        return user;
    }
}
