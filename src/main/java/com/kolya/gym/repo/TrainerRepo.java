package com.kolya.gym.repo;

import com.kolya.gym.domain.Trainer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepo extends CrudRepository<Trainer,Long> {
    Trainer deleteById(long id);
    List<Trainer> findAll();

    Optional<Trainer> findByUserId(long id);
}
