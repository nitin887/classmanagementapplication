package com.classmanagementsystem.classmanagement.repository;

import com.classmanagementsystem.classmanagement.entity.Role;
import com.classmanagementsystem.classmanagement.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository; // To manage roles for users

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;
    private Role roleUser;
    private Role roleAdmin;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        entityManager.clear();

        roleUser = entityManager.persist(new Role(null, "ROLE_USER"));
        roleAdmin = entityManager.persist(new Role(null, "ROLE_ADMIN"));

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleUser);

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(roleUser);
        adminRoles.add(roleAdmin);

        user1 = new User(null, "Alice", "alice_user", "alice@example.com", "password_hash_1", userRoles);
        user2 = new User(null, "Bob", "bob_admin", "bob@example.com", "password_hash_2", adminRoles);
    }

    @Test
    void testSaveUser() {
        User savedUser = userRepository.save(user1);
        assertNotNull(savedUser.getId());
        assertEquals("Alice", savedUser.getName());
        assertFalse(savedUser.getRoles().isEmpty());
        assertTrue(savedUser.getRoles().contains(roleUser));
    }

    @Test
    void testFindById() {
        entityManager.persist(user1);
        entityManager.flush();

        Optional<User> foundUser = userRepository.findById(user1.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("alice_user", foundUser.get().getUsername());
    }

    @Test
    void testFindById_NotFound() {
        Optional<User> foundUser = userRepository.findById(99L);
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testFindAll() {
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void testDeleteById() {
        entityManager.persist(user1);
        entityManager.flush();

        userRepository.deleteById(user1.getId());
        Optional<User> deletedUser = userRepository.findById(user1.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void testFindByEmail() {
        entityManager.persist(user1);
        entityManager.flush();

        Optional<User> found = userRepository.findByEmail("alice@example.com");
        assertTrue(found.isPresent());
        assertEquals("alice_user", found.get().getUsername());
    }

    @Test
    void testFindByEmail_NotFound() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByUsername() {
        entityManager.persist(user1);
        entityManager.flush();

        Optional<User> found = userRepository.findByUsername("alice_user");
        assertTrue(found.isPresent());
        assertEquals("alice@example.com", found.get().getEmail());
    }

    @Test
    void testFindByUsername_NotFound() {
        Optional<User> found = userRepository.findByUsername("nonexistent_user");
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByUsernameOrEmail_ByUsername() {
        entityManager.persist(user1);
        entityManager.flush();

        Optional<User> found = userRepository.findByUsernameOrEmail("alice_user", "dummy@example.com");
        assertTrue(found.isPresent());
        assertEquals("alice@example.com", found.get().getEmail());
    }

    @Test
    void testFindByUsernameOrEmail_ByEmail() {
        entityManager.persist(user1);
        entityManager.flush();

        Optional<User> found = userRepository.findByUsernameOrEmail("dummy_user", "alice@example.com");
        assertTrue(found.isPresent());
        assertEquals("alice_user", found.get().getUsername());
    }

    @Test
    void testFindByUsernameOrEmail_NotFound() {
        Optional<User> found = userRepository.findByUsernameOrEmail("nonexistent", "nonexistent@example.com");
        assertFalse(found.isPresent());
    }

    @Test
    void testExistsByUsername() {
        entityManager.persist(user1);
        entityManager.flush();

        assertTrue(userRepository.existsByUsername("alice_user"));
        assertFalse(userRepository.existsByUsername("nonexistent_user"));
    }

    @Test
    void testExistsByEmail() {
        entityManager.persist(user1);
        entityManager.flush();

        assertTrue(userRepository.existsByEmail("alice@example.com"));
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
    }
}
