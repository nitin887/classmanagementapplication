package com.classmanagementsystem.classmanagement.mapper;

import com.classmanagementsystem.classmanagement.dto.StudentDTO;
import com.classmanagementsystem.classmanagement.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentDTO toDTO(Student student) {
        if (student == null) {
            return null;
        }
        return new StudentDTO(
                student.getId(),
                student.getName(),
                student.getEmail()
        );
    }

    public Student toEntity(StudentDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Student(
                dto.getId(),
                dto.getName(),
                dto.getEmail()
        );
    }
}
