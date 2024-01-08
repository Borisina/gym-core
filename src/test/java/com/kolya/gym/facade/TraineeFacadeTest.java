package com.kolya.gym.facade;

import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.TraineeData;
import com.kolya.gym.data.TraineeDataUpdate;
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

import javax.naming.AuthenticationException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class TraineeFacadeTest {

    @InjectMocks
    TraineeFacade facade;

    @Mock
    TraineeService traineeService;

    @Mock
    UserService userService;

    @Mock
    Logger logger;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateTrainee() {
        TraineeData traineeData = new TraineeData();
        traineeData.setFirstName("Nikolay");
        traineeData.setLastName("Alexeev");
        User user = new User();
        user.setUsername("Nikolay.Alexeev");
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        when(traineeService.create(traineeData)).thenReturn(trainee);
        Trainee result = facade.createTrainee(traineeData);
        assertEquals(trainee, result);
    }

    @Test
    public void testUpdateTrainee() {
        AuthData authData = new AuthData();
        TraineeDataUpdate updateData = new TraineeDataUpdate();
        updateData.setId(1L);
        Trainee trainee = new Trainee();
        when(traineeService.update(updateData)).thenReturn(trainee);
        Trainee result = facade.updateTrainee(authData, updateData);
        assertEquals(trainee, result);
    }

    @Test
    public void testUpdateTraineeList() {
        AuthData authData = new AuthData();
        List<TraineeDataUpdate> updateList = Arrays.asList(new TraineeDataUpdate(), new TraineeDataUpdate());
        when(traineeService.updateList(updateList)).thenReturn(Arrays.asList(new Trainee(), new Trainee()));
        List<Trainee> result = facade.updateTraineeList(authData, updateList);
        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteByUsername() {
        AuthData authData = new AuthData();
        Trainee trainee = new Trainee();
        when(traineeService.deleteByUsername("test")).thenReturn(trainee);
        Trainee result = facade.deleteByUsername(authData, "test");
        assertEquals(trainee, result);
    }

    @Test
    public void testGetTrainee() {
        Trainee trainee = new Trainee();
        when(traineeService.get(1L)).thenReturn(trainee);
        Trainee result = facade.getTrainee(1L);
        assertEquals(trainee, result);
    }

    @Test
    public void testGetByUsername() {
        Trainee trainee = new Trainee();
        when(traineeService.getByUsername("test")).thenReturn(trainee);
        Trainee result = facade.getByUsername("test");
        assertEquals(trainee, result);
    }

    @Test
    public void testGetAllTrainees() {
        when(traineeService.getAll()).thenReturn(Arrays.asList(new Trainee(), new Trainee()));
        List<Trainee> result = facade.getAllTrainees();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetNotAssignedTrainees() {
        when(traineeService.getNotAssigned()).thenReturn(Arrays.asList(new Trainee(), new Trainee()));
        List<Trainee> result = facade.getNotAssignedTrainees();
        assertEquals(2, result.size());
    }

    @Test
    public void createTrainee_NullData_Test() {
        assertNull(facade.createTrainee(null));
    }

    @Test
    public void updateTrainee_NullAuthData_Test() throws AuthenticationException {
        when(userService.authenticate(null)).thenThrow(IllegalArgumentException.class);
        TraineeDataUpdate mockUpdate = mock(TraineeDataUpdate.class);

        assertNull(facade.updateTrainee(null, mockUpdate));
    }

    @Test
    public void updateTrainee_NullUpdateData_Test() throws AuthenticationException {
        AuthData mockAuth = mock(AuthData.class);
        when(userService.authenticate(mockAuth)).thenReturn(null);

        assertNull(facade.updateTrainee(mockAuth, null));
    }

    @Test
    public void updateTraineeList_NullAuthData_Test() throws AuthenticationException {
        when(userService.authenticate(null)).thenThrow(IllegalArgumentException.class);
        List<TraineeDataUpdate> mockUpdateList = mock(List.class);

        assertNull(facade.updateTraineeList(null, mockUpdateList));
    }

    @Test
    public void updateTraineeList_NullUpdateData_Test() throws AuthenticationException {
        AuthData mockAuth = mock(AuthData.class);
        when(userService.authenticate(mockAuth)).thenReturn(null);

        assertNull(facade.updateTraineeList(mockAuth, null));
    }

    @Test
    public void deleteByUsername_NullAuthData_Test() throws AuthenticationException {
        when(userService.authenticate(null)).thenThrow(IllegalArgumentException.class);

        assertNull(facade.deleteByUsername(null, "username"));
    }

    @Test
    public void getTrainee_InvalidId_Test() {
        when(traineeService.get(-1L)).thenThrow(IllegalArgumentException.class);

        assertNull(facade.getTrainee(-1));
    }

    @Test
    public void getByUsername_NullUsername_Test() {
        when(traineeService.getByUsername(null)).thenThrow(IllegalArgumentException.class);

        assertNull(facade.getByUsername(null));
    }

}