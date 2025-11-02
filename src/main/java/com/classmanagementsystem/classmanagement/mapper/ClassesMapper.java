package com.classmanagementsystem.classmanagement.mapper;

import com.classmanagementsystem.classmanagement.dto.ClassesDTO;
import com.classmanagementsystem.classmanagement.entity.Classes;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ClassesMapper {

    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;

    public ClassesMapper(StudentMapper studentMapper, TeacherMapper teacherMapper) {
        this.studentMapper = studentMapper;
        this.teacherMapper = teacherMapper;
    }

    public ClassesDTO toDTO(Classes classes) {
        if (classes == null) {
            return null;
        }
        ClassesDTO dto = new ClassesDTO();
        dto.setId(classes.getId());
        dto.setName(classes.getName());
        dto.setTeacher(teacherMapper.toDTO(classes.getTeacher()));
        dto.setStudents(classes.getStudents().stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toSet()));
        return dto;
    }

    public Classes toEntity(ClassesDTO dto) {
        if (dto == null) {
            return null;
        }
        Classes classes = new Classes();
        classes.setId(dto.getId());
        classes.setName(dto.getName());
        classes.setTeacher(teacherMapper.toEntity(dto.getTeacher()));
        // We intentionally don't map students from DTO to entity to keep creation simple.
        // Enrollment is handled by a specific service method.
        return classes;
    }
}
