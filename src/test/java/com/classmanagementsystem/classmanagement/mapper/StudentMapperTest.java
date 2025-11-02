package com.classmanagementsystem.classmanagement.mapper;

import com.classmanagementsystem.classmanagement.dto.StudentDTO;
import com.classmanagementsystem.classmanagement.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentMapperTest {

    private StudentMapper studentMapper;

    @BeforeEach
    void setUp() {
        studentMapper = new StudentMapper();
    }

    @Test
    void testToDTO() {
        // Given
        Student student = new Student(1L, "John Doe", "john.doe@example.com");

        // When
        StudentDTO dto = studentMapper.toDTO(student);

        // Then
        assertNotNull(dto);
        assertEquals(student.getId(), dto.getId());
        assertEquals(student.getName(), dto.getName());
        assertEquals(student.getEmail(), dto.getEmail());
    }

    @Test
    void testToDTO_NullInput() {
        // When
        StudentDTO dto = studentMapper.toDTO(null);

        // Then
        assertNull(dto);
    }

    @Test
    void testToEntity() {
        // Given
        StudentDTO dto = new StudentDTO(1L, "Jane Smith", "jane.smith@example.com");

        // When
        Student student = studentMapper.toEntity(dto);

        // Then
        assertNotNull(student);
        assertEquals(dto.getId(), student.getId());
        assertEquals(dto.getName(), student.getName());
        assertEquals(dto.getEmail(), student.getEmail());
    }

    @Test
    void testToEntity_NullInput() {
        // When
        Student student = studentMapper.toEntity(null);

        // Then
        assertNull(student);
    }
}
