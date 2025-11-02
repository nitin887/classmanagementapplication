package com.classmanagementsystem.classmanagement.controller;

import com.classmanagementsystem.classmanagement.dto.RegisterDto;
import com.classmanagementsystem.classmanagement.dto.RoleDTO;
import com.classmanagementsystem.classmanagement.dto.UserDTO;
import com.classmanagementsystem.classmanagement.exception.ResourceNotFoundException;
import com.classmanagementsystem.classmanagement.serviceinterface.UserServiceInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceInterface userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO1;
    private UserDTO userDTO2;
    private RegisterDto registerDto;

    @BeforeEach
    void setUp() {
        RoleDTO roleUserDTO = new RoleDTO(1L, "ROLE_USER");
        Set<RoleDTO> roles1 = new HashSet<>(Arrays.asList(roleUserDTO));
        userDTO1 = new UserDTO(1L, "John Doe", "johndoe", "john.doe@example.com", roles1);

        RoleDTO roleAdminDTO = new RoleDTO(2L, "ROLE_ADMIN");
        Set<RoleDTO> roles2 = new HashSet<>(Arrays.asList(roleUserDTO, roleAdminDTO));
        userDTO2 = new UserDTO(2L, "Jane Admin", "janeadmin", "jane.admin@example.com", roles2);

        registerDto = new RegisterDto("New User", "newuser", "new.user@example.com", "password123");
    }

    @Test
    void registerUser_Success() throws Exception {
        when(userService.registerUser(any(RegisterDto.class))).thenReturn(userDTO1);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("johndoe")));

        verify(userService, times(1)).registerUser(any(RegisterDto.class));
    }

    @Test
    void registerUser_InvalidInput() throws Exception {
        RegisterDto invalidRegisterDto = new RegisterDto("", "", "invalid-email", "short");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRegisterDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation Failed")));

        verify(userService, never()).registerUser(any(RegisterDto.class));
    }

    @Test
    void getUserById_Found() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userDTO1);

        mockMvc.perform(get("/api/auth/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("johndoe")));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getUserById_NotFound() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new ResourceNotFoundException("User", "id", 99L));

        mockMvc.perform(get("/api/auth/users/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User not found with id : '99'")));

        verify(userService, times(1)).getUserById(99L);
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() throws Exception {
        List<UserDTO> userList = Arrays.asList(userDTO1, userDTO2);
        when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get("/api/auth/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]").isArray())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("johndoe")));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void updateUser_Success() throws Exception {
        UserDTO updatedDTO = new UserDTO(1L, "John Doe Updated", "johndoe_updated", "john.doe.updated@example.com", userDTO1.getRoles());
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/auth/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("johndoe_updated")));

        verify(userService, times(1)).updateUser(eq(1L), any(UserDTO.class));
    }

    @Test
    void updateUser_NotFound() throws Exception {
        UserDTO nonExistentDTO = new UserDTO(99L, "Non Existent", "nonexistent", "nonexistent@example.com", new HashSet<>());
        when(userService.updateUser(eq(99L), any(UserDTO.class))).thenThrow(new ResourceNotFoundException("User", "id", 99L));

        mockMvc.perform(put("/api/auth/users/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistentDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User not found with id : '99'")));

        verify(userService, times(1)).updateUser(eq(99L), any(UserDTO.class));
    }

    @Test
    void deleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/auth/users/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void deleteUser_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("User", "id", 99L)).when(userService).deleteUser(99L);

        mockMvc.perform(delete("/api/auth/users/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User not found with id : '99'")));

        verify(userService, times(1)).deleteUser(99L);
    }
}
