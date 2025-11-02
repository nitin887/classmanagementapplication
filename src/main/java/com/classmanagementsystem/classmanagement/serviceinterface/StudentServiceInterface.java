package com.classmanagementsystem.classmanagement.serviceinterface;

import com.classmanagementsystem.classmanagement.dto.StudentDTO;

import java.util.List;

public interface StudentServiceInterface {
    StudentDTO createStudent(StudentDTO studentDTO);
    List<StudentDTO> getAllStudents();
    StudentDTO getStudentById(Long id);
    StudentDTO updateStudent(Long id, StudentDTO studentDTO);
    void deleteStudent(Long id);
}
