package com.kolya.gym.repo;

import com.kolya.gym.domain.Trainee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeRepo extends CrudRepository<Trainee, Long> {
    List<Trainee> findAll();

    Optional<Trainee> findByUserUsername(String username);

    void deleteByUserUsername(String username);
}
