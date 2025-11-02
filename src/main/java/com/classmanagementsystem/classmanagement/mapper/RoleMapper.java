package com.classmanagementsystem.classmanagement.mapper;

import com.classmanagementsystem.classmanagement.dto.RoleDTO;
import com.classmanagementsystem.classmanagement.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleDTO toDTO(Role role) {
        if (role == null) {
            return null;
        }
        return new RoleDTO(
                role.getId(),
                role.getName()
        );
    }

    public Role toEntity(RoleDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Role(
                dto.getId(),
                dto.getName()
        );
    }
}
