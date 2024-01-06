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

import static com.kolya.gym.prepareddata.PreparedData.*;


public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Logger logger = context.getBean(Logger.class);
        TraineeFacade traineeFacade = context.getBean(TraineeFacade.class);
        TrainerFacade trainerFacade = context.getBean(TrainerFacade.class);
        TrainingFacade trainingFacade = context.getBean(TrainingFacade.class);

        logger.info("Application context is ready!");

        TraineeData wrongTraineeData = wrongTraineeDataList.get(0);
        traineeFacade.createTrainee(wrongTraineeData);

        TraineeData wrongTraineeData2 = wrongTraineeDataList.get(1);
        traineeFacade.createTrainee(wrongTraineeData2);

        TraineeData traineeData = traineeDataList.get(0);
        Trainee trainee= traineeFacade.createTrainee(traineeData);
        AuthData authData = new AuthData(trainee.getUser().getUsername(),trainee.getUser().getPassword());

        TraineeData traineeData2 = traineeDataList.get(1);
        traineeFacade.createTrainee(traineeData2);

        traineeFacade.changePassword(authData,"password");
        trainee = traineeFacade.getTrainee(1);
        System.out.println(trainee.getUser().getPassword());
        authData.setPassword("password");

        traineeFacade.createTrainee(traineeData);
        System.out.println(traineeFacade.getAllTrainees());


        TraineeDataUpdate traineeDataUpdate = new TraineeDataUpdate();
        traineeDataUpdate.setLastName("Ivanov");
        traineeDataUpdate.setId(2);
        traineeFacade.updateTrainee(authData,traineeDataUpdate);
        System.out.println(traineeFacade.getAllTrainees());


        TrainerData wrongTrainerData = wrongTrainerDataList.get(0);
        trainerFacade.createTrainer(wrongTrainerData);
        System.out.println(trainerFacade.getAllTrainers());

        TrainerData trainerData = trainerDataList.get(0);
        trainerFacade.createTrainer(trainerData);
        System.out.println(trainerFacade.getAllTrainers());

        TrainingData trainingData = trainingDataList.get(0);
        trainingFacade.createTraining(trainingData);
        System.out.println(trainingFacade.getAllTrainings());



        TrainingData wrongTrainingData = wrongTrainingDataList.get(0);
        trainingFacade.createTraining(wrongTrainingData);
        System.out.println(trainingFacade.getAllTrainings());

        traineeFacade.deleteByUsername(authData,"Nikolay.Alexeev");
        System.out.println(trainingFacade.getAllTrainings());

        System.out.println(traineeFacade.getNotAssignedTrainees());

        traineeFacade.changeActiveStatus(2);

        System.out.println(traineeFacade.getNotAssignedTrainees());


        TrainingData trainingData2 = trainingDataList.get(1);
        trainingFacade.createTraining(trainingData2);

        TrainingData trainingData3 = trainingDataList.get(2);
        trainingFacade.createTraining(trainingData3);

        TrainingData trainingData4 = trainingDataList.get(3);
        trainingFacade.createTraining(trainingData4);
        System.out.println(trainingFacade.getAllTrainings());

        TrainingCriteria trainingCriteria = new TrainingCriteria();
        trainingCriteria.setTrainingName("3");
        trainingCriteria.setDurationMax(25);
        trainingCriteria.setTrainingType(TrainingType.TYPE_3);

        System.out.println(trainingFacade.getByTraineeUsernameAndCriteria("Nikolay.Alexeev1",trainingCriteria));
    }

}
