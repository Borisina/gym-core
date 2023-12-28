package com.kolya.gym.dao;

import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.Training;
import com.kolya.gym.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TrainingDao implements DaoIface<Training>{
    private final InMemoryStorage storage;

    @Autowired
    public TrainingDao(InMemoryStorage storage) {
        this.storage = storage;
    }

    @Override
    public Training create(Training training) {
        Map<Long, Training> trainingList = storage.getTrainings();
        training.setId(storage.getNewTrainingId());
        trainingList.put(training.getId(),training);
        return training;
    }

    @Override
    public Training delete(long id) {
        Map<Long, Training> trainingList = storage.getTrainings();
        Training training = trainingList.get(id);
        trainingList.remove(id);
        return training;
    }

    @Override
    public boolean update(Training training) {
        Map<Long, Training> trainingList = storage.getTrainings();
        if (trainingList.get(training.getId())==null){
            return false;
        }
        trainingList.put(training.getId(), training);
        return true;
    }

    @Override
    public Training get(long id) {
        Map<Long, Training> trainingList = storage.getTrainings();
        return trainingList.get(id);
    }

    @Override
    public List<Training> getAll() {
        return new ArrayList<>(storage.getTrainings().values());
    }
}
