package com.classmanagementsystem.classmanagement.repository;

import com.classmanagementsystem.classmanagement.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestEntityManager entityManager; // Used to persist entities directly for testing

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        studentRepository.deleteAll();
        entityManager.clear();

        student1 = new Student(null, "Alice", "alice@example.com");
        student2 = new Student(null, "Bob", "bob@example.com");
    }

    @Test
    void testSaveStudent() {
        Student savedStudent = studentRepository.save(student1);
        assertNotNull(savedStudent.getId());
        assertEquals("Alice", savedStudent.getName());
    }

    @Test
    void testFindById() {
        entityManager.persist(student1);
        entityManager.flush();

        Optional<Student> foundStudent = studentRepository.findById(student1.getId());
        assertTrue(foundStudent.isPresent());
        assertEquals("Alice", foundStudent.get().getName());
    }

    @Test
    void testFindById_NotFound() {
        Optional<Student> foundStudent = studentRepository.findById(99L);
        assertFalse(foundStudent.isPresent());
    }

    @Test
    void testFindAll() {
        entityManager.persist(student1);
        entityManager.persist(student2);
        entityManager.flush();

        Iterable<Student> students = studentRepository.findAll();
        long count = students.spliterator().getExactSizeIfKnown();
        assertEquals(2, count);
    }

    @Test
    void testDeleteById() {
        entityManager.persist(student1);
        entityManager.flush();

        studentRepository.deleteById(student1.getId());
        Optional<Student> deletedStudent = studentRepository.findById(student1.getId());
        assertFalse(deletedStudent.isPresent());
    }

    @Test
    void testFindByEmail() {
        entityManager.persist(student1);
        entityManager.flush();

        Optional<Student> found = studentRepository.findByEmail("alice@example.com");
        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getName());
    }

    @Test
    void testFindByEmail_NotFound() {
        Optional<Student> found = studentRepository.findByEmail("nonexistent@example.com");
        assertFalse(found.isPresent());
    }
}
