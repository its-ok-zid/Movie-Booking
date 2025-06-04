package com.tech.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.booking.dto.LoginRequest;
import com.tech.booking.dto.ResetPasswordRequest;
import com.tech.booking.dto.UserDTO;
import com.tech.booking.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class TestUserController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Register user - success")
    void registerUser_success() throws Exception {
        UserDTO request = new UserDTO();
        request.setLoginId("testuser");
        request.setPassword("password");
        request.setConfirmPassword("password");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setEmail("testuser@example.com");
        request.setContactNumber("1234567890");
        UserDTO response = new UserDTO();
        response.setLoginId("testuser");

        Mockito.when(userService.register(any(UserDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1.0/moviebooking/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loginId").value("testuser"));
    }

    @Test
    @DisplayName("Login (GET) - success")
    void loginGet_success() throws Exception {
        Mockito.when(userService.login("testuser", "password")).thenReturn(true);

        mockMvc.perform(get("/api/v1.0/moviebooking/login")
                        .param("loginId", "testuser")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    @DisplayName("Login (GET) - unauthorized")
    void loginGet_unauthorized() throws Exception {
        Mockito.when(userService.login("testuser", "wrongpass")).thenReturn(false);

        mockMvc.perform(get("/api/v1.0/moviebooking/login")
                        .param("loginId", "testuser")
                        .param("password", "wrongpass"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid login credentials"));
    }

    @Test
    @DisplayName("Login (POST) - success")
    void loginPost_success() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setLoginId("testuser");
        request.setPassword("password");

        Mockito.when(userService.login("testuser", "password")).thenReturn(true);

        mockMvc.perform(post("/api/v1.0/moviebooking/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    @DisplayName("Login (POST) - unauthorized")
    void loginPost_unauthorized() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setLoginId("testuser");
        request.setPassword("wrongpass");

        Mockito.when(userService.login("testuser", "wrongpass")).thenReturn(false);

        mockMvc.perform(post("/api/v1.0/moviebooking/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid login credentials"));
    }

    @Test
    @DisplayName("Forgot password - success")
    void forgotPassword_success() throws Exception {
        Mockito.when(userService.forgotPassword("testuser")).thenReturn("Password reset link sent");

        mockMvc.perform(get("/api/v1.0/moviebooking/testuser/forgot"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset link sent"));
    }

    @Test
    @DisplayName("Reset password - success")
    void resetPassword_success() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setLoginId("testuser");
        request.setNewPassword("newpass");
        request.setConfirmPassword("newpass"); // Set required field

        Mockito.when(userService.resetPassword(any(ResetPasswordRequest.class))).thenReturn("Password reset successful");

        mockMvc.perform(post("/api/v1.0/moviebooking/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successful"));
    }
}