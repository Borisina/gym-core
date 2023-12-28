package com.kolya.gym.service;

import com.kolya.gym.dao.TrainerDao;
import com.kolya.gym.data.TrainerData;
import com.kolya.gym.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {

    private final TrainerDao trainerDao;

    @Autowired
    public TrainerService(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Trainer create(TrainerData trainerData, User user){
        Trainer trainer = getTrainerFromData(trainerData);
        trainer.setUser(user);
        return trainerDao.create(trainer);
    }

    public Trainer update(TrainerData trainerData, User user, long trainerId) throws IllegalArgumentException{
        Trainer trainer = get(trainerId);
        trainer.setUser(user);
        if (trainerData.getSpecialization()!=null && !trainerData.getSpecialization().isBlank()){
            trainer.setSpecialization(trainerData.getSpecialization());
        }
        trainerDao.update(trainer);
        return trainer;
    }

    public Trainer get(long id) throws IllegalArgumentException{
        Trainer trainer = trainerDao.get(id);
        if (trainer==null){
            throw new IllegalArgumentException("There is no trainer with id = "+id);
        }
        return trainer;
    }

    public Trainer getTrainerFromData(TrainerData data){
        Trainer trainer = new Trainer();
        trainer.setSpecialization(data.getSpecialization());
        return trainer;
    }

    public Trainer delete(long id) throws IllegalArgumentException{
        Trainer trainer = trainerDao.delete(id);
        if (trainer==null){
            throw new IllegalArgumentException("There is no trainer with id = "+id);
        }
        return trainer;
    }

    public List<Trainer> getAll() {
        return trainerDao.getAll();
    }
}
