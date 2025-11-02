package com.classmanagementsystem.classmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class TeacherDTO {
    private Long id;

    @NotBlank(message = "Teacher name cannot be empty")
    @Size(min = 2, max = 100, message = "Teacher name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Teacher subject cannot be empty")
    @Size(min = 2, max = 50, message = "Teacher subject must be between 2 and 50 characters")
    private String subject;

    // No-argument constructor
    public TeacherDTO() {
    }

    // All-arguments constructor
    public TeacherDTO(Long id, String name, String subject) {
        this.id = id;
        this.name = name;
        this.subject = subject;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
