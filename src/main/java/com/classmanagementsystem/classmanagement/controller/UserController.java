package com.classmanagementsystem.classmanagement.controller;

import com.classmanagementsystem.classmanagement.dto.RegisterDto;
import com.classmanagementsystem.classmanagement.dto.UserDTO;
import com.classmanagementsystem.classmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
// Removed: import io.swagger.v3.oas.annotations.parameters.RequestBody; // This caused ambiguity
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@Tag(name = "User Management", description = "APIs for managing user records")
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user with a default 'ROLE_USER'."
    )
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data or user already exists")
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User registration details", required = true) // Used fully qualified name
            @Valid @org.springframework.web.bind.annotation.RequestBody RegisterDto registerDto) {
        UserDTO savedUser = userService.registerUser(registerDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a single user record by its ID."
    )
    @ApiResponse(responseCode = "200", description = "User found successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of all user records. Only accessible by ADMIN."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(
            summary = "Update an existing user",
            description = "Updates an existing user record identified by ID. Only accessible by ADMIN."
    )
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated user object", required = true) // Used fully qualified name
            @Valid @org.springframework.web.bind.annotation.RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @Operation(
            summary = "Delete a user by ID",
            description = "Deletes a user record from the database by its ID. Only accessible by ADMIN."
    )
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
