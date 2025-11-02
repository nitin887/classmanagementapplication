package com.classmanagementsystem.classmanagement.repository;

import com.classmanagementsystem.classmanagement.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Role roleUser;
    private Role roleAdmin;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();
        entityManager.clear();

        roleUser = new Role(null, "ROLE_USER");
        roleAdmin = new Role(null, "ROLE_ADMIN");
    }

    @Test
    void testSaveRole() {
        Role savedRole = roleRepository.save(roleUser);
        assertNotNull(savedRole.getId());
        assertEquals("ROLE_USER", savedRole.getName());
    }

    @Test
    void testFindById() {
        entityManager.persist(roleUser);
        entityManager.flush();

        Optional<Role> foundRole = roleRepository.findById(roleUser.getId());
        assertTrue(foundRole.isPresent());
        assertEquals("ROLE_USER", foundRole.get().getName());
    }

    @Test
    void testFindById_NotFound() {
        Optional<Role> foundRole = roleRepository.findById(99L);
        assertFalse(foundRole.isPresent());
    }

    @Test
    void testFindAll() {
        entityManager.persist(roleUser);
        entityManager.persist(roleAdmin);
        entityManager.flush();

        Iterable<Role> roles = roleRepository.findAll();
        long count = roles.spliterator().getExactSizeIfKnown();
        assertEquals(2, count);
    }

    @Test
    void testDeleteById() {
        entityManager.persist(roleUser);
        entityManager.flush();

        roleRepository.deleteById(roleUser.getId());
        Optional<Role> deletedRole = roleRepository.findById(roleUser.getId());
        assertFalse(deletedRole.isPresent());
    }

    @Test
    void testFindByName() {
        entityManager.persist(roleUser);
        entityManager.flush();

        Optional<Role> found = roleRepository.findByName("ROLE_USER");
        assertTrue(found.isPresent());
        assertEquals("ROLE_USER", found.get().getName());
    }

    @Test
    void testFindByName_NotFound() {
        Optional<Role> found = roleRepository.findByName("ROLE_NONEXISTENT");
        assertFalse(found.isPresent());
    }
}
