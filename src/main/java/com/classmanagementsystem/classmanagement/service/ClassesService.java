package com.classmanagementsystem.classmanagement.service;

import com.classmanagementsystem.classmanagement.dto.ClassesDTO;
import com.classmanagementsystem.classmanagement.entity.Classes;
import com.classmanagementsystem.classmanagement.entity.Student;
import com.classmanagementsystem.classmanagement.exception.ResourceNotFoundException;
import com.classmanagementsystem.classmanagement.mapper.ClassesMapper;
import com.classmanagementsystem.classmanagement.repository.ClassesRepository;
import com.classmanagementsystem.classmanagement.repository.StudentRepository;
import com.classmanagementsystem.classmanagement.serviceinterface.ClassesServiceInterface;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassesService implements ClassesServiceInterface {

    private final ClassesRepository classesRepository;
    private final StudentRepository studentRepository;
    private final ClassesMapper classesMapper;

    public ClassesService(ClassesRepository classesRepository, StudentRepository studentRepository, ClassesMapper classesMapper) {
        this.classesRepository = classesRepository;
        this.studentRepository = studentRepository;
        this.classesMapper = classesMapper;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ClassesDTO createClasses(ClassesDTO classesDTO) {
        Classes newClasses = classesMapper.toEntity(classesDTO);
        Classes savedClasses = classesRepository.save(newClasses);
        return classesMapper.toDTO(savedClasses);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'USER')")
    public List<ClassesDTO> getAllClasses() {
        return classesRepository.findAll().stream()
                .map(classesMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'USER')")
    public ClassesDTO getClassesById(Long id) {
        Classes classes = classesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classes", "id", id));
        return classesMapper.toDTO(classes);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ClassesDTO updateClasses(Long id, ClassesDTO classesDTO) {
        Classes existingClasses = classesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classes", "id", id));

        existingClasses.setName(classesDTO.getName());
        // Note: Teacher and Students are handled via separate service methods (e.g., assign teacher, enroll student)
        // For simplicity, we are not updating nested objects directly via the updateClasses DTO for now.

        Classes updatedClasses = classesRepository.save(existingClasses);
        return classesMapper.toDTO(updatedClasses);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteClasses(Long id) {
        Classes classes = classesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classes", "id", id));
        classesRepository.delete(classes);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Transactional
    public ClassesDTO enrollStudentInClasses(Long classesId, Long studentId) {
        Classes existingClasses = classesRepository.findById(classesId)
                .orElseThrow(() -> new ResourceNotFoundException("Classes", "id", classesId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        existingClasses.getStudents().add(student);
        Classes savedClasses = classesRepository.save(existingClasses);
        return classesMapper.toDTO(savedClasses);
    }
}
