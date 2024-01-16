package com.kolya.gym.config;

import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;

import java.io.Serializable;

public class EnumAsEntityInterceptor extends EmptyInterceptor {

    @Override
    public Object instantiate(String entityName, EntityMode entityMode, Serializable id) {
        try {
            Object o;
            Class cls = Class.forName(entityName);
            if (cls.isEnum() && id instanceof Number) {
                o = cls.getEnumConstants()[((Number)id).intValue()-1];
            } else {
                o = super.getEntity(entityName, id);
            }
            return o;
        } catch (ClassNotFoundException e) {
            // It would be very unexpected if the JPA layer gave us an entity name that does not map to a class
            throw new RuntimeException(e);
        }
    }

}
