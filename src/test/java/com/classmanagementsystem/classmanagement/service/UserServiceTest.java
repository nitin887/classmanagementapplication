package com.classmanagementsystem.classmanagement.service;

import com.classmanagementsystem.classmanagement.dto.RegisterDto;
import com.classmanagementsystem.classmanagement.dto.RoleDTO;
import com.classmanagementsystem.classmanagement.dto.UserDTO;
import com.classmanagementsystem.classmanagement.entity.Role;
import com.classmanagementsystem.classmanagement.entity.User;
import com.classmanagementsystem.classmanagement.exception.ResourceNotFoundException;
import com.classmanagementsystem.classmanagement.mapper.UserMapper;
import com.classmanagementsystem.classmanagement.repository.RoleRepository;
import com.classmanagementsystem.classmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;
    private RegisterDto registerDto;
    private Role roleUser;
    private RoleDTO roleUserDTO;

    @BeforeEach
    void setUp() {
        roleUser = new Role(1L, "ROLE_USER");
        roleUserDTO = new RoleDTO(1L, "ROLE_USER");

        user = new User(1L, "John Doe", "johndoe", "john.doe@example.com", "encodedpassword", new HashSet<>(Arrays.asList(roleUser)));
        userDTO = new UserDTO(1L, "John Doe", "johndoe", "john.doe@example.com", new HashSet<>(Arrays.asList(roleUserDTO)));

        registerDto = new RegisterDto("Jane Doe", "janedoe", "jane.doe@example.com", "rawpassword");
    }

    @Test
    void registerUser_Success() {
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedpassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.registerUser(registerDto);

        assertNotNull(result);
        assertEquals(userDTO.getUsername(), result.getUsername());
        verify(userRepository, times(1)).existsByUsername(registerDto.getUsername());
        verify(userRepository, times(1)).existsByEmail(registerDto.getEmail());
        verify(passwordEncoder, times(1)).encode(registerDto.getPassword());
        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toDTO(user);
    }

    @Test
    void registerUser_UsernameAlreadyExists_ThrowsException() {
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.registerUser(registerDto));
        verify(userRepository, times(1)).existsByUsername(registerDto.getUsername());
        verify(userRepository, never()).existsByEmail(anyString());
    }

    @Test
    void registerUser_EmailAlreadyExists_ThrowsException() {
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.registerUser(registerDto));
        verify(userRepository, times(1)).existsByUsername(registerDto.getUsername());
        verify(userRepository, times(1)).existsByEmail(registerDto.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void registerUser_RoleNotFoundAndCreated() {
        Role newRole = new Role(2L, "ROLE_USER");
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedpassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(newRole);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.registerUser(registerDto);

        assertNotNull(result);
        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void getUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(userDTO.getId(), foundUser.getId());
        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toDTO(user);
    }

    @Test
    void getUserById_NotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, never()).toDTO(any(User.class));
    }

    @Test
    void updateUser_FoundAndUpdated() {
        UserDTO updatedUserDTO = new UserDTO(1L, "Jane Doe", "janedoe", "jane.doe@example.com", new HashSet<>(Arrays.asList(roleUserDTO)));
        User updatedUserEntity = new User(1L, "Jane Doe", "janedoe", "jane.doe@example.com", "encodedpassword", new HashSet<>(Arrays.asList(roleUser)));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUserEntity);
        when(userMapper.toDTO(updatedUserEntity)).thenReturn(updatedUserDTO);

        UserDTO result = userService.updateUser(1L, updatedUserDTO);

        assertNotNull(result);
        assertEquals(updatedUserDTO.getName(), result.getName());
        assertEquals(updatedUserDTO.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toDTO(updatedUserEntity);
    }

    @Test
    void updateUser_NotFound_ThrowsException() {
        UserDTO updatedUserDTO = new UserDTO(1L, "Jane Doe", "janedoe", "jane.doe@example.com", new HashSet<>(Arrays.asList(roleUserDTO)));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, updatedUserDTO));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
        verify(userMapper, never()).toDTO(any(User.class));
    }

    @Test
    void deleteUser_FoundAndDeleted() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        List<User> users = Arrays.asList(user, new User(2L, "Jane Smith", "janesmith", "jane.smith@example.com", "encodedpassword2", new HashSet<>(Arrays.asList(roleUser))));
        List<UserDTO> userDTOs = Arrays.asList(userDTO, new UserDTO(2L, "Jane Smith", "janesmith", "jane.smith@example.com", new HashSet<>(Arrays.asList(roleUserDTO))));

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDTO(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            return new UserDTO(u.getId(), u.getName(), u.getUsername(), u.getEmail(), new HashSet<>(Arrays.asList(roleUserDTO)));
        });

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(2)).toDTO(any(User.class));
    }
}
