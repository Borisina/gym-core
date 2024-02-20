package com.kolya.gym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.ChangePasswordData;
import com.kolya.gym.domain.User;
import com.kolya.gym.service.JwtService;
import com.kolya.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    public void loginSuccessfully() throws Exception {
        AuthData authData = new AuthData("username", "password");
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        when(userService.authorize(any(AuthData.class))).thenReturn(user);
        when(jwtService.generateToken(any(UUID.class),any(UserDetails.class))).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/login")
                        .content(asJsonString(authData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void logoutSuccessfully() throws Exception {
        mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser
    public void changeLoginSuccessfully() throws Exception {
        ChangePasswordData changeData = new ChangePasswordData("username", "password", "newpassword");

        mockMvc.perform(put("/change-login")
                        .content(asJsonString(changeData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void loginFailureInvalidPassword() throws Exception {
        AuthData authData = new AuthData("username", ""); // Передаем пустой пароль
        when(userService.authorize(any(AuthData.class))).thenThrow(new IllegalArgumentException("Wrong parameter 'password': cant be empty"));

        mockMvc.perform(post("/login")
                        .content(asJsonString(authData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // ожидаем код статуса 400
    }

    @Test
    public void changeLoginFailureInvalidPassword() throws Exception {
        ChangePasswordData changeData = new ChangePasswordData("username", "", "newpassword"); // Отправляем пустой старый пароль
        doThrow(new IllegalArgumentException("Wrong parameter 'password': cant be empty")).when(userService).changePassword(any(UUID.class), any(ChangePasswordData.class));

        mockMvc.perform(put("/change-login")
                        .content(asJsonString(changeData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // ожидаем код статуса 400
    }

}