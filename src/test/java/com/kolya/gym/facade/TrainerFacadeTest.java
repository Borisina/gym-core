package com.kolya.gym.facade;

import com.kolya.gym.data.TrainerData;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.User;
import com.kolya.gym.service.TrainerService;
import com.kolya.gym.service.UserService;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class TrainerFacadeTest {

    @InjectMocks
    private TrainerFacade trainerFacade;

    @Mock
    private TrainerService trainerService;

    @Mock
    private UserService userService;

    @Mock
    private Logger logger;

    private Trainer trainer;
    private User user;
    private TrainerData trainerData;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        trainer = new Trainer();
        trainer.setId(1L);

        user = new User();
        user.setId(1L);
        trainer.setUser(user);

        trainerData = new TrainerData();
        trainerData.setFirstName("first");
        trainerData.setLastName("last");
        trainerData.setSpecialization("basketball");
    }

    @Test
    public void testCreateTrainer() {
        when(userService.create(any(String.class), any(String.class))).thenReturn(user);
        when(trainerService.create(any(TrainerData.class), any(User.class))).thenReturn(trainer);
        Trainer result = trainerFacade.createTrainer(trainerData);
        assertNotNull(result);
    }

    @Test
    public void testUpdateTrainer() {
        when(trainerService.get(any(Long.class))).thenReturn(trainer);
        when(userService.update(any(String.class), any(String.class), any(Long.class))).thenReturn(user);
        when(trainerService.update(any(TrainerData.class), any(User.class), any(Long.class))).thenReturn(trainer);
        Trainer result = trainerFacade.updateTrainer(trainerData,any(Long.class));
        assertNotNull(result);
    }

    @Test
    public void testGetTrainer() {
        when(trainerService.get(1L)).thenReturn(trainer);
        Trainer result = trainerFacade.getTrainer(1L);
        assertNotNull(result);
    }

    // invalid data triggers catch block ensuring returned Trainer is null
    @Test
    public void testCreateTrainerWithInvalidData() {
        trainerData.setFirstName(""); // invalid
        Trainer result = trainerFacade.createTrainer(trainerData);
        assertNull(result);

        trainerData.setFirstName("Ivanov4"); // invalid
        result = trainerFacade.createTrainer(trainerData);
        assertNull(result);
    }
}