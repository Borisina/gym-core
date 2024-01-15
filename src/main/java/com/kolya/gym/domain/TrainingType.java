package com.kolya.gym.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public enum TrainingType {
    TYPE_1,TYPE_2,TYPE_3;

    @Id
    public final byte id = (byte)(ordinal()+1);

    @Column(unique = true, nullable = false)
    public final String value = name();
}


