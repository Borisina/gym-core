package com.kolya.gym.domain;

import java.util.HashMap;
import java.util.Map;

public class TrainerWorkload {
    private Map<Integer, Map<String, Integer>> workLoad = new HashMap<>();

    public Map<Integer, Map<String, Integer>> getWorkload() {
        return workLoad;
    }

    public void setWorkload(Map<Integer, Map<String, Integer>> list) {
        this.workLoad = list;
    }

    @Override
    public String toString() {
        return "TrainerWorkload{" +
                "workLoad=" + workLoad +
                '}';
    }
}
