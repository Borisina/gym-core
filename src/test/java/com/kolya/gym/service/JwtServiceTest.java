package com.kolya.gym.service;

import com.kolya.gym.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.crypto.KeyGenerator;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtServiceTest {

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtService jwtService;

    private String username = "testUser";
    private String password = "testPassword";
    private UserDetails userDetails;

    private String SECRET_KEY;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException, NoSuchAlgorithmException {
        SECRET_KEY = Base64.getEncoder().encodeToString(KeyGenerator.getInstance("HmacSHA256").generateKey().getEncoded());
        Field field = jwtService.getClass().getDeclaredField("SECRET_KEY");
        field.setAccessible(true);
        field.set(jwtService, SECRET_KEY);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(true);
        userDetails = user;
    }

    @Test
    public void generateTokenTest() {
        UUID transactionId = UUID.randomUUID();
        String token = jwtService.generateToken(transactionId, userDetails);
        assertNotNull(token);
    }

    @Test
    public void createTokenTest() throws Exception {
        Field fieldSet = jwtService.getClass().getDeclaredField("SECRET_KEY");
        fieldSet.setAccessible(true);
        assertNotNull(((String)fieldSet.get(jwtService)));
    }

    @Test
    public void isTokenValidTest() {
        when(userDetailsService.loadUserByUsername(userDetails.getUsername())).thenReturn(userDetails);
        String token = jwtService.generateToken(UUID.randomUUID(), userDetails);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    public void extractUsernameTest() {
        String token = jwtService.generateToken(UUID.randomUUID(), userDetails);
        assertEquals("testUser", jwtService.extractUsernameOrReturnNull(token));
    }

    @Test
    public void extractUsernameForInvalidTokenTest() {
        assertNull(jwtService.extractUsernameOrReturnNull("invalidToken"));
    }

    @Test
    public void getTokenForServicesTest_nonNull() {
        assertNotNull(jwtService.getTokenForServices(UUID.randomUUID()));
    }

}