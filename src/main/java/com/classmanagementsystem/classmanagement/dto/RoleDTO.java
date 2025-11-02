package com.classmanagementsystem.classmanagement.dto;

public class RoleDTO {
    private Long id;
    private String name;

    // No-argument constructor
    public RoleDTO() {
    }

    // All-arguments constructor
    public RoleDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
