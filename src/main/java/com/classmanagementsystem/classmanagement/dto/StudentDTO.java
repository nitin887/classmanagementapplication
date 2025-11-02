package com.classmanagementsystem.classmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class StudentDTO {
    private Long id;

    @NotBlank(message = "Student name cannot be empty")
    @Size(min = 2, max = 100, message = "Student name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Student email cannot be empty")
    @Email(message = "Student email must be a valid email address")
    private String email;

    // No-argument constructor
    public StudentDTO() {
    }

    // All-arguments constructor
    public StudentDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
