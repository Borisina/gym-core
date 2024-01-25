package com.kolya.gym.repo;

import com.kolya.gym.domain.Trainer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepo extends CrudRepository<Trainer,Long> {
    List<Trainer> findAll();
    Optional<Trainer> findByUserUsername(String username);

    @Query(nativeQuery = true, value = "SELECT ter.specialization, ter.id, u.first_name, u.last_name, ter.username FROM trainer ter " +
            "INNER JOIN usr u ON ter.username = u.username " +
            "LEFT JOIN trainees_trainers tt ON ter.id = tt.trainer_id " +
            "WHERE tt.trainee_id IS NULL " +
            "AND u.is_active=true ")
    List<Trainer> findNotAssignedOnTrainee(String username);
}
