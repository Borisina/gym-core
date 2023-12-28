package com.kolya.gym.dao;

import java.util.List;

public interface DaoIface<T> {
    T create(T object);
    T delete(long id);
    boolean update(T object);
    T get(long id);

    List<T> getAll();
}
