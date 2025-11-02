package com.classmanagementsystem.classmanagement.service;

import com.classmanagementsystem.classmanagement.dto.RegisterDto;
import com.classmanagementsystem.classmanagement.dto.UserDTO;
import com.classmanagementsystem.classmanagement.entity.Role;
import com.classmanagementsystem.classmanagement.entity.User;
import com.classmanagementsystem.classmanagement.exception.ResourceNotFoundException;
import com.classmanagementsystem.classmanagement.mapper.UserMapper;
import com.classmanagementsystem.classmanagement.repository.RoleRepository;
import com.classmanagementsystem.classmanagement.repository.UserRepository;
import com.classmanagementsystem.classmanagement.serviceinterface.UserServiceInterface;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO registerUser(RegisterDto registerDto) {
        // Check if username is already taken
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        // Check if email is already taken
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }

        // Create new user object
        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // Assign a default role (e.g., "ROLE_USER")
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role newUserRole = new Role();
            newUserRole.setName("ROLE_USER");
            return roleRepository.save(newUserRole);
        });

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // Save the user to the database
        User savedUser = userRepository.save(user);

        return userMapper.toDTO(savedUser);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userMapper.toDTO(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // Update fields from DTO
        existingUser.setName(userDTO.getName());
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        // Password and roles are typically updated via separate methods for security reasons

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}
