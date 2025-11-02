package com.classmanagementsystem.classmanagement.repository;

import com.classmanagementsystem.classmanagement.entity.Classes;
import com.classmanagementsystem.classmanagement.entity.Student;
import com.classmanagementsystem.classmanagement.entity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ClassesRepositoryTest {

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Teacher teacher;
    private Student student1;
    private Student student2;
    private Classes class1;
    private Classes class2;

    @BeforeEach
    void setUp() {
        classesRepository.deleteAll();
        entityManager.clear();

        // Persist related entities first
        teacher = entityManager.persist(new Teacher(null, "Prof. Oak", "Biology"));
        student1 = entityManager.persist(new Student(null, "Ash Ketchum", "ash@pokemon.com"));
        student2 = entityManager.persist(new Student(null, "Misty", "misty@pokemon.com"));

        class1 = new Classes(null, "Pokemon Training 101", teacher, new HashSet<>());
        class2 = new Classes(null, "Advanced Battling", teacher, new HashSet<>());
    }

    @Test
    void testSaveClasses() {
        Classes savedClass = classesRepository.save(class1);
        assertNotNull(savedClass.getId());
        assertEquals("Pokemon Training 101", savedClass.getName());
        assertNotNull(savedClass.getTeacher());
        assertEquals(teacher.getId(), savedClass.getTeacher().getId());
    }

    @Test
    void testFindById() {
        entityManager.persist(class1);
        entityManager.flush();

        Optional<Classes> foundClass = classesRepository.findById(class1.getId());
        assertTrue(foundClass.isPresent());
        assertEquals("Pokemon Training 101", foundClass.get().getName());
    }

    @Test
    void testFindById_NotFound() {
        Optional<Classes> foundClass = classesRepository.findById(99L);
        assertFalse(foundClass.isPresent());
    }

    @Test
    void testFindAll() {
        entityManager.persist(class1);
        entityManager.persist(class2);
        entityManager.flush();

        List<Classes> classes = classesRepository.findAll();
        assertEquals(2, classes.size());
    }

    @Test
    void testDeleteById() {
        entityManager.persist(class1);
        entityManager.flush();

        classesRepository.deleteById(class1.getId());
        Optional<Classes> deletedClass = classesRepository.findById(class1.getId());
        assertFalse(deletedClass.isPresent());
    }

    @Test
    void testFindByName() {
        entityManager.persist(class1);
        // Removed the line that persists another class with the same name
        entityManager.flush();

        Optional<Classes> found = classesRepository.findByName("Pokemon Training 101");
        assertTrue(found.isPresent());
        assertEquals("Pokemon Training 101", found.get().getName());
    }

    @Test
    void testFindByName_NotFound() {
        Optional<Classes> found = classesRepository.findByName("NonExistentClass");
        assertFalse(found.isPresent());
    }

    @Test
    void testEnrollStudent() {
        // Given a class and a student
        Classes savedClass = classesRepository.save(class1);
        assertNotNull(savedClass.getId());

        // Enroll student1
        savedClass.getStudents().add(student1);
        Classes updatedClass = classesRepository.save(savedClass);

        // Verify enrollment
        Optional<Classes> retrievedClass = classesRepository.findById(updatedClass.getId());
        assertTrue(retrievedClass.isPresent());
        assertEquals(1, retrievedClass.get().getStudents().size());
        assertTrue(retrievedClass.get().getStudents().contains(student1));

        // Enroll student2
        retrievedClass.get().getStudents().add(student2);
        Classes finalClass = classesRepository.save(retrievedClass.get());

        // Verify second enrollment
        Optional<Classes> finalRetrievedClass = classesRepository.findById(finalClass.getId());
        assertTrue(finalRetrievedClass.isPresent());
        assertEquals(2, finalRetrievedClass.get().getStudents().size());
        assertTrue(finalRetrievedClass.get().getStudents().contains(student1));
        assertTrue(finalRetrievedClass.get().getStudents().contains(student2));
    }
}
