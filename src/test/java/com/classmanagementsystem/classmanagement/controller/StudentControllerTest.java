package com.classmanagementsystem.classmanagement.controller;

import com.classmanagementsystem.classmanagement.dto.StudentDTO;
import com.classmanagementsystem.classmanagement.exception.ResourceNotFoundException;
import com.classmanagementsystem.classmanagement.serviceinterface.StudentServiceInterface;
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

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentServiceInterface studentService;

    @Autowired
    private ObjectMapper objectMapper; // Used to convert objects to JSON

    private StudentDTO studentDTO1;
    private StudentDTO studentDTO2;

    @BeforeEach
    void setUp() {
        studentDTO1 = new StudentDTO(1L, "Alice", "alice@example.com");
        studentDTO2 = new StudentDTO(2L, "Bob", "bob@example.com");
    }

    @Test
    void createStudent_Success() throws Exception {
        when(studentService.createStudent(any(StudentDTO.class))).thenReturn(studentDTO1);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDTO1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Alice")));

        verify(studentService, times(1)).createStudent(any(StudentDTO.class));
    }

    @Test
    void createStudent_InvalidInput() throws Exception {
        StudentDTO invalidStudentDTO = new StudentDTO(null, "A", "invalid-email"); // Invalid name and email

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStudentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation Failed"))); // Expecting validation error from GlobalExceptionHandler

        verify(studentService, never()).createStudent(any(StudentDTO.class));
    }

    @Test
    void getAllStudents_ReturnsListOfStudents() throws Exception {
        List<StudentDTO> studentList = Arrays.asList(studentDTO1, studentDTO2);
        when(studentService.getAllStudents()).thenReturn(studentList);

        mockMvc.perform(get("/api/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]").isArray())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Alice")));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void getStudentById_Found() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(studentDTO1);

        mockMvc.perform(get("/api/students/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Alice")));

        verify(studentService, times(1)).getStudentById(1L);
    }

    @Test
    void getStudentById_NotFound() throws Exception {
        when(studentService.getStudentById(99L)).thenThrow(new ResourceNotFoundException("Student", "id", 99L));

        mockMvc.perform(get("/api/students/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Student not found with id : '99'")));

        verify(studentService, times(1)).getStudentById(99L);
    }

    @Test
    void updateStudent_Success() throws Exception {
        StudentDTO updatedDTO = new StudentDTO(1L, "Alice Updated", "alice.updated@example.com");
        when(studentService.updateStudent(eq(1L), any(StudentDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/students/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Alice Updated")));

        verify(studentService, times(1)).updateStudent(eq(1L), any(StudentDTO.class));
    }

    @Test
    void updateStudent_NotFound() throws Exception {
        StudentDTO nonExistentDTO = new StudentDTO(99L, "Non Existent", "nonexistent@example.com");
        when(studentService.updateStudent(eq(99L), any(StudentDTO.class))).thenThrow(new ResourceNotFoundException("Student", "id", 99L));

        mockMvc.perform(put("/api/students/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistentDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Student not found with id : '99'")));

        verify(studentService, times(1)).updateStudent(eq(99L), any(StudentDTO.class));
    }

    @Test
    void deleteStudent_Success() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/api/students/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(studentService, times(1)).deleteStudent(1L);
    }

    @Test
    void deleteStudent_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Student", "id", 99L)).when(studentService).deleteStudent(99L);

        mockMvc.perform(delete("/api/students/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Student not found with id : '99'")));

        verify(studentService, times(1)).deleteStudent(99L);
    }
}
