package com.classmanagementsystem.classmanagement.dto;

import java.util.Set;

public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private Set<RoleDTO> roles;

    // No-argument constructor
    public UserDTO() {
    }

    // All-arguments constructor
    public UserDTO(Long id, String name, String username, String email, Set<RoleDTO> roles) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.roles = roles;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }
}
