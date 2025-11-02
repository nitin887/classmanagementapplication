package com.classmanagementsystem.classmanagement.controller;

import com.classmanagementsystem.classmanagement.dto.LoginDto;
import com.classmanagementsystem.classmanagement.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginDto loginDto;

    @BeforeEach
    void setUp() {
        loginDto = new LoginDto("testuser", "password");
    }

    @Test
    void login_Success() throws Exception {
        when(authService.login(any(LoginDto.class))).thenReturn("User logged in successfully!");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User logged in successfully!"));

        verify(authService, times(1)).login(any(LoginDto.class));
    }

    @Test
    void login_Failure() throws Exception {
        when(authService.login(any(LoginDto.class))).thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest()); // Assuming a RuntimeException from service maps to 400

        verify(authService, times(1)).login(any(LoginDto.class));
    }
}
