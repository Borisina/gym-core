package com.kolya.gym.repo;

import com.kolya.gym.domain.TrainingType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingTypeRepo extends CrudRepository<TrainingType,Integer> {
    List<TrainingType> findAll();
}
