package com.kolya.gym;


import com.kolya.gym.config.Config;
import com.kolya.gym.data.*;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.TrainingType;
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

        TraineeData traineeData2 = new TraineeData();
        traineeData2.setFirstName("Nikolay5");
        traineeData2.setLastName("Alexeev");
        traineeFacade.createTrainee(traineeData2);

        TraineeData traineeData3 = new TraineeData();
        traineeData3.setFirstName("Nikolay");
        traineeData3.setLastName("Alexeev");
        Trainee trainee= traineeFacade.createTrainee(traineeData3);
        AuthData authData = new AuthData(trainee.getUser().getUsername(),trainee.getUser().getPassword());

        TraineeData traineeData4 = new TraineeData();
        traineeData4.setFirstName("Vasiliy");
        traineeData4.setLastName("Palkin");
        traineeFacade.createTrainee(traineeData4);

        traineeFacade.changePassword(authData,"password");
        trainee = traineeFacade.getTrainee(1);
        System.out.println(trainee.getUser().getPassword());
        authData.setPassword("password");
        traineeFacade.createTrainee(traineeData3);
        System.out.println(traineeFacade.getAllTrainees());


        TraineeDataUpdate traineeDataUpdate = new TraineeDataUpdate();
        traineeDataUpdate.setFirstName("");
        traineeDataUpdate.setLastName("Ivanov");
        traineeFacade.updateTrainee(authData,traineeDataUpdate);
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
        trainingData.setTrainingType(TrainingType.TYPE_1);
        trainingData.setTrainingName("Training Name 1");
        trainingData.setTrainingDate(new Date());
        trainingFacade.createTraining(trainingData);
        System.out.println(trainingFacade.getAllTrainings());



        TrainingData trainingDataWrong = new TrainingData();
        trainingDataWrong.setTraineeId(1);
        trainingDataWrong.setTrainerId(5);
        trainingDataWrong.setDuration(24);
        trainingDataWrong.setTrainingType(TrainingType.TYPE_1);
        trainingDataWrong.setTrainingName("Training Name 1");
        trainingDataWrong.setTrainingDate(new Date());
        trainingFacade.createTraining(trainingDataWrong);
        System.out.println(trainingFacade.getAllTrainings());

        System.out.println(trainingFacade.getAllTrainings());
        traineeFacade.deleteByUsername(authData,"Nikolay.Alexeev");
        System.out.println(trainingFacade.getAllTrainings());

        System.out.println(traineeFacade.getNotAssignedTrainees());

        traineeFacade.changeActiveStatus(2);

        System.out.println(traineeFacade.getNotAssignedTrainees());


        TrainingData trainingData2 = new TrainingData();
        trainingData2.setTraineeId(3);
        trainingData2.setTrainerId(1);
        trainingData2.setDuration(12);
        trainingData2.setTrainingType(TrainingType.TYPE_2);
        trainingData2.setTrainingName("Training Name 2");
        trainingData2.setTrainingDate(new Date());
        trainingFacade.createTraining(trainingData2);

        TrainingData trainingData3 = new TrainingData();
        trainingData2.setTraineeId(3);
        trainingData2.setTrainerId(1);
        trainingData2.setDuration(24);
        trainingData2.setTrainingType(TrainingType.TYPE_3);
        trainingData2.setTrainingName("Training Name 3");
        trainingData2.setTrainingDate(new Date());
        trainingFacade.createTraining(trainingData2);

        TrainingData trainingData4 = new TrainingData();
        trainingData2.setTraineeId(3);
        trainingData2.setTrainerId(1);
        trainingData2.setDuration(36);
        trainingData2.setTrainingType(TrainingType.TYPE_3);
        trainingData2.setTrainingName("Training Name 3");
        trainingData2.setTrainingDate(new Date());
        trainingFacade.createTraining(trainingData2);
        System.out.println(trainingFacade.getAllTrainings());

        TrainingCriteria trainingCriteria = new TrainingCriteria();
        trainingCriteria.setTrainingName("3");
        trainingCriteria.setDurationMax(25);
        trainingCriteria.setTrainingType(TrainingType.TYPE_3);

        System.out.println(trainingFacade.getByTraineeUsernameAndCriteria("Nikolay.Alexeev1",trainingCriteria));
    }

}
