package com.classmanagementsystem.classmanagement.mapper;

import com.classmanagementsystem.classmanagement.dto.ClassesDTO;
import com.classmanagementsystem.classmanagement.dto.StudentDTO;
import com.classmanagementsystem.classmanagement.dto.TeacherDTO;
import com.classmanagementsystem.classmanagement.entity.Classes;
import com.classmanagementsystem.classmanagement.entity.Student;
import com.classmanagementsystem.classmanagement.entity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ClassesMapperTest {

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private ClassesMapper classesMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToDTO() {
        // Given
        Teacher teacher = new Teacher(1L, "Dr. Who", "Time Travel");
        Student student1 = new Student(10L, "Rose Tyler", "rose@example.com");
        Student student2 = new Student(11L, "Martha Jones", "martha@example.com");

        Set<Student> students = new HashSet<>();
        students.add(student1);
        students.add(student2);

        Classes classes = new Classes(100L, "Advanced Chronology", teacher, students);

        // Mock nested mappers
        TeacherDTO teacherDTO = new TeacherDTO(1L, "Dr. Who", "Time Travel");
        StudentDTO studentDTO1 = new StudentDTO(10L, "Rose Tyler", "rose@example.com");
        StudentDTO studentDTO2 = new StudentDTO(11L, "Martha Jones", "martha@example.com");

        when(teacherMapper.toDTO(teacher)).thenReturn(teacherDTO);
        when(studentMapper.toDTO(student1)).thenReturn(studentDTO1);
        when(studentMapper.toDTO(student2)).thenReturn(studentDTO2);

        // When
        ClassesDTO dto = classesMapper.toDTO(classes);

        // Then
        assertNotNull(dto);
        assertEquals(classes.getId(), dto.getId());
        assertEquals(classes.getName(), dto.getName());
        assertEquals(teacherDTO, dto.getTeacher());
        assertNotNull(dto.getStudents());
        assertEquals(2, dto.getStudents().size());
        assertTrue(dto.getStudents().contains(studentDTO1));
        assertTrue(dto.getStudents().contains(studentDTO2));
    }

    @Test
    void testToDTO_NullTeacherAndStudents() {
        // Given
        Classes classes = new Classes(101L, "Basic Spacetime", null, new HashSet<>());

        // When
        ClassesDTO dto = classesMapper.toDTO(classes);

        // Then
        assertNotNull(dto);
        assertEquals(classes.getId(), dto.getId());
        assertEquals(classes.getName(), dto.getName());
        assertNull(dto.getTeacher());
        assertNotNull(dto.getStudents());
        assertTrue(dto.getStudents().isEmpty());
    }

    @Test
    void testToDTO_NullInput() {
        // When
        ClassesDTO dto = classesMapper.toDTO(null);

        // Then
        assertNull(dto);
    }

    @Test
    void testToEntity() {
        // Given
        TeacherDTO teacherDTO = new TeacherDTO(1L, "Dr. Who", "Time Travel");
        ClassesDTO dto = new ClassesDTO(100L, "Advanced Chronology", teacherDTO, Collections.emptySet());

        // Mock nested mappers
        Teacher teacher = new Teacher(1L, "Dr. Who", "Time Travel");
        when(teacherMapper.toEntity(teacherDTO)).thenReturn(teacher);

        // When
        Classes classes = classesMapper.toEntity(dto);

        // Then
        assertNotNull(classes);
        assertEquals(dto.getId(), classes.getId());
        assertEquals(dto.getName(), classes.getName());
        assertEquals(teacher, classes.getTeacher());
        // Students are not mapped back from DTO to entity in this mapper
        assertNotNull(classes.getStudents());
        assertTrue(classes.getStudents().isEmpty());
    }

    @Test
    void testToEntity_NullTeacher() {
        // Given
        ClassesDTO dto = new ClassesDTO(101L, "Basic Spacetime", null, Collections.emptySet());

        // When
        Classes classes = classesMapper.toEntity(dto);

        // Then
        assertNotNull(classes);
        assertEquals(dto.getId(), classes.getId());
        assertEquals(dto.getName(), classes.getName());
        assertNull(classes.getTeacher());
    }

    @Test
    void testToEntity_NullInput() {
        // When
        Classes classes = classesMapper.toEntity(null);

        // Then
        assertNull(classes);
    }
}
