package com.kolya.gym.dao;

import com.kolya.gym.domain.Trainer;
import com.kolya.gym.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TrainerDao implements DaoIface<Trainer> {

    private final InMemoryStorage storage;

    @Autowired
    public TrainerDao(InMemoryStorage storage) {
        this.storage = storage;
    }

    @Override
    public Trainer create(Trainer trainer) {
        Map<Long, Trainer> trainerList = storage.getTrainers();
        trainer.setId(storage.getNewTrainerId());
        trainerList.put(trainer.getId(),trainer);
        return trainer;
    }

    @Override
    public Trainer delete(long id) {
        Map<Long, Trainer> trainerList = storage.getTrainers();
        Trainer trainer = trainerList.get(id);
        trainerList.remove(id);
        return trainer;
    }

    @Override
    public boolean update(Trainer trainer) {
        Map<Long, Trainer> trainerList = storage.getTrainers();
        if (trainerList.get(trainer.getId())==null){
            return false;
        }
        trainerList.put(trainer.getId(), trainer);
        return true;
    }

    @Override
    public Trainer get(long id) {
        Map<Long, Trainer> trainerList = storage.getTrainers();
        return trainerList.get(id);
    }

    @Override
    public List<Trainer> getAll() {
        return new ArrayList<>(storage.getTrainers().values());
    }


}
