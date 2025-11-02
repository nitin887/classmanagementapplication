package com.classmanagementsystem.classmanagement.service;

import com.classmanagementsystem.classmanagement.dto.StudentDTO;
import com.classmanagementsystem.classmanagement.entity.Student;
import com.classmanagementsystem.classmanagement.exception.ResourceNotFoundException;
import com.classmanagementsystem.classmanagement.mapper.StudentMapper;
import com.classmanagementsystem.classmanagement.repository.StudentRepository;
import com.classmanagementsystem.classmanagement.serviceinterface.StudentServiceInterface;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService implements StudentServiceInterface {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = studentMapper.toEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDTO(savedStudent);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        return studentMapper.toDTO(student);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        existingStudent.setName(studentDTO.getName());
        existingStudent.setEmail(studentDTO.getEmail());

        Student updatedStudent = studentRepository.save(existingStudent);
        return studentMapper.toDTO(updatedStudent);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        studentRepository.delete(student);
    }
}
