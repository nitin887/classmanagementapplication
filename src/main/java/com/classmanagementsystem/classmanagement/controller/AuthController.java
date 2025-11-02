package com.classmanagementsystem.classmanagement.controller;

import com.classmanagementsystem.classmanagement.dto.LoginDto;
import com.classmanagementsystem.classmanagement.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
// Removed: import io.swagger.v3.oas.annotations.parameters.RequestBody; // This caused ambiguity

@Tag(name = "Authentication", description = "APIs for user authentication (login)")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "User login",
            description = "Authenticates a user with username/email and password. Returns a success message upon successful login."
    )
    @ApiResponse(responseCode = "200", description = "User logged in successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid credentials")
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<String> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User login credentials", required = true) // Used fully qualified name
            @org.springframework.web.bind.annotation.RequestBody LoginDto loginDto) {
        String response = authService.login(loginDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
