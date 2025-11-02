package com.classmanagementsystem.classmanagement.mapper;

import com.classmanagementsystem.classmanagement.dto.RoleDTO;
import com.classmanagementsystem.classmanagement.dto.UserDTO;
import com.classmanagementsystem.classmanagement.entity.Role;
import com.classmanagementsystem.classmanagement.entity.User;
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

public class UserMapperTest {

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToDTO() {
        // Given
        Role roleUser = new Role(1L, "ROLE_USER");
        Role roleAdmin = new Role(2L, "ROLE_ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);
        roles.add(roleAdmin);

        User user = new User(1L, "John Doe", "johndoe", "john.doe@example.com", "hashedpassword", roles);

        // Mock nested mapper
        RoleDTO roleUserDTO = new RoleDTO(1L, "ROLE_USER");
        RoleDTO roleAdminDTO = new RoleDTO(2L, "ROLE_ADMIN");

        when(roleMapper.toDTO(roleUser)).thenReturn(roleUserDTO);
        when(roleMapper.toDTO(roleAdmin)).thenReturn(roleAdminDTO);

        // When
        UserDTO dto = userMapper.toDTO(user);

        // Then
        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getEmail(), dto.getEmail());
        assertNotNull(dto.getRoles());
        assertEquals(2, dto.getRoles().size());
        assertTrue(dto.getRoles().contains(roleUserDTO));
        assertTrue(dto.getRoles().contains(roleAdminDTO));
    }

    @Test
    void testToDTO_NullRoles() {
        // Given
        User user = new User(1L, "John Doe", "johndoe", "john.doe@example.com", "hashedpassword", null);

        // When
        UserDTO dto = userMapper.toDTO(user);

        // Then
        assertNotNull(dto);
        assertNull(dto.getRoles());
    }

    @Test
    void testToDTO_EmptyRoles() {
        // Given
        User user = new User(1L, "John Doe", "johndoe", "john.doe@example.com", "hashedpassword", new HashSet<>());

        // When
        UserDTO dto = userMapper.toDTO(user);

        // Then
        assertNotNull(dto);
        assertNotNull(dto.getRoles());
        assertTrue(dto.getRoles().isEmpty());
    }

    @Test
    void testToDTO_NullInput() {
        // When
        UserDTO dto = userMapper.toDTO(null);

        // Then
        assertNull(dto);
    }

    @Test
    void testToEntity() {
        // Given
        RoleDTO roleUserDTO = new RoleDTO(1L, "ROLE_USER");
        Set<RoleDTO> roleDTOs = new HashSet<>();
        roleDTOs.add(roleUserDTO);

        UserDTO dto = new UserDTO(1L, "Jane Smith", "janesmith", "jane.smith@example.com", roleDTOs);

        // Mock nested mapper
        Role roleUser = new Role(1L, "ROLE_USER");
        when(roleMapper.toEntity(roleUserDTO)).thenReturn(roleUser);

        // When
        User user = userMapper.toEntity(dto);

        // Then
        assertNotNull(user);
        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getUsername(), user.getUsername());
        assertEquals(dto.getEmail(), user.getEmail());
        // Roles are not mapped back from DTO to entity in this mapper (as per design)
        assertNull(user.getRoles()); // Should be null or empty depending on entity constructor
    }

    @Test
    void testToEntity_NullRoles() {
        // Given
        UserDTO dto = new UserDTO(1L, "Jane Smith", "janesmith", "jane.smith@example.com", null);

        // When
        User user = userMapper.toEntity(dto);

        // Then
        assertNotNull(user);
        assertNull(user.getRoles());
    }

    @Test
    void testToEntity_NullInput() {
        // When
        User user = userMapper.toEntity(null);

        // Then
        assertNull(user);
    }
}
