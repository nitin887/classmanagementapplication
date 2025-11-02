package com.classmanagementsystem.classmanagement.serviceinterface;

import com.classmanagementsystem.classmanagement.dto.TeacherDTO;

import java.util.List;

public interface TeacherServiceInterface {
    TeacherDTO createTeacher(TeacherDTO teacherDTO);
    List<TeacherDTO> getAllTeachers();
    TeacherDTO getTeacherById(Long id);
    TeacherDTO updateTeacher(Long id, TeacherDTO teacherDTO);
    void deleteTeacher(Long id);
}
