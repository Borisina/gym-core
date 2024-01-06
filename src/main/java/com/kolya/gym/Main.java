package com.kolya.gym;


import com.kolya.gym.builder.TraineeDataBuilder;
import com.kolya.gym.builder.TrainerDataBuilder;
import com.kolya.gym.builder.TrainingDataBuilder;
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

        TraineeData traineeData = new TraineeDataBuilder().setFirstName("Nikolay").setLastName("").build();
        traineeFacade.createTrainee(traineeData);

        TraineeData traineeData2 = new TraineeDataBuilder().setFirstName("Nikolay5").setLastName("Alexeev").build();
        traineeFacade.createTrainee(traineeData2);

        TraineeData traineeData3 = new TraineeDataBuilder().setFirstName("Nikolay").setLastName("Alexeev").build();
        Trainee trainee= traineeFacade.createTrainee(traineeData3);
        AuthData authData = new AuthData(trainee.getUser().getUsername(),trainee.getUser().getPassword());

        TraineeData traineeData4 = new TraineeDataBuilder().setFirstName("Vasiliy").setLastName("Palkin").build();
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

        TrainerData trainerDataWrong = new TrainerDataBuilder().setFirstName("Genadiy").setLastName("Tokov").setSpecialization("   ").build();
        trainerFacade.createTrainer(trainerDataWrong);
        System.out.println(trainerFacade.getAllTrainers());

        TrainerData trainerData = new TrainerDataBuilder().setFirstName("Genadiy").setLastName("Tokov").setSpecialization("Super trainer").build();
        trainerFacade.createTrainer(trainerData);
        System.out.println(trainerFacade.getAllTrainers());

        TrainingData trainingData = new TrainingDataBuilder()
                .setTraineeId(1)
                .setTrainerId(1)
                .setDuration(24)
                .setTrainingType(TrainingType.TYPE_1)
                .setTrainingDate(new Date())
                .build();
        trainingFacade.createTraining(trainingData);
        System.out.println(trainingFacade.getAllTrainings());



        TrainingData trainingDataWrong = new TrainingDataBuilder()
                .setTraineeId(1)
                .setTrainerId(5)
                .setDuration(24)
                .setTrainingType(TrainingType.TYPE_1)
                .setTrainingName("Training Name 1")
                .setTrainingDate(new Date())
                .build();
        trainingFacade.createTraining(trainingDataWrong);
        System.out.println(trainingFacade.getAllTrainings());

        System.out.println(trainingFacade.getAllTrainings());
        traineeFacade.deleteByUsername(authData,"Nikolay.Alexeev");
        System.out.println(trainingFacade.getAllTrainings());

        System.out.println(traineeFacade.getNotAssignedTrainees());

        traineeFacade.changeActiveStatus(2);

        System.out.println(traineeFacade.getNotAssignedTrainees());


        TrainingData trainingData2 = new TrainingDataBuilder()
                .setTraineeId(3)
                .setTrainerId(1)
                .setDuration(12)
                .setTrainingType(TrainingType.TYPE_2)
                .setTrainingName("Training Name 2")
                .setTrainingDate(new Date())
                .build();
        trainingFacade.createTraining(trainingData2);

        TrainingData trainingData3 = new TrainingDataBuilder()
                .setTraineeId(3)
                .setTrainerId(1)
                .setDuration(24)
                .setTrainingType(TrainingType.TYPE_3)
                .setTrainingName("Training Name 3")
                .setTrainingDate(new Date())
                .build();
        trainingFacade.createTraining(trainingData2);

        TrainingData trainingData4 = new TrainingDataBuilder()
                .setTraineeId(3)
                .setTrainerId(1)
                .setDuration(36)
                .setTrainingType(TrainingType.TYPE_3)
                .setTrainingName("Training Name 3")
                .setTrainingDate(new Date())
                .build();
        trainingFacade.createTraining(trainingData2);
        System.out.println(trainingFacade.getAllTrainings());

        TrainingCriteria trainingCriteria = new TrainingCriteria();
        trainingCriteria.setTrainingName("3");
        trainingCriteria.setDurationMax(25);
        trainingCriteria.setTrainingType(TrainingType.TYPE_3);

        System.out.println(trainingFacade.getByTraineeUsernameAndCriteria("Nikolay.Alexeev1",trainingCriteria));
    }

}
