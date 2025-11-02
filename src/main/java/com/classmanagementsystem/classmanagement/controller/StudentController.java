package com.classmanagementsystem.classmanagement.controller;

import com.classmanagementsystem.classmanagement.dto.StudentDTO;
import com.classmanagementsystem.classmanagement.service.StudentService;
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

@Tag(name = "Student Management", description = "APIs for managing student records")
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(
            summary = "Create a new student",
            description = "Adds a new student record to the database."
    )
    @ApiResponse(responseCode = "201", description = "Student created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(
            @RequestBody(description = "Student object to be created", required = true) // Used RequestBody directly
            @Valid @org.springframework.web.bind.annotation.RequestBody StudentDTO studentDTO) {
        return new ResponseEntity<>(studentService.createStudent(studentDTO), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all students",
            description = "Retrieves a list of all student records."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of students")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @Operation(
            summary = "Get student by ID",
            description = "Retrieves a single student record by its ID."
    )
    @ApiResponse(responseCode = "200", description = "Student found successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(
            @Parameter(description = "ID of the student to retrieve", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @Operation(
            summary = "Update an existing student",
            description = "Updates an existing student record identified by ID."
    )
    @ApiResponse(responseCode = "200", description = "Student updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Student not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
            @Parameter(description = "ID of the student to update", required = true)
            @PathVariable Long id,
            @RequestBody(description = "Updated student object", required = true) // Used RequestBody directly
            @Valid @org.springframework.web.bind.annotation.RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.updateStudent(id, studentDTO));
    }

    @Operation(
            summary = "Delete a student by ID",
            description = "Deletes a student record from the database by its ID."
    )
    @ApiResponse(responseCode = "204", description = "Student deleted successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient privileges")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "ID of the student to delete", required = true)
            @PathVariable Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
