package com.classmanagementsystem.classmanagement.repository;

import com.classmanagementsystem.classmanagement.entity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeEach
    void setUp() {
        teacherRepository.deleteAll();
        entityManager.clear();

        teacher1 = new Teacher(null, "Mr. Smith", "Math");
        teacher2 = new Teacher(null, "Ms. Jones", "Physics");
    }

    @Test
    void testSaveTeacher() {
        Teacher savedTeacher = teacherRepository.save(teacher1);
        assertNotNull(savedTeacher.getId());
        assertEquals("Mr. Smith", savedTeacher.getName());
    }

    @Test
    void testFindById() {
        entityManager.persist(teacher1);
        entityManager.flush();

        Optional<Teacher> foundTeacher = teacherRepository.findById(teacher1.getId());
        assertTrue(foundTeacher.isPresent());
        assertEquals("Mr. Smith", foundTeacher.get().getName());
    }

    @Test
    void testFindById_NotFound() {
        Optional<Teacher> foundTeacher = teacherRepository.findById(99L);
        assertFalse(foundTeacher.isPresent());
    }

    @Test
    void testFindAll() {
        entityManager.persist(teacher1);
        entityManager.persist(teacher2);
        entityManager.flush();

        List<Teacher> teachers = teacherRepository.findAll();
        assertEquals(2, teachers.size());
    }

    @Test
    void testDeleteById() {
        entityManager.persist(teacher1);
        entityManager.flush();

        teacherRepository.deleteById(teacher1.getId());
        Optional<Teacher> deletedTeacher = teacherRepository.findById(teacher1.getId());
        assertFalse(deletedTeacher.isPresent());
    }

    @Test
    void testFindByName() {
        entityManager.persist(teacher1);
        entityManager.persist(new Teacher(null, "Mr. Smith", "Chemistry")); // Another teacher with same name
        entityManager.flush();

        List<Teacher> found = teacherRepository.findByName("Mr. Smith");
        assertNotNull(found);
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(t -> t.getSubject().equals("Math")));
        assertTrue(found.stream().anyMatch(t -> t.getSubject().equals("Chemistry")));
    }

    @Test
    void testFindByName_NotFound() {
        List<Teacher> found = teacherRepository.findByName("NonExistent");
        assertNotNull(found);
        assertTrue(found.isEmpty());
    }
}
