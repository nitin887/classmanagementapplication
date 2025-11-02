package com.classmanagementsystem.classmanagement.repository;

import com.classmanagementsystem.classmanagement.entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassesRepository extends JpaRepository<Classes, Long> {

    /**
     * Finds a class by its name.
     * Spring Data JPA will automatically generate the query for this method based on its name.
     * @param name The name of the class to search for.
     * @return An Optional containing the class if found, or an empty Optional if not.
     */
    Optional<Classes> findByName(String name);
}
