package com.classmanagementsystem.classmanagement.serviceinterface;

import com.classmanagementsystem.classmanagement.dto.ClassesDTO;

import java.util.List;

public interface ClassesServiceInterface {
    ClassesDTO createClasses(ClassesDTO classesDTO);
    List<ClassesDTO> getAllClasses();
    ClassesDTO getClassesById(Long id);
    ClassesDTO updateClasses(Long id, ClassesDTO classesDTO);
    void deleteClasses(Long id);
    ClassesDTO enrollStudentInClasses(Long classesId, Long studentId);
}
