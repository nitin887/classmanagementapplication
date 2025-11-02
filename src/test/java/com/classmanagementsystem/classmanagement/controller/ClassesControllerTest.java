package com.classmanagementsystem.classmanagement.controller;

import com.classmanagementsystem.classmanagement.dto.ClassesDTO;
import com.classmanagementsystem.classmanagement.dto.StudentDTO;
import com.classmanagementsystem.classmanagement.dto.TeacherDTO;
import com.classmanagementsystem.classmanagement.exception.ResourceNotFoundException;
import com.classmanagementsystem.classmanagement.serviceinterface.ClassesServiceInterface;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClassesController.class)
public class ClassesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClassesServiceInterface classesService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClassesDTO classesDTO1;
    private ClassesDTO classesDTO2;
    private TeacherDTO teacherDTO;
    private StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        teacherDTO = new TeacherDTO(1L, "Prof. X", "Mutant Studies");
        studentDTO = new StudentDTO(10L, "Jean Grey", "jean@xmen.com");
        classesDTO1 = new ClassesDTO(100L, "Telekinesis 101", teacherDTO, new HashSet<>(Arrays.asList(studentDTO)));
        classesDTO2 = new ClassesDTO(101L, "Combat Training", teacherDTO, new HashSet<>());
    }

    @Test
    void createClasses_Success() throws Exception {
        when(classesService.createClasses(any(ClassesDTO.class))).thenReturn(classesDTO1);

        mockMvc.perform(post("/api/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(classesDTO1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.name", is("Telekinesis 101")));

        verify(classesService, times(1)).createClasses(any(ClassesDTO.class));
    }

    @Test
    void createClasses_InvalidInput() throws Exception {
        ClassesDTO invalidClassesDTO = new ClassesDTO(null, "A", null, null); // Invalid name

        mockMvc.perform(post("/api/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidClassesDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation Failed")));

        verify(classesService, never()).createClasses(any(ClassesDTO.class));
    }

    @Test
    void getAllClasses_ReturnsListOfClasses() throws Exception {
        List<ClassesDTO> classesList = Arrays.asList(classesDTO1, classesDTO2);
        when(classesService.getAllClasses()).thenReturn(classesList);

        mockMvc.perform(get("/api/classes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]").isArray())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Telekinesis 101")));

        verify(classesService, times(1)).getAllClasses();
    }

    @Test
    void getClassesById_Found() throws Exception {
        when(classesService.getClassesById(100L)).thenReturn(classesDTO1);

        mockMvc.perform(get("/api/classes/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.name", is("Telekinesis 101")));

        verify(classesService, times(1)).getClassesById(100L);
    }

    @Test
    void getClassesById_NotFound() throws Exception {
        when(classesService.getClassesById(99L)).thenThrow(new ResourceNotFoundException("Classes", "id", 99L));

        mockMvc.perform(get("/api/classes/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Classes not found with id : '99'")));

        verify(classesService, times(1)).getClassesById(99L);
    }

    @Test
    void updateClasses_Success() throws Exception {
        ClassesDTO updatedDTO = new ClassesDTO(100L, "Advanced Telekinesis", teacherDTO, new HashSet<>());
        when(classesService.updateClasses(eq(100L), any(ClassesDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/classes/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.name", is("Advanced Telekinesis")));

        verify(classesService, times(1)).updateClasses(eq(100L), any(ClassesDTO.class));
    }

    @Test
    void updateClasses_NotFound() throws Exception {
        ClassesDTO nonExistentDTO = new ClassesDTO(99L, "Non Existent", null, null);
        when(classesService.updateClasses(eq(99L), any(ClassesDTO.class))).thenThrow(new ResourceNotFoundException("Classes", "id", 99L));

        mockMvc.perform(put("/api/classes/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistentDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Classes not found with id : '99'")));

        verify(classesService, times(1)).updateClasses(eq(99L), any(ClassesDTO.class));
    }

    @Test
    void deleteClasses_Success() throws Exception {
        doNothing().when(classesService).deleteClasses(100L);

        mockMvc.perform(delete("/api/classes/{id}", 100L))
                .andExpect(status().isNoContent());

        verify(classesService, times(1)).deleteClasses(100L);
    }

    @Test
    void deleteClasses_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Classes", "id", 99L)).when(classesService).deleteClasses(99L);

        mockMvc.perform(delete("/api/classes/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Classes not found with id : '99'")));

        verify(classesService, times(1)).deleteClasses(99L);
    }

    @Test
    void enrollStudentInClasses_Success() throws Exception {
        ClassesDTO enrolledClassesDTO = new ClassesDTO(100L, "Telekinesis 101", teacherDTO, new HashSet<>(Arrays.asList(studentDTO)));
        when(classesService.enrollStudentInClasses(100L, 10L)).thenReturn(enrolledClassesDTO);

        mockMvc.perform(put("/api/classes/{classesId}/students/{studentId}", 100L, 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.students", hasSize(1)))
                .andExpect(jsonPath("$.students[0].id", is(10)));

        verify(classesService, times(1)).enrollStudentInClasses(100L, 10L);
    }

    @Test
    void enrollStudentInClasses_ClassesNotFound() throws Exception {
        when(classesService.enrollStudentInClasses(99L, 10L)).thenThrow(new ResourceNotFoundException("Classes", "id", 99L));

        mockMvc.perform(put("/api/classes/{classesId}/students/{studentId}", 99L, 10L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Classes not found with id : '99'")));

        verify(classesService, times(1)).enrollStudentInClasses(99L, 10L);
    }

    @Test
    void enrollStudentInClasses_StudentNotFound() throws Exception {
        when(classesService.enrollStudentInClasses(100L, 99L)).thenThrow(new ResourceNotFoundException("Student", "id", 99L));

        mockMvc.perform(put("/api/classes/{classesId}/students/{studentId}", 100L, 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Student not found with id : '99'")));

        verify(classesService, times(1)).enrollStudentInClasses(100L, 99L);
    }
}
