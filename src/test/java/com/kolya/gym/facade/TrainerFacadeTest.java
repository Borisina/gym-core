package com.kolya.gym.facade;

import com.kolya.gym.data.AuthData;
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

import javax.naming.AuthenticationException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class TrainerFacadeTest {

    @InjectMocks
    TrainerFacade facade;

    @Mock
    TrainerService trainerService;

    @Mock
    UserService userService;

    @Mock
    Logger logger;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateTrainer() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername("First.Last");
        trainer.setUser(user);
        TrainerData trainerData = new TrainerData();
        trainerData.setFirstName("First");
        trainerData.setLastName("Last");
        trainerData.setSpecialization("Spec");
        when(trainerService.create(trainerData)).thenReturn(trainer);
        Trainer result = facade.createTrainer(trainerData);
        assertEquals(trainer, result);
    }

    @Test
    public void testUpdateTrainer() {
        AuthData authData = new AuthData();
        Trainer trainer = new Trainer();
        TrainerData trainerData = new TrainerData();
        when(trainerService.update(trainerData, 1L)).thenReturn(trainer);
        Trainer result = facade.updateTrainer(authData, trainerData, 1L);
        assertEquals(trainer, result);
    }

    @Test
    public void testGetTrainer() {
        Trainer trainer = new Trainer();
        when(trainerService.get(1L)).thenReturn(trainer);
        Trainer result = facade.getTrainer(1L);
        assertEquals(trainer, result);
    }

    @Test
    public void testGetByUsername() {
        Trainer trainer = new Trainer();
        when(trainerService.getByUsername("test")).thenReturn(trainer);
        Trainer result = facade.getByUsername("test");
        assertEquals(trainer, result);
    }

    @Test
    public void testGetAllTrainers() {
        when(trainerService.getAll()).thenReturn(Arrays.asList(new Trainer(), new Trainer()));
        List<Trainer> result = facade.getAllTrainers();
        assertEquals(2, result.size());
    }

    @Test
    public void createTrainer_NullData_Test() {
        assertNull(facade.createTrainer(null));
    }

    @Test
    public void updateTrainer_NullAuthData_Test() throws AuthenticationException {
        when(userService.authenticate(null)).thenThrow(IllegalArgumentException.class);
        TrainerData mockTrainerData = mock(TrainerData.class);

        assertNull(facade.updateTrainer(null, mockTrainerData, 1L));
    }

    @Test
    public void updateTrainer_NullTrainerData_Test() throws AuthenticationException {
        AuthData mockAuth = mock(AuthData.class);
        when(userService.authenticate(mockAuth)).thenReturn(null);

        assertNull(facade.updateTrainer(mockAuth, null, 1L));
    }

    @Test
    public void getTrainer_InvalidId_Test() {
        when(trainerService.get(-1L)).thenThrow(IllegalArgumentException.class);

        assertNull(facade.getTrainer(-1));
    }

    @Test
    public void getByUsername_NullUsername_Test() {
        when(trainerService.getByUsername(null)).thenThrow(IllegalArgumentException.class);

        assertNull(facade.getByUsername(null));
    }

    @Test
    public void createTrainer_InvalidData_Test() {
        TrainerData mockTrainerData = mock(TrainerData.class);
        doThrow(IllegalArgumentException.class).when(mockTrainerData).validate();

        assertNull(facade.createTrainer(mockTrainerData));
    }

    @Test
    public void updateTrainer_InvalidAuthData_Test() throws AuthenticationException {
        AuthData invalidAuthData = mock(AuthData.class);
        when(userService.authenticate(invalidAuthData)).thenThrow(AuthenticationException.class);
        TrainerData mockTrainerData = mock(TrainerData.class);

        assertNull(facade.updateTrainer(invalidAuthData, mockTrainerData, 1L));
    }

    @Test
    public void updateTrainer_InvalidTrainerData_Test() throws AuthenticationException {
        AuthData validAuthData = mock(AuthData.class);
        when(userService.authenticate(validAuthData)).thenReturn(null);

        TrainerData invalidTrainerData = mock(TrainerData.class);
        doThrow(IllegalArgumentException.class).when(invalidTrainerData).validateCharacters();

        assertNull(facade.updateTrainer(validAuthData, invalidTrainerData, 1L));
    }

}