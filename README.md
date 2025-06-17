In-Memory API Server (Spring Boot)

A simple RESTful API server built with Spring Boot, featuring JWT-based authentication, in-memory H2 storage, and core CRUD functionality. This project was developed as part of a backend developer evaluation task.

🛠️ Tech Stack

Java 8+

Spring Boot 3.5

Spring Security

H2 (in-memory database)

JWT (JSON Web Token)

Maven

✅ Features
User Signup and Login (no authentication required for these)

Passwords are securely hashed using BCrypt

Generate JWT token on successful login

Protected APIs for:

Creating Posts

Liking Posts

Deleting Posts

Fetching All Posts

Global Exception Handling for runtime and checked exceptions

Logs using SLF4J for every important operation and error

@ControllerAdvice used for centralized error management

Clean code with meaningful comments

📁 Project Structure
php
Copy
Edit
com.ApiServer.java/
├── Controller/
│   ├── AuthController.java        # Handles signup & login
│   └── PostController.java        # Handles post operations
├── Model/
│   ├── User.java
│   └── Post.java
├── Repository/
│   ├── UserRepository.java
│   └── PostRepository.java
├── Security/
│   ├── JwtUtil.java               # Token generation & parsing
│   ├── JwtFilter.java             # Filters incoming requests
│   └── SecurityConfig.java        # Main security config
├── Exception/
│   └── GlobalExceptionHandler.java
└── JavaApplication.java           # Main application entry point
🚀 Getting Started
Prerequisites
Java 17+

Maven

IDE (e.g., IntelliJ, Eclipse)

Postman

Run the Application
bash
Copy
Edit
mvn spring-boot:run
The server will start at: http://localhost:8080

🧪 API Testing
You can use Postman to test the following endpoints.

Authentication Endpoints

POST /auth/signup      - Register a new user

POST /auth/login       - Login and receive a JWT token

No token is required for signup/login.
On successful login, a Bearer token will be returned to use for secured APIs.

Post Endpoints (Protected by JWT)

POST   /posts/create         - Create a post (requires token)

POST   /posts/like/{id}      - Like a post (pass Post ID)

DELETE /posts/delete/{id}   - Delete a post (only by creator)

GET    /posts/all            - Fetch all posts


🛢️ H2 Database
You can access the H2 Console at:

http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb

Username: sa

Password: (leave blank)

You can use this console to explore tables such as USER and POST.

📌 Notes
JWT token expires in 1 hour

In-memory H2 DB resets on server restart

Passwords are securely encoded using BCrypt

SLF4J logs are added for traceability

Global error handling ensures consistent error response structure

Try-catch is used where appropriate (e.g., in token extraction logic)

