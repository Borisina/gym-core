package com.kolya.gym.domain;

import java.util.HashMap;
import java.util.Map;

public class TrainerWorkload {
    private Map<Integer, Map<Month, Integer>> workLoad = new HashMap<>();

    public Map<Integer, Map<Month, Integer>> getWorkload() {
        return workLoad;
    }

    public void setWorkload(Map<Integer, Map<Month, Integer>> workLoad) {
        this.workLoad = workLoad;
    }

    @Override
    public String toString() {
        return "TrainerWorkload{" +
                "workLoad=" + workLoad +
                '}';
    }
}
