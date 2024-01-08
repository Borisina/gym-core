package com.kolya.gym.service;

import com.kolya.gym.data.TraineeDataUpdate;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.data.TraineeData;
import com.kolya.gym.domain.User;
import com.kolya.gym.repo.TraineeRepo;
import com.kolya.gym.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TraineeService {

    private final TraineeRepo traineeRepo;
    private final UserService userService;
    private final UserRepo userRepo;

    @Autowired
    public TraineeService(TraineeRepo traineeRepo, UserService userService, UserRepo userRepo) {
        this.traineeRepo = traineeRepo;
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @Transactional
    public Trainee create(TraineeData traineeData){
        Trainee trainee = getTraineeFromDataForCreate(traineeData);
        return traineeRepo.save(trainee);
    }

    @Transactional
    public Trainee update(TraineeDataUpdate traineeDataUpdate) throws IllegalArgumentException{
        Trainee updatedTrainee = getTraineeFromDataForUpdate(traineeDataUpdate);
        Trainee trainee = get(traineeDataUpdate.getId());
        if(updatedTrainee.getAddress()!=null){
            trainee.setAddress(updatedTrainee.getAddress());
        }
        if(updatedTrainee.getDateOfBirth()!=null) {
            trainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
        }
        userService.change(trainee.getUser(),updatedTrainee.getUser());
        //trainee = traineeRepo.save(trainee);
        return trainee;
    }

    @Transactional
    public List<Trainee> updateList(List<TraineeDataUpdate> traineeDataUpdateList) throws IllegalArgumentException{
        List<Trainee> result = new ArrayList<>();
        for (TraineeDataUpdate traineeDataUpdate:traineeDataUpdateList){
            result.add(update(traineeDataUpdate));
        }
        return result;
    }

    public Trainee get(long id) throws IllegalArgumentException{
        return traineeRepo.findById(id).orElseThrow(()->new IllegalArgumentException("There is no trainee with id = "+id));
    }

    public List<Trainee> getAll() {
        return traineeRepo.findAll();
    }

    public Trainee getTraineeFromData(TraineeData traineeData){
        Trainee trainee = new Trainee();
        trainee.setAddress(traineeData.getAddress());
        trainee.setDateOfBirth(traineeData.getDateOfBirth());
        return trainee;
    }

    private Trainee getTraineeFromDataForCreate(TraineeData traineeData){
        Trainee trainee = getTraineeFromData(traineeData);
        User user = userService.generateUser(traineeData);
        trainee.setUser(user);
        return trainee;
    }

    private Trainee getTraineeFromDataForUpdate(TraineeData traineeData){
        Trainee trainee = getTraineeFromData(traineeData);
        User user = userService.generateUserForUpdate(traineeData);
        trainee.setUser(user);
        return trainee;
    }

    @Transactional
    public Trainee getByUsername(String username) throws IllegalArgumentException {
        User user = userRepo.findByUsername(username).orElseThrow(()-> new IllegalArgumentException("There is no user with username = "+username));
        return traineeRepo.findByUserId(user.getId()).orElseThrow(()->new IllegalArgumentException("There is no user with id = "+user.getId()));
    }

    @Transactional
    public Trainee deleteByUsername(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(()-> new IllegalArgumentException("There is no user with username = "+username));
        return traineeRepo.deleteByUserId(user.getId());
    }

    public List<Trainee> getNotAssigned(){
        return traineeRepo.getNotAssigned();
    }
}
