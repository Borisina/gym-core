package com.kolya.gym.dao;

import com.kolya.gym.domain.User;
import com.kolya.gym.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UserDao implements DaoIface<User>{

    private final InMemoryStorage storage;

    @Autowired
    public UserDao(InMemoryStorage storage) {
        this.storage = storage;
    }
    @Override
    public User create(User user) {
        Map<Long, User> userList = storage.getUsers();
        user.setId(storage.getNewUserId());
        userList.put(user.getId(),user);
        return user;
    }

    @Override
    public User delete(long id) {
        Map<Long, User> userList =  storage.getUsers();
        User user = userList.get(id);
        userList.remove(id);
        return user;
    }

    @Override
    public boolean update(User user) {
        Map<Long, User> userList = storage.getUsers();
        if (userList.get(user.getId())==null){
            return false;
        }
        userList.put(user.getId(), user);
        return true;
    }

    @Override
    public User get(long id) {
        Map<Long, User> userList = storage.getUsers();
        return (User) userList.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(storage.getUsers().values());
    }

}
