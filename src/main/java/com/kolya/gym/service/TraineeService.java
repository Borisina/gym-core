package com.kolya.gym.service;

import com.kolya.gym.dao.TraineeDao;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.data.TraineeData;
import com.kolya.gym.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeService {

    private final TraineeDao traineeDao;

    @Autowired
    public TraineeService(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public Trainee create(TraineeData traineeData, User user){
        Trainee trainee = getTraineeFromData(traineeData);
        trainee.setUser(user);
        return traineeDao.create(trainee);
    }

    public Trainee update(TraineeData traineeData, User user, long traineeId) throws IllegalArgumentException{
        Trainee trainee = get(traineeId);
        trainee.setUser(user);
        trainee.setAddress(traineeData.getAddress());
        trainee.setDateOfBirt(traineeData.getDateOfBirt());
        traineeDao.update(trainee);
        return trainee;
    }

    public Trainee get(long id) throws IllegalArgumentException{
        Trainee trainee =  traineeDao.get(id);
        if (trainee==null){
            throw new IllegalArgumentException("There is no trainee with id = "+id);
        }
        return trainee;
    }

    public Trainee delete(long id) throws IllegalArgumentException{
        Trainee trainee = traineeDao.delete(id);
        if (trainee==null){
            throw new IllegalArgumentException("There is no trainee with id = "+id);
        }
        return trainee;
    }

    public List<Trainee> getAll() {
        return traineeDao.getAll();
    }

    private Trainee getTraineeFromData(TraineeData data){
        Trainee trainee = new Trainee();
        trainee.setAddress(data.getAddress());
        trainee.setDateOfBirt(data.getDateOfBirt());
        return trainee;
    }

}
