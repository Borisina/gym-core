package com.kolya.gym.domain;

public enum TrainingType {
    TYPE_1,TYPE_2,TYPE_3;

    private byte id = (byte)(ordinal()+1);

    private String value = name();

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}


