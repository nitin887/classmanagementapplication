package com.classmanagementsystem.classmanagement.service;

import com.classmanagementsystem.classmanagement.dto.TeacherDTO;
import com.classmanagementsystem.classmanagement.entity.Teacher;
import com.classmanagementsystem.classmanagement.exception.ResourceNotFoundException;
import com.classmanagementsystem.classmanagement.mapper.TeacherMapper;
import com.classmanagementsystem.classmanagement.repository.TeacherRepository;
import com.classmanagementsystem.classmanagement.serviceinterface.TeacherServiceInterface;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService implements TeacherServiceInterface {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    public TeacherService(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = teacherMapper.toEntity(teacherDTO);
        Teacher savedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toDTO(savedTeacher);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public TeacherDTO getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
        return teacherMapper.toDTO(teacher);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public TeacherDTO updateTeacher(Long id, TeacherDTO teacherDTO) {
        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));

        existingTeacher.setName(teacherDTO.getName());
        existingTeacher.setSubject(teacherDTO.getSubject());

        Teacher updatedTeacher = teacherRepository.save(existingTeacher);
        return teacherMapper.toDTO(updatedTeacher);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
        teacherRepository.delete(teacher);
    }
}
