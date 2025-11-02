package com.classmanagementsystem.classmanagement.repository;

import com.classmanagementsystem.classmanagement.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    /**
     * Finds all teachers with a given name.
     * Spring Data JPA will automatically generate the query for this method based on its name.
     * @param name The name of the teacher to search for.
     * @return A list of teachers with the matching name.
     */
    List<Teacher> findByName(String name);
}
