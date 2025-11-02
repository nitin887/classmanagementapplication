package com.classmanagementsystem.classmanagement.service;

import com.classmanagementsystem.classmanagement.dto.StudentDTO;
import com.classmanagementsystem.classmanagement.entity.Student;
import com.classmanagementsystem.classmanagement.exception.ResourceNotFoundException;
import com.classmanagementsystem.classmanagement.mapper.StudentMapper;
import com.classmanagementsystem.classmanagement.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        student = new Student(1L, "John Doe", "john.doe@example.com");
        studentDTO = new StudentDTO(1L, "John Doe", "john.doe@example.com");
    }

    @Test
    void createStudent_Success() {
        when(studentMapper.toEntity(studentDTO)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);

        StudentDTO createdStudent = studentService.createStudent(studentDTO);

        assertNotNull(createdStudent);
        assertEquals(studentDTO.getName(), createdStudent.getName());
        verify(studentRepository, times(1)).save(student);
        verify(studentMapper, times(1)).toEntity(studentDTO);
        verify(studentMapper, times(1)).toDTO(student);
    }

    @Test
    void getAllStudents_ReturnsListOfStudents() {
        List<Student> students = Arrays.asList(student, new Student(2L, "Jane Doe", "jane.doe@example.com"));
        List<StudentDTO> studentDTOs = Arrays.asList(studentDTO, new StudentDTO(2L, "Jane Doe", "jane.doe@example.com"));

        when(studentRepository.findAll()).thenReturn(students);
        when(studentMapper.toDTO(any(Student.class))).thenAnswer(invocation -> {
            Student s = invocation.getArgument(0);
            return new StudentDTO(s.getId(), s.getName(), s.getEmail());
        });

        List<StudentDTO> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentRepository, times(1)).findAll();
        verify(studentMapper, times(2)).toDTO(any(Student.class));
    }

    @Test
    void getStudentById_Found() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);

        StudentDTO foundStudent = studentService.getStudentById(1L);

        assertNotNull(foundStudent);
        assertEquals(studentDTO.getId(), foundStudent.getId());
        verify(studentRepository, times(1)).findById(1L);
        verify(studentMapper, times(1)).toDTO(student);
    }

    @Test
    void getStudentById_NotFound_ThrowsException() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(1L));
        verify(studentRepository, times(1)).findById(1L);
        verify(studentMapper, never()).toDTO(any(Student.class));
    }

    @Test
    void updateStudent_FoundAndUpdated() {
        StudentDTO updatedStudentDTO = new StudentDTO(1L, "Johnathan Doe", "johnathan.doe@example.com");
        Student updatedStudentEntity = new Student(1L, "Johnathan Doe", "johnathan.doe@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudentEntity);
        when(studentMapper.toDTO(updatedStudentEntity)).thenReturn(updatedStudentDTO);

        StudentDTO result = studentService.updateStudent(1L, updatedStudentDTO);

        assertNotNull(result);
        assertEquals(updatedStudentDTO.getName(), result.getName());
        assertEquals(updatedStudentDTO.getEmail(), result.getEmail());
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(any(Student.class));
        verify(studentMapper, times(1)).toDTO(updatedStudentEntity);
    }

    @Test
    void updateStudent_NotFound_ThrowsException() {
        StudentDTO updatedStudentDTO = new StudentDTO(1L, "Johnathan Doe", "johnathan.doe@example.com");
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.updateStudent(1L, updatedStudentDTO));
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, never()).save(any(Student.class));
        verify(studentMapper, never()).toDTO(any(Student.class));
    }

    @Test
    void deleteStudent_FoundAndDeleted() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).delete(student);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).delete(student);
    }

    @Test
    void deleteStudent_NotFound_ThrowsException() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(1L));
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, never()).delete(any(Student.class));
    }
}
