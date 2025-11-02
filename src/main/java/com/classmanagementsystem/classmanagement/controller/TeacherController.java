package com.classmanagementsystem.classmanagement.controller;

import com.classmanagementsystem.classmanagement.dto.TeacherDTO;
import com.classmanagementsystem.classmanagement.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody; // Corrected import
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@Tag(name = "Teacher Management", description = "APIs for managing teacher records")
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Operation(
            summary = "Create a new teacher",
            description = "Adds a new teacher record to the database."
    )
    @ApiResponse(responseCode = "201", description = "Teacher created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @PostMapping
    public ResponseEntity<TeacherDTO> createTeacher(
            @RequestBody(description = "Teacher object to be created", required = true) // Used RequestBody directly
            @Valid @org.springframework.web.bind.annotation.RequestBody TeacherDTO teacherDTO) {
        return new ResponseEntity<>(teacherService.createTeacher(teacherDTO), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all teachers",
            description = "Retrieves a list of all teacher records."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of teachers")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @GetMapping
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @Operation(
            summary = "Get teacher by ID",
            description = "Retrieves a single teacher record by its ID."
    )
    @ApiResponse(responseCode = "200", description = "Teacher found successfully")
    @ApiResponse(responseCode = "404", description = "Teacher not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(
            @Parameter(description = "ID of the teacher to retrieve", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @Operation(
            summary = "Update an existing teacher",
            description = "Updates an existing teacher record identified by ID."
    )
    @ApiResponse(responseCode = "200", description = "Teacher updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Teacher not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @PutMapping("/{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(
            @Parameter(description = "ID of the teacher to update", required = true)
            @PathVariable Long id,
            @RequestBody(description = "Updated teacher object", required = true) // Used RequestBody directly
            @Valid @org.springframework.web.bind.annotation.RequestBody TeacherDTO teacherDTO) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, teacherDTO));
    }

    @Operation(
            summary = "Delete a teacher by ID",
            description = "Deletes a teacher record from the database by its ID."
    )
    @ApiResponse(responseCode = "204", description = "Teacher deleted successfully")
    @ApiResponse(responseCode = "404", description = "Teacher not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(
            @Parameter(description = "ID of the teacher to delete", required = true)
            @PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
