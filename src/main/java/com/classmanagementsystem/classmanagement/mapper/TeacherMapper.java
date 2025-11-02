package com.classmanagementsystem.classmanagement.mapper;

import com.classmanagementsystem.classmanagement.dto.TeacherDTO;
import com.classmanagementsystem.classmanagement.entity.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    public TeacherDTO toDTO(Teacher teacher) {
        if (teacher == null) {
            return null;
        }
        return new TeacherDTO(
                teacher.getId(),
                teacher.getName(),
                teacher.getSubject()
        );
    }

    public Teacher toEntity(TeacherDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Teacher(
                dto.getId(),
                dto.getName(),
                dto.getSubject()
        );
    }
}
