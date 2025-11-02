package com.classmanagementsystem.classmanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Setter
public class ClassesDTO {
    private Long id;
    private String name;
    private TeacherDTO teacher;
    private Set<StudentDTO> students;

    // No-argument constructor
    public ClassesDTO() {
    }

    // All-arguments constructor
    public ClassesDTO(Long id, String name, TeacherDTO teacher, Set<StudentDTO> students) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.students = students;
    }
    public Long getId() {
        return id;
  }

    public void setId(Long id) {
       this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeacherDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherDTO teacher) {
        this.teacher = teacher;
    }

    public Set<StudentDTO> getStudents() {
        return students;
    }

    public void setStudents(Set<StudentDTO> students) {
        this.students = students; }
    
}
