package com.kolya.gym.repo;

import com.kolya.gym.data.TrainingCriteria;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.Training;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrainingRepo extends CrudRepository<Training,Long> {
    List<Training> findAll();

    @Query(nativeQuery = true, value =
            "SELECT * FROM training " +
                    "WHERE trainee_id = :trainee_id " +
                    "AND (training_type = COALESCE(:training_type, training_type)) " +
                    "AND (training_name LIKE CONCAT('%',COALESCE(:training_name,training_name),'%')) " +
                    "AND (training_date >= COALESCE(:training_date_from, training_date)) " +
                    "AND (training_date <= COALESCE(:training_date_to,training_date)) " +
                    "AND (duration >= COALESCE(:duration_min, duration)) " +
                    "AND (duration <= COALESCE(:duration_max,duration)) "
    )
    List<Training> findByTraineeIdAndCriteria(
            @Param("trainee_id") long traineeId,
            @Param("training_type") String trainingType,
            @Param("training_name") String trainingName,
            @Param("training_date_from") Date trainingDateFrom,
            @Param("training_date_to") Date trainingDateTo,
            @Param("duration_min") Integer durationMin,
            @Param("duration_max") Integer durationMax
    );

    @Query(nativeQuery = true, value =
            "SELECT * FROM training " +
                    "WHERE trainer_id = :trainer_id " +
                    "AND (training_type = COALESCE(:training_type, training_type)) " +
                    "AND (training_name LIKE CONCAT('%',COALESCE(:training_name,training_name),'%')) " +
                    "AND (training_date >= COALESCE(:training_date_from, training_date)) " +
                    "AND (training_date <= COALESCE(:training_date_to,training_date)) " +
                    "AND (duration >= COALESCE(:duration_min, duration)) " +
                    "AND (duration <= COALESCE(:duration_max,duration)) "
    )
    List<Training> findByTrainerIdAndCriteria(
            @Param("trainer_id") long trainerId,
            @Param("training_type") String trainingType,
            @Param("training_name") String trainingName,
            @Param("training_date_from") Date trainingDateFrom,
            @Param("training_date_to") Date trainingDateTo,
            @Param("duration_min") Integer durationMin,
            @Param("duration_max") Integer durationMax
    );

    List<Training> findByTrainee(Trainee trainee);

    List<Training> findByTrainer(Trainer trainer);
}

