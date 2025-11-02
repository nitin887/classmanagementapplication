package com.classmanagementsystem.classmanagement.repository;

import com.classmanagementsystem.classmanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Finds a student by their email address.
     * Spring Data JPA will automatically generate the query for this method based on its name.
     * @param email The email address of the student to search for.
     * @return An Optional containing the student if found, or an empty Optional if not.
     */
    Optional<Student> findByEmail(String email);
}
