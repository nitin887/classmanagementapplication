package com.classmanagementsystem.classmanagement.util;

import com.classmanagementsystem.classmanagement.entity.Classes;
import com.classmanagementsystem.classmanagement.entity.Role;
import com.classmanagementsystem.classmanagement.entity.Student;
import com.classmanagementsystem.classmanagement.entity.Teacher;
import com.classmanagementsystem.classmanagement.entity.User;
import com.classmanagementsystem.classmanagement.repository.ClassesRepository;
import com.classmanagementsystem.classmanagement.repository.RoleRepository;
import com.classmanagementsystem.classmanagement.repository.StudentRepository;
import com.classmanagementsystem.classmanagement.repository.TeacherRepository;
import com.classmanagementsystem.classmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ClassesRepository classesRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(StudentRepository studentRepository,
                      TeacherRepository teacherRepository,
                      ClassesRepository classesRepository,
                      UserRepository userRepository,
                      RoleRepository roleRepository,
                      PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.classesRepository = classesRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional // Ensure the entire run method is within a single transaction
    public void run(String... args) throws Exception {
        // Clear existing data (for development/testing purposes)
        classesRepository.deleteAll();
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // 1. Create Roles
        // Ensure roles are saved and then fetched within the same transaction
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role role = new Role();
            role.setName("ROLE_USER");
            return roleRepository.save(role);
        });

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            return roleRepository.save(role);
        });

        // Fetch roles again to ensure they are managed within the current transaction
        userRole = roleRepository.findByName("ROLE_USER").get();
        adminRole = roleRepository.findByName("ROLE_ADMIN").get();

        // 2. Create Users
        Set<Role> userRolesSet = new HashSet<>(Arrays.asList(userRole));
        Set<Role> adminRolesSet = new HashSet<>(Arrays.asList(userRole, adminRole));

        User adminUser = new User(null, "Admin User", "admin", "admin@example.com", passwordEncoder.encode("adminpass"), adminRolesSet);
        User regularUser = new User(null, "Regular User", "user", "user@example.com", passwordEncoder.encode("userpass"), userRolesSet);

        userRepository.saveAll(Arrays.asList(adminUser, regularUser));

        // 3. Create Teachers
        Teacher teacher1 = new Teacher(null, "Prof. Alice", "Mathematics");
        Teacher teacher2 = new Teacher(null, "Dr. Bob", "Physics");
        Teacher teacher3 = new Teacher(null, "Ms. Carol", "Literature");
        Teacher teacher4 = new Teacher(null, "Mr. David", "Computer Science");
        Teacher teacher5 = new Teacher(null, "Dr. Eve", "Chemistry");
        teacherRepository.saveAll(Arrays.asList(teacher1, teacher2, teacher3, teacher4, teacher5));

        // 4. Create Students
        Student student1 = new Student(null, "Student A", "studentA@example.com");
        Student student2 = new Student(null, "Student B", "studentB@example.com");
        Student student3 = new Student(null, "Student C", "studentC@example.com");
        Student student4 = new Student(null, "Student D", "studentD@example.com");
        Student student5 = new Student(null, "Student E", "studentE@example.com");
        Student student6 = new Student(null, "Student F", "studentF@example.com");
        Student student7 = new Student(null, "Student G", "studentG@example.com");
        Student student8 = new Student(null, "Student H", "studentH@example.com");
        Student student9 = new Student(null, "Student I", "studentI@example.com");
        Student student10 = new Student(null, "Student J", "studentJ@example.com");
        studentRepository.saveAll(Arrays.asList(student1, student2, student3, student4, student5, student6, student7, student8, student9, student10));

        // 5. Create Classes and Enroll Students
        Classes mathClass = new Classes(null, "Algebra I", teacher1, new HashSet<>(Arrays.asList(student1, student2, student3)));
        Classes physicsClass = new Classes(null, "Mechanics", teacher2, new HashSet<>(Arrays.asList(student4, student5)));
        Classes literatureClass = new Classes(null, "World Lit", teacher3, new HashSet<>(Arrays.asList(student6, student7, student8)));
        Classes csClass = new Classes(null, "Intro to Java", teacher4, new HashSet<>(Arrays.asList(student1, student9, student10)));
        Classes chemClass = new Classes(null, "Organic Chem", teacher5, new HashSet<>(Arrays.asList(student2, student5, student7)));

        classesRepository.saveAll(Arrays.asList(mathClass, physicsClass, literatureClass, csClass, chemClass));

        System.out.println("Dummy data loaded successfully!");
    }
}
