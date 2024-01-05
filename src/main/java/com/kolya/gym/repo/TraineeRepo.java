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
    Trainee deleteById(long id);
    Optional<Trainee> findByUserId(long id);

    @Query(nativeQuery = true,value = "DELETE FROM trainee WHERE user_id=:id RETURNING *")
    Trainee deleteByUserId(@Param("id") long id);


    @Query(nativeQuery = true, value = "SELECT tee.date_of_birth, tee.id, tee.user_id, tee.address FROM trainee tee " +
            "INNER JOIN usr u ON tee.user_id = u.id " +
            "LEFT JOIN training ting ON tee.id = ting.trainee_id " +
            "WHERE ting.id IS NULL " +
            "AND u.is_active=true ")
    List<Trainee> getNotAssigned();
}
