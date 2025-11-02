package com.classmanagementsystem.classmanagement.service;

import com.classmanagementsystem.classmanagement.dto.ClassesDTO;
import com.classmanagementsystem.classmanagement.dto.StudentDTO;
import com.classmanagementsystem.classmanagement.dto.TeacherDTO;
import com.classmanagementsystem.classmanagement.entity.Classes;
import com.classmanagementsystem.classmanagement.entity.Student;
import com.classmanagementsystem.classmanagement.entity.Teacher;
import com.classmanagementsystem.classmanagement.exception.ResourceNotFoundException;
import com.classmanagementsystem.classmanagement.mapper.ClassesMapper;
import com.classmanagementsystem.classmanagement.repository.ClassesRepository;
import com.classmanagementsystem.classmanagement.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClassesServiceTest {

    @Mock
    private ClassesRepository classesRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ClassesMapper classesMapper;

    @InjectMocks
    private ClassesService classesService;

    private Classes classes;
    private ClassesDTO classesDTO;
    private Student student;
    private StudentDTO studentDTO;
    private Teacher teacher;
    private TeacherDTO teacherDTO;

    @BeforeEach
    void setUp() {
        teacher = new Teacher(1L, "Prof. X", "Mutant Studies");
        teacherDTO = new TeacherDTO(1L, "Prof. X", "Mutant Studies");

        student = new Student(10L, "Jean Grey", "jean@xmen.com");
        studentDTO = new StudentDTO(10L, "Jean Grey", "jean@xmen.com");

        Set<Student> students = new HashSet<>();
        students.add(student);

        classes = new Classes(100L, "Telekinesis 101", teacher, students);
        classesDTO = new ClassesDTO(100L, "Telekinesis 101", teacherDTO, new HashSet<>(Arrays.asList(studentDTO)));
    }

    @Test
    void createClasses_Success() {
        when(classesMapper.toEntity(classesDTO)).thenReturn(classes);
        when(classesRepository.save(classes)).thenReturn(classes);
        when(classesMapper.toDTO(classes)).thenReturn(classesDTO);

        ClassesDTO createdClasses = classesService.createClasses(classesDTO);

        assertNotNull(createdClasses);
        assertEquals(classesDTO.getName(), createdClasses.getName());
        verify(classesRepository, times(1)).save(classes);
        verify(classesMapper, times(1)).toEntity(classesDTO);
        verify(classesMapper, times(1)).toDTO(classes);
    }

    @Test
    void getAllClasses_ReturnsListOfClasses() {
        List<Classes> classesList = Arrays.asList(classes, new Classes(101L, "Combat Training", teacher, new HashSet<>()));
        List<ClassesDTO> classesDTOList = Arrays.asList(classesDTO, new ClassesDTO(101L, "Combat Training", teacherDTO, new HashSet<>()));

        when(classesRepository.findAll()).thenReturn(classesList);
        when(classesMapper.toDTO(any(Classes.class))).thenAnswer(invocation -> {
            Classes c = invocation.getArgument(0);
            // Simplified mapping for test, real mapper handles nested objects
            return new ClassesDTO(c.getId(), c.getName(), null, null);
        });

        List<ClassesDTO> result = classesService.getAllClasses();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(classesRepository, times(1)).findAll();
        verify(classesMapper, times(2)).toDTO(any(Classes.class));
    }

    @Test
    void getClassesById_Found() {
        when(classesRepository.findById(100L)).thenReturn(Optional.of(classes));
        when(classesMapper.toDTO(classes)).thenReturn(classesDTO);

        ClassesDTO foundClasses = classesService.getClassesById(100L);

        assertNotNull(foundClasses);
        assertEquals(classesDTO.getId(), foundClasses.getId());
        verify(classesRepository, times(1)).findById(100L);
        verify(classesMapper, times(1)).toDTO(classes);
    }

    @Test
    void getClassesById_NotFound_ThrowsException() {
        when(classesRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> classesService.getClassesById(100L));
        verify(classesRepository, times(1)).findById(100L);
        verify(classesMapper, never()).toDTO(any(Classes.class));
    }

    @Test
    void updateClasses_FoundAndUpdated() {
        ClassesDTO updatedClassesDTO = new ClassesDTO(100L, "Advanced Telekinesis", teacherDTO, new HashSet<>());
        Classes updatedClassesEntity = new Classes(100L, "Advanced Telekinesis", teacher, new HashSet<>());

        when(classesRepository.findById(100L)).thenReturn(Optional.of(classes));
        when(classesRepository.save(any(Classes.class))).thenReturn(updatedClassesEntity);
        when(classesMapper.toDTO(updatedClassesEntity)).thenReturn(updatedClassesDTO);

        ClassesDTO result = classesService.updateClasses(100L, updatedClassesDTO);

        assertNotNull(result);
        assertEquals(updatedClassesDTO.getName(), result.getName());
        verify(classesRepository, times(1)).findById(100L);
        verify(classesRepository, times(1)).save(any(Classes.class));
        verify(classesMapper, times(1)).toDTO(updatedClassesEntity);
    }

    @Test
    void updateClasses_NotFound_ThrowsException() {
        ClassesDTO updatedClassesDTO = new ClassesDTO(100L, "Advanced Telekinesis", teacherDTO, new HashSet<>());
        when(classesRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> classesService.updateClasses(100L, updatedClassesDTO));
        verify(classesRepository, times(1)).findById(100L);
        verify(classesRepository, never()).save(any(Classes.class));
        verify(classesMapper, never()).toDTO(any(Classes.class));
    }

    @Test
    void deleteClasses_FoundAndDeleted() {
        when(classesRepository.findById(100L)).thenReturn(Optional.of(classes));
        doNothing().when(classesRepository).delete(classes);

        classesService.deleteClasses(100L);

        verify(classesRepository, times(1)).findById(100L);
        verify(classesRepository, times(1)).delete(classes);
    }

    @Test
    void deleteClasses_NotFound_ThrowsException() {
        when(classesRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> classesService.deleteClasses(100L));
        verify(classesRepository, times(1)).findById(100L);
        verify(classesRepository, never()).delete(any(Classes.class));
    }

    @Test
    void enrollStudentInClasses_Success() {
        Classes classesBeforeEnroll = new Classes(100L, "Telekinesis 101", teacher, new HashSet<>());
        Classes classesAfterEnroll = new Classes(100L, "Telekinesis 101", teacher, new HashSet<>(Arrays.asList(student)));

        when(classesRepository.findById(100L)).thenReturn(Optional.of(classesBeforeEnroll));
        when(studentRepository.findById(10L)).thenReturn(Optional.of(student));
        when(classesRepository.save(any(Classes.class))).thenReturn(classesAfterEnroll);
        when(classesMapper.toDTO(classesAfterEnroll)).thenReturn(classesDTO);

        ClassesDTO result = classesService.enrollStudentInClasses(100L, 10L);

        assertNotNull(result);
        assertEquals(1, result.getStudents().size());
        verify(classesRepository, times(1)).findById(100L);
        verify(studentRepository, times(1)).findById(10L);
        verify(classesRepository, times(1)).save(any(Classes.class));
        verify(classesMapper, times(1)).toDTO(classesAfterEnroll);
    }

    @Test
    void enrollStudentInClasses_ClassesNotFound_ThrowsException() {
        when(classesRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> classesService.enrollStudentInClasses(100L, 10L));
        verify(classesRepository, times(1)).findById(100L);
        verify(studentRepository, never()).findById(anyLong());
    }

    @Test
    void enrollStudentInClasses_StudentNotFound_ThrowsException() {
        when(classesRepository.findById(100L)).thenReturn(Optional.of(classes));
        when(studentRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> classesService.enrollStudentInClasses(100L, 10L));
        verify(classesRepository, times(1)).findById(100L);
        verify(studentRepository, times(1)).findById(10L);
        verify(classesRepository, never()).save(any(Classes.class));
    }
}
