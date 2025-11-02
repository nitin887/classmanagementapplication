package com.classmanagementsystem.classmanagement.mapper;

import com.classmanagementsystem.classmanagement.dto.UserDTO;
import com.classmanagementsystem.classmanagement.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream()
                .map(roleMapper::toDTO)
                .collect(Collectors.toSet()));
        return dto;
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        // We intentionally don't map roles or password from DTO to entity
        // to keep creation simple. This is handled by specific service logic.
        return user;
    }
}
