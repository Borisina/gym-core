package com.kolya.gym.facade;

import com.kolya.gym.data.TraineeData;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.User;
import com.kolya.gym.service.TraineeService;
import com.kolya.gym.service.UserService;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TraineeFacadeTest {

    @InjectMocks
    private TraineeFacade traineeFacade;

    @Mock
    private TraineeService traineeService;

    @Mock
    private UserService userService;

    @Mock
    private Logger logger;

    private Trainee trainee;
    private User user;
    private TraineeData traineeData;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        trainee = new Trainee();
        trainee.setId(1L);

        user = new User();
        user.setId(1L);
        trainee.setUser(user);

        traineeData = new TraineeData();
        traineeData.setFirstName("first");
        traineeData.setLastName("last");
    }

    @Test
    public void testCreateTrainee() {
        when(userService.create(any(String.class), any(String.class))).thenReturn(user);
        when(traineeService.create(any(TraineeData.class), any(User.class))).thenReturn(trainee);
        Trainee result = traineeFacade.createTrainee(traineeData);
        assertNotNull(result);
    }

    @Test
    public void testUpdateTrainee() {
        when(traineeService.get(any(Long.class))).thenReturn(trainee);
        when(userService.update(any(String.class), any(String.class), any(Long.class))).thenReturn(user);
        when(traineeService.update(any(TraineeData.class), any(User.class), any(Long.class))).thenReturn(trainee);
        Trainee result = traineeFacade.updateTrainee(traineeData,any(Long.class));
        assertNotNull(result);
    }

    @Test
    public void testDeleteTrainee() {
        when(traineeService.delete(1L)).thenReturn(trainee);
        when(userService.delete(1L)).thenReturn(user);
        Trainee result = traineeFacade.deleteTrainee(1L);
        assertNotNull(result);
    }

    @Test
    public void testGetTrainee() {
        when(traineeService.get(1L)).thenReturn(trainee);
        Trainee result = traineeFacade.getTrainee(1L);
        assertNotNull(result);
    }

    // invalid data triggers catch block ensuring returned Trainee is null
    @Test
    public void testCreateTraineeWithInvalidData() {
        traineeData.setFirstName(""); // invalid
        Trainee result = traineeFacade.createTrainee(traineeData);
        assertNull(result);

        traineeData.setFirstName("Ivanov4"); // invalid
        result = traineeFacade.createTrainee(traineeData);
        assertNull(result);

    }
}