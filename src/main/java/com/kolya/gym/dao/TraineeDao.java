package com.kolya.gym.dao;

import com.kolya.gym.domain.Trainee;
import com.kolya.gym.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TraineeDao implements DaoIface<Trainee>{

    private final InMemoryStorage storage;

    @Autowired
    public TraineeDao(InMemoryStorage storage) {
        this.storage = storage;
    }

    @Override
    public Trainee create(Trainee trainee) {
        Map<Long, Trainee> traineeList = storage.getTrainees();
        trainee.setId(storage.getNewTraineeId());
        traineeList.put(trainee.getId(),trainee);
        return trainee;
    }

    @Override
    public Trainee delete(long id) {
        Map<Long, Trainee> traineeList = storage.getTrainees();
        Trainee trainee = traineeList.get(id);
        traineeList.remove(id);
        return trainee;
    }

    @Override
    public boolean update(Trainee trainee) {
        Map<Long, Trainee> traineeList = storage.getTrainees();
        if (traineeList.get(trainee.getId())==null){
            return false;
        }
        traineeList.put(trainee.getId(), trainee);
        return true;
    }

    @Override
    public Trainee get(long id) {
        Map<Long, Trainee> traineeList = storage.getTrainees();
        return traineeList.get(id);
    }

    @Override
    public List<Trainee> getAll() {
        return new ArrayList<>(storage.getTrainees().values());
    }
}
