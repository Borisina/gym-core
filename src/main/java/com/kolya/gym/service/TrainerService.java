package com.kolya.gym.service;

import com.kolya.gym.data.TrainerData;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.User;
import com.kolya.gym.repo.TrainerRepo;
import com.kolya.gym.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainerService {

    private final TrainerRepo trainerRepo;
    private final UserService userService;
    private final UserRepo userRepo;

    @Autowired
    public TrainerService(TrainerRepo trainerRepo, UserService userService, UserRepo userRepo) {
        this.trainerRepo = trainerRepo;
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @Transactional
    public Trainer create(TrainerData trainerData){
        Trainer trainer = getTrainerFromDataForCreate(trainerData);
        return trainerRepo.save(trainer);
    }

    @Transactional
    public Trainer update(TrainerData trainerData, long id) throws IllegalArgumentException{
        Trainer updatedTrainer = getTrainerFromDataForUpdate(trainerData);
        Trainer trainer = trainerRepo.findById(id).orElseThrow(()->new IllegalArgumentException("There is no trainer with id = "+id));
        if(updatedTrainer.getSpecialization()!=null){
            trainer.setSpecialization(updatedTrainer.getSpecialization());
        }
        userService.change(trainer.getUser(),updatedTrainer.getUser());
        trainer = trainerRepo.save(trainer);
        return trainer;
    }

    public Trainer get(long id) throws IllegalArgumentException{
        return trainerRepo.findById(id).orElseThrow(()->new IllegalArgumentException("There is no trainer with id = "+id));
    }

    @Transactional
    public Trainer getByUsername(String username) throws IllegalArgumentException {
        User user = userRepo.findByUsername(username).orElseThrow(()-> new IllegalArgumentException("There is no user with username = "+username));
        return trainerRepo.findByUserId(user.getId()).orElseThrow(()->new IllegalArgumentException("There is no user with id = "+user.getId()));
    }

    public List<Trainer> getAll() {
        return trainerRepo.findAll();
    }

    public Trainer getTrainerFromData(TrainerData trainerData){
        Trainer trainer = new Trainer();
        trainer.setSpecialization(trainerData.getSpecialization());
        return trainer;
    }

    private Trainer getTrainerFromDataForCreate(TrainerData trainerData){
        Trainer trainer = getTrainerFromData(trainerData);
        User user = userService.generateUser(trainerData);
        trainer.setUser(user);
        return trainer;
    }

    private Trainer getTrainerFromDataForUpdate(TrainerData trainerData){
        Trainer trainer = getTrainerFromData(trainerData);
        User user = userService.generateUserForUpdate(trainerData);
        trainer.setUser(user);
        return trainer;
    }
}
