package com.classmanagementsystem.classmanagement.mapper;

import com.classmanagementsystem.classmanagement.dto.RoleDTO;
import com.classmanagementsystem.classmanagement.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleMapperTest {

    private RoleMapper roleMapper;

    @BeforeEach
    void setUp() {
        roleMapper = new RoleMapper();
    }

    @Test
    void testToDTO() {
        // Given
        Role role = new Role(1L, "ROLE_USER");

        // When
        RoleDTO dto = roleMapper.toDTO(role);

        // Then
        assertNotNull(dto);
        assertEquals(role.getId(), dto.getId());
        assertEquals(role.getName(), dto.getName());
    }

    @Test
    void testToDTO_NullInput() {
        // When
        RoleDTO dto = roleMapper.toDTO(null);

        // Then
        assertNull(dto);
    }

    @Test
    void testToEntity() {
        // Given
        RoleDTO dto = new RoleDTO(2L, "ROLE_ADMIN");

        // When
        Role role = roleMapper.toEntity(dto);

        // Then
        assertNotNull(role);
        assertEquals(dto.getId(), role.getId());
        assertEquals(dto.getName(), role.getName());
    }

    @Test
    void testToEntity_NullInput() {
        // When
        Role role = roleMapper.toEntity(null);

        // Then
        assertNull(role);
    }
}
