package com.classmanagementsystem.classmanagement.service;

import com.classmanagementsystem.classmanagement.dto.TeacherDTO;
import com.classmanagementsystem.classmanagement.entity.Teacher;
import com.classmanagementsystem.classmanagement.exception.ResourceNotFoundException;
import com.classmanagementsystem.classmanagement.mapper.TeacherMapper;
import com.classmanagementsystem.classmanagement.repository.TeacherRepository;
import com.classmanagementsystem.classmanagement.serviceinterface.TeacherServiceInterface;
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
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;
    private TeacherDTO teacherDTO;

    @BeforeEach
    void setUp() {
        teacher = new Teacher(1L, "Mr. Smith", "Math");
        teacherDTO = new TeacherDTO(1L, "Mr. Smith", "Math");
    }

    @Test
    void createTeacher_Success() {
        when(teacherMapper.toEntity(teacherDTO)).thenReturn(teacher);
        when(teacherRepository.save(teacher)).thenReturn(teacher);
        when(teacherMapper.toDTO(teacher)).thenReturn(teacherDTO);

        TeacherDTO createdTeacher = teacherService.createTeacher(teacherDTO);

        assertNotNull(createdTeacher);
        assertEquals(teacherDTO.getName(), createdTeacher.getName());
        verify(teacherRepository, times(1)).save(teacher);
        verify(teacherMapper, times(1)).toEntity(teacherDTO);
        verify(teacherMapper, times(1)).toDTO(teacher);
    }

    @Test
    void getAllTeachers_ReturnsListOfTeachers() {
        List<Teacher> teachers = Arrays.asList(teacher, new Teacher(2L, "Ms. Jones", "Physics"));
        List<TeacherDTO> teacherDTOs = Arrays.asList(teacherDTO, new TeacherDTO(2L, "Ms. Jones", "Physics"));

        when(teacherRepository.findAll()).thenReturn(teachers);
        when(teacherMapper.toDTO(any(Teacher.class))).thenAnswer(invocation -> {
            Teacher t = invocation.getArgument(0);
            return new TeacherDTO(t.getId(), t.getName(), t.getSubject());
        });

        List<TeacherDTO> result = teacherService.getAllTeachers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(teacherRepository, times(1)).findAll();
        verify(teacherMapper, times(2)).toDTO(any(Teacher.class));
    }

    @Test
    void getTeacherById_Found() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toDTO(teacher)).thenReturn(teacherDTO);

        TeacherDTO foundTeacher = teacherService.getTeacherById(1L);

        assertNotNull(foundTeacher);
        assertEquals(teacherDTO.getId(), foundTeacher.getId());
        verify(teacherRepository, times(1)).findById(1L);
        verify(teacherMapper, times(1)).toDTO(teacher);
    }

    @Test
    void getTeacherById_NotFound_ThrowsException() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teacherService.getTeacherById(1L));
        verify(teacherRepository, times(1)).findById(1L);
        verify(teacherMapper, never()).toDTO(any(Teacher.class));
    }

    @Test
    void updateTeacher_FoundAndUpdated() {
        TeacherDTO updatedTeacherDTO = new TeacherDTO(1L, "Mr. Smithers", "Chemistry");
        Teacher updatedTeacherEntity = new Teacher(1L, "Mr. Smithers", "Chemistry");

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(updatedTeacherEntity);
        when(teacherMapper.toDTO(updatedTeacherEntity)).thenReturn(updatedTeacherDTO);

        TeacherDTO result = teacherService.updateTeacher(1L, updatedTeacherDTO);

        assertNotNull(result);
        assertEquals(updatedTeacherDTO.getName(), result.getName());
        assertEquals(updatedTeacherDTO.getSubject(), result.getSubject());
        verify(teacherRepository, times(1)).findById(1L);
        verify(teacherRepository, times(1)).save(any(Teacher.class));
        verify(teacherMapper, times(1)).toDTO(updatedTeacherEntity);
    }

    @Test
    void updateTeacher_NotFound_ThrowsException() {
        TeacherDTO updatedTeacherDTO = new TeacherDTO(1L, "Mr. Smithers", "Chemistry");
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teacherService.updateTeacher(1L, updatedTeacherDTO));
        verify(teacherRepository, times(1)).findById(1L);
        verify(teacherRepository, never()).save(any(Teacher.class));
        verify(teacherMapper, never()).toDTO(any(Teacher.class));
    }

    @Test
    void deleteTeacher_FoundAndDeleted() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        doNothing().when(teacherRepository).delete(teacher);

        teacherService.deleteTeacher(1L);

        verify(teacherRepository, times(1)).findById(1L);
        verify(teacherRepository, times(1)).delete(teacher);
    }

    @Test
    void deleteTeacher_NotFound_ThrowsException() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teacherService.deleteTeacher(1L));
        verify(teacherRepository, times(1)).findById(1L);
        verify(teacherRepository, never()).delete(any(Teacher.class));
    }
}
