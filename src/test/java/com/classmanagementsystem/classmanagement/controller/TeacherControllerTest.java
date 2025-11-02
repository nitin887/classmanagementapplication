package com.classmanagementsystem.classmanagement.controller;

import com.classmanagementsystem.classmanagement.dto.TeacherDTO;
import com.classmanagementsystem.classmanagement.exception.ResourceNotFoundException;
import com.classmanagementsystem.classmanagement.serviceinterface.TeacherServiceInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherController.class)
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherServiceInterface teacherService;

    @Autowired
    private ObjectMapper objectMapper;

    private TeacherDTO teacherDTO1;
    private TeacherDTO teacherDTO2;

    @BeforeEach
    void setUp() {
        teacherDTO1 = new TeacherDTO(1L, "Mr. Smith", "Math");
        teacherDTO2 = new TeacherDTO(2L, "Ms. Jones", "Physics");
    }

    @Test
    void createTeacher_Success() throws Exception {
        when(teacherService.createTeacher(any(TeacherDTO.class))).thenReturn(teacherDTO1);

        mockMvc.perform(post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacherDTO1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mr. Smith")));

        verify(teacherService, times(1)).createTeacher(any(TeacherDTO.class));
    }

    @Test
    void createTeacher_InvalidInput() throws Exception {
        TeacherDTO invalidTeacherDTO = new TeacherDTO(null, "A", ""); // Invalid name and subject

        mockMvc.perform(post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTeacherDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation Failed")));

        verify(teacherService, never()).createTeacher(any(TeacherDTO.class));
    }

    @Test
    void getAllTeachers_ReturnsListOfTeachers() throws Exception {
        List<TeacherDTO> teacherList = Arrays.asList(teacherDTO1, teacherDTO2);
        when(teacherService.getAllTeachers()).thenReturn(teacherList);

        mockMvc.perform(get("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]").isArray())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Mr. Smith")));

        verify(teacherService, times(1)).getAllTeachers();
    }

    @Test
    void getTeacherById_Found() throws Exception {
        when(teacherService.getTeacherById(1L)).thenReturn(teacherDTO1);

        mockMvc.perform(get("/api/teachers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mr. Smith")));

        verify(teacherService, times(1)).getTeacherById(1L);
    }

    @Test
    void getTeacherById_NotFound() throws Exception {
        when(teacherService.getTeacherById(99L)).thenThrow(new ResourceNotFoundException("Teacher", "id", 99L));

        mockMvc.perform(get("/api/teachers/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Teacher not found with id : '99'")));

        verify(teacherService, times(1)).getTeacherById(99L);
    }

    @Test
    void updateTeacher_Success() throws Exception {
        TeacherDTO updatedDTO = new TeacherDTO(1L, "Mr. Smith Updated", "Chemistry");
        when(teacherService.updateTeacher(eq(1L), any(TeacherDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/teachers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mr. Smith Updated")));

        verify(teacherService, times(1)).updateTeacher(eq(1L), any(TeacherDTO.class));
    }

    @Test
    void updateTeacher_NotFound() throws Exception {
        TeacherDTO nonExistentDTO = new TeacherDTO(99L, "Non Existent", "Art");
        when(teacherService.updateTeacher(eq(99L), any(TeacherDTO.class))).thenThrow(new ResourceNotFoundException("Teacher", "id", 99L));

        mockMvc.perform(put("/api/teachers/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistentDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Teacher not found with id : '99'")));

        verify(teacherService, times(1)).updateTeacher(eq(99L), any(TeacherDTO.class));
    }

    @Test
    void deleteTeacher_Success() throws Exception {
        doNothing().when(teacherService).deleteTeacher(1L);

        mockMvc.perform(delete("/api/teachers/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(teacherService, times(1)).deleteTeacher(1L);
    }

    @Test
    void deleteTeacher_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Teacher", "id", 99L)).when(teacherService).deleteTeacher(99L);

        mockMvc.perform(delete("/api/teachers/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Teacher not found with id : '99'")));

        verify(teacherService, times(1)).deleteTeacher(99L);
    }
}
