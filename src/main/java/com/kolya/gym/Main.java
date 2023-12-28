package com.kolya.gym;


import com.kolya.gym.config.Config;
import com.kolya.gym.data.TraineeData;
import com.kolya.gym.data.TrainerData;
import com.kolya.gym.data.TrainingData;
import com.kolya.gym.facade.TraineeFacade;
import com.kolya.gym.facade.TrainerFacade;
import com.kolya.gym.facade.TrainingFacade;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;


public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Logger logger = context.getBean(Logger.class);
        TraineeFacade traineeFacade = context.getBean(TraineeFacade.class);
        TrainerFacade trainerFacade = context.getBean(TrainerFacade.class);
        TrainingFacade trainingFacade = context.getBean(TrainingFacade.class);

        logger.info("Application context is ready!");

        TraineeData traineeData = new TraineeData();
        traineeData.setFirstName("Nikolay");
        traineeData.setLastName("");
        traineeFacade.createTrainee(traineeData);
        System.out.println(traineeFacade.getAllTrainees());

        TraineeData traineeData2 = new TraineeData();
        traineeData2.setFirstName("Nikolay5");
        traineeData2.setLastName("Alexeev");
        traineeFacade.createTrainee(traineeData2);
        System.out.println(traineeFacade.getAllTrainees());

        TraineeData traineeData3 = new TraineeData();
        traineeData3.setFirstName("Nikolay");
        traineeData3.setLastName("Alexeev");
        traineeFacade.createTrainee(traineeData3);
        traineeFacade.createTrainee(traineeData3);
        System.out.println(traineeFacade.getAllTrainees());


        TraineeData traineeDataUpdate = new TraineeData();
        traineeDataUpdate.setFirstName("");
        traineeDataUpdate.setLastName("Ivanov");
        traineeFacade.updateTrainee(traineeDataUpdate,2);
        System.out.println(traineeFacade.getAllTrainees());

        TrainerData trainerDataWrong = new TrainerData();
        trainerDataWrong.setFirstName("Genadiy");
        trainerDataWrong.setLastName("Tokov");
        trainerDataWrong.setSpecialization("   ");
        trainerFacade.createTrainer(trainerDataWrong);
        System.out.println(trainerFacade.getAllTrainers());

        TrainerData trainerData = new TrainerData();
        trainerData.setFirstName("Genadiy");
        trainerData.setLastName("Tokov");
        trainerData.setSpecialization("Super trainer");
        trainerFacade.createTrainer(trainerData);
        System.out.println(trainerFacade.getAllTrainers());

        TrainingData trainingData = new TrainingData();
        trainingData.setTraineeId(1);
        trainingData.setTrainerId(1);
        trainingData.setDuration(24);
        trainingData.setTrainingType("type_1");
        trainingData.setTrainingName("Training Name 1");
        trainingData.setTrainingDate(new Date());
        trainingFacade.createTraining(trainingData);
        System.out.println(trainingFacade.getAllTrainings());

        TrainingData trainingDataWrong = new TrainingData();
        trainingDataWrong.setTraineeId(2);
        trainingDataWrong.setTrainerId(5);
        trainingDataWrong.setDuration(24);
        trainingDataWrong.setTrainingType("type_1");
        trainingDataWrong.setTrainingName("Training Name 1");
        trainingDataWrong.setTrainingDate(new Date());
        trainingFacade.createTraining(trainingDataWrong);
        System.out.println(trainingFacade.getAllTrainings());
    }

}
