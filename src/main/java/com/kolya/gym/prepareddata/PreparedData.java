package com.kolya.gym.prepareddata;

import com.kolya.gym.builder.TraineeDataBuilder;
import com.kolya.gym.builder.TrainerDataBuilder;
import com.kolya.gym.builder.TrainingDataBuilder;
import com.kolya.gym.data.TraineeData;
import com.kolya.gym.data.TrainerData;
import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.TrainingType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreparedData {
    static public List<TraineeData> traineeDataList;
    static public List<TraineeData> wrongTraineeDataList;
    static public List<TrainerData> trainerDataList;
    static public List<TrainerData> wrongTrainerDataList;
    static public List<TrainingData> trainingDataList;
    static public List<TrainingData> wrongTrainingDataList;

    static {
        traineeDataList = new ArrayList<>(){{
            add(new TraineeDataBuilder().setFirstName("Nikolay").setLastName("Alexeev").build());
            add(new TraineeDataBuilder().setFirstName("Vasiliy").setLastName("Palkin").build());
        }};

        wrongTraineeDataList = new ArrayList<>(){{
            add(new TraineeDataBuilder().setFirstName("Nikolay").setLastName("").build());
            add(new TraineeDataBuilder().setFirstName("Nikolay5").setLastName("Alexeev").build());
        }};

        trainerDataList = new ArrayList<>(){{
            add(new TrainerDataBuilder().setFirstName("Genadiy").setLastName("Tokov").setSpecialization("Super trainer").build());
        }};

        wrongTrainerDataList = new ArrayList<>(){{
            add(new TrainerDataBuilder().setFirstName("Genadiy").setLastName("Tokov").setSpecialization("   ").build());
        }};

        trainingDataList = new ArrayList<>(){{
            add(new TrainingDataBuilder()
                    .setTraineeId(1)
                    .setTrainerId(1)
                    .setDuration(24)
                    .setTrainingName("Training Name 0")
                    .setTrainingType(TrainingType.TYPE_1)
                    .setTrainingDate(new Date())
                    .build());
            add(new TrainingDataBuilder()
                    .setTraineeId(3)
                    .setTrainerId(1)
                    .setDuration(12)
                    .setTrainingType(TrainingType.TYPE_2)
                    .setTrainingName("Training Name 2")
                    .setTrainingDate(new Date())
                    .build());
            add(new TrainingDataBuilder()
                    .setTraineeId(3)
                    .setTrainerId(1)
                    .setDuration(24)
                    .setTrainingType(TrainingType.TYPE_3)
                    .setTrainingName("Training Name 3")
                    .setTrainingDate(new Date())
                    .build());
            add(new TrainingDataBuilder()
                    .setTraineeId(3)
                    .setTrainerId(1)
                    .setDuration(36)
                    .setTrainingType(TrainingType.TYPE_3)
                    .setTrainingName("Training Name 3")
                    .setTrainingDate(new Date())
                    .build());
        }};

        wrongTrainingDataList = new ArrayList<>(){{
            add(new TrainingDataBuilder()
                    .setTraineeId(1)
                    .setTrainerId(5)
                    .setDuration(24)
                    .setTrainingType(TrainingType.TYPE_1)
                    .setTrainingName("Training Name 1")
                    .setTrainingDate(new Date())
                    .build());
        }};
    }
}
