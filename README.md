# Class Management System

This is a Spring Boot application for managing classes, students, and teachers. It provides RESTful APIs for various operations related to class management.

## Features

*   User Authentication (Login, Register)
*   CRUD operations for Classes, Students, Teachers, and Users
*   Student enrollment in classes
*   Role-based access control (Admin, User)
*   API Documentation with Swagger UI
*   Database interaction with MySQL

## Technologies Used

*   Spring Boot
*   Spring Data JPA
*   Spring Security
*   MySQL
*   Maven
*   Lombok
*   Springdoc OpenAPI (for Swagger UI)

## Setup Instructions

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/classmanagement.git
    cd classmanagement
    ```
    (Replace `https://github.com/your-username/classmanagement.git` with the actual repository URL.)

2.  **Java Development Kit (JDK):**
    Ensure you have JDK 17 or newer installed.

3.  **Database Configuration:**
    *   Ensure you have a MySQL server running.
    *   Update the `src/main/resources/application.properties` file with your MySQL database credentials.
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/class_management?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
        spring.datasource.username=root
        spring.datasource.password=abc123
        ```
        (Replace `class_management`, `root`, and `abc123` with your actual database name, username, and password.)
    *   **Security Note:** For production environments, consider using environment variables or a secrets management system for database credentials instead of hardcoding them in `application.properties`.

4.  **Build the project:**
    ```bash
    mvn clean install
    ```

## Running the Application

You can run the application using Maven:
```bash
mvn spring-boot:run
```
Alternatively, you can build an executable JAR and run it:
```bash
mvn clean package
java -jar target/classmanagement-0.0.1-SNAPSHOT.jar
```
The application will start on `http://localhost:8080`.

## API Documentation (Swagger UI)

Once the application is running, you can access the API documentation via Swagger UI at:
*   `http://localhost:8080/swagger-ui.html`
*   `http://localhost:8080/swagger-ui/index.html`

## Default User Credentials

The `DataLoader.java` class populates the database with some initial data, including users. You can use these credentials to test the authentication endpoints:

```json
[
  {
    "name": "Admin User",
    "username": "admin",
    "email": "admin@example.com",
    "password": "adminpass",
    "roles": ["ROLE_USER", "ROLE_ADMIN"]
  },
  {
    "name": "Regular User",
    "username": "user",
    "email": "user@example.com",
    "password": "userpass",
    "roles": ["ROLE_USER"]
  }
]
```

**Note:** The passwords (`adminpass`, `userpass`) are encoded by `BCryptPasswordEncoder` during application startup. You should use these plain-text passwords when logging in via the `/api/auth/login` endpoint.
**Important:** For production deployments, ensure you change or remove these default credentials.

## Endpoints Overview

*   `/api/auth/register`: Register a new user
*   `/api/auth/login`: Authenticate a user
*   `/api/classes`: CRUD operations for classes
*   `/api/students`: CRUD operations for students
*   `/api/teachers`: CRUD operations for teachers
*   `/api/auth/users`: CRUD operations for users (Admin only for some operations)
*   `/api/classes/classes/{classesId}/students/{studentId}`: Enroll a student in a class

Please refer to the Swagger UI for detailed information on each endpoint, including request/response models and available parameters.

## How to Contribute

We welcome contributions to the Class Management System! If you'd like to contribute, please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix: `git checkout -b feature/your-feature-name` or `git checkout -b bugfix/issue-description`.
3.  Make your changes and ensure the code adheres to the project's coding style.
4.  Write appropriate unit and integration tests for your changes.
5.  Commit your changes with a clear and descriptive commit message.
6.  Push your branch to your forked repository.
7.  Open a pull request to the `main` branch of the original repository, describing your changes in detail.


