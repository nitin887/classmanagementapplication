package com.classmanagementsystem.classmanagement.serviceinterface;

import com.classmanagementsystem.classmanagement.dto.RegisterDto;
import com.classmanagementsystem.classmanagement.dto.UserDTO;

import java.util.List;

public interface UserServiceInterface {
    UserDTO registerUser(RegisterDto registerDto);
    UserDTO getUserById(Long id);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    List<UserDTO> getAllUsers(); // Adding this for completeness, though not explicitly requested yet
}
