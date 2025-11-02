package com.classmanagementsystem.classmanagement.mapper;

import com.classmanagementsystem.classmanagement.dto.TeacherDTO;
import com.classmanagementsystem.classmanagement.entity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @BeforeEach
    void setUp() {
        teacherMapper = new TeacherMapper();
    }

    @Test
    void testToDTO() {
        // Given
        Teacher teacher = new Teacher(1L, "Jane Doe", "Math");

        // When
        TeacherDTO dto = teacherMapper.toDTO(teacher);

        // Then
        assertNotNull(dto);
        assertEquals(teacher.getId(), dto.getId());
        assertEquals(teacher.getName(), dto.getName());
        assertEquals(teacher.getSubject(), dto.getSubject());
    }

    @Test
    void testToDTO_NullInput() {
        // When
        TeacherDTO dto = teacherMapper.toDTO(null);

        // Then
        assertNull(dto);
    }

    @Test
    void testToEntity() {
        // Given
        TeacherDTO dto = new TeacherDTO(1L, "John Smith", "Physics");

        // When
        Teacher teacher = teacherMapper.toEntity(dto);

        // Then
        assertNotNull(teacher);
        assertEquals(dto.getId(), teacher.getId());
        assertEquals(dto.getName(), teacher.getName());
        assertEquals(dto.getSubject(), teacher.getSubject());
    }

    @Test
    void testToEntity_NullInput() {
        // When
        Teacher teacher = teacherMapper.toEntity(null);

        // Then
        assertNull(teacher);
    }
}
