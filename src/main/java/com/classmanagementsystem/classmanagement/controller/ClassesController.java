package com.classmanagementsystem.classmanagement.controller;

import com.classmanagementsystem.classmanagement.dto.ClassesDTO;
import com.classmanagementsystem.classmanagement.service.ClassesService;
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

@Tag(name = "Classes Management", description = "APIs for managing class records and student enrollment")
@RestController
@RequestMapping("/api/classes")
public class ClassesController {

    private final ClassesService classesService;

    public ClassesController(ClassesService classesService) {
        this.classesService = classesService;
    }

    @Operation(
            summary = "Create a new class",
            description = "Adds a new class record to the database. A teacher can be assigned during creation."
    )
    @ApiResponse(responseCode = "201", description = "Class created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @PostMapping
    public ResponseEntity<ClassesDTO> createClasses(
            @RequestBody(description = "Class object to be created", required = true) // Used RequestBody directly
            @Valid @org.springframework.web.bind.annotation.RequestBody ClassesDTO classesDTO) {
        return new ResponseEntity<>(classesService.createClasses(classesDTO), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all classes",
            description = "Retrieves a list of all class records."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of classes")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @GetMapping
    public ResponseEntity<List<ClassesDTO>> getAllClasses() {
        return ResponseEntity.ok(classesService.getAllClasses());
    }

    @Operation(
            summary = "Get class by ID",
            description = "Retrieves a single class record by its ID."
    )
    @ApiResponse(responseCode = "200", description = "Class found successfully")
    @ApiResponse(responseCode = "404", description = "Class not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @GetMapping("/{id}")
    public ResponseEntity<ClassesDTO> getClassesById(
            @Parameter(description = "ID of the class to retrieve", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(classesService.getClassesById(id));
    }

    @Operation(
            summary = "Update an existing class",
            description = "Updates an existing class record identified by ID."
    )
    @ApiResponse(responseCode = "200", description = "Class updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Class not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @PutMapping("/{id}")
    public ResponseEntity<ClassesDTO> updateClasses(
            @Parameter(description = "ID of the class to update", required = true)
            @PathVariable Long id,
            @RequestBody(description = "Updated class object", required = true) // Used RequestBody directly
            @Valid @org.springframework.web.bind.annotation.RequestBody ClassesDTO classesDTO) {
        return ResponseEntity.ok(classesService.updateClasses(id, classesDTO));
    }

    @Operation(
            summary = "Delete a class by ID",
            description = "Deletes a class record from the database by its ID."
    )
    @ApiResponse(responseCode = "204", description = "Class deleted successfully")
    @ApiResponse(responseCode = "404", description = "Class not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClasses(
            @Parameter(description = "ID of the class to delete", required = true)
            @PathVariable Long id) {
        classesService.deleteClasses(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Enroll a student in a class",
            description = "Enrolls a student into a specific class. Both class and student must exist."
    )
    @ApiResponse(responseCode = "200", description = "Student enrolled successfully")
    @ApiResponse(responseCode = "404", description = "Class or Student not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @PutMapping("/classes/{classesId}/students/{studentId}")
    public ResponseEntity<ClassesDTO> enrollStudentInClasses(
            @Parameter(description = "ID of the class to enroll student in", required = true)
            @PathVariable Long classesId,
            @Parameter(description = "ID of the student to enroll", required = true)
            @PathVariable Long studentId) {
        return ResponseEntity.ok(classesService.enrollStudentInClasses(classesId, studentId));
    }
}
