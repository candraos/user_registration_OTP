User Registration with OTP Service
This project is a Spring Boot application that provides user registration, login, and OTP (One-Time Password) verification functionality. It integrates with RabbitMQ for messaging and Redis for caching OTPs. The application also uses PostgreSQL as the database and generates JWT tokens for user authentication.

Features
User Registration: Allows users to register with their email, name, password, and age.
Login: Authenticates users with email and password and returns a JWT token.
OTP Generation: Generates a 6-digit OTP upon successful registration and sends it to RabbitMQ.
OTP Verification: Verifies the OTP sent to the user.
JWT Authentication: Issues JWT tokens for secure user authentication.
Redis Integration: Caches OTPs for quick access and expiration handling.
RabbitMQ Integration: Sends OTP events to a queue for processing.
PostgreSQL Integration: Stores user and OTP data persistently.
Technologies Used
Spring Boot: Backend framework.
PostgreSQL: Relational database for persistent storage.
Redis: In-memory data store for caching OTPs.
RabbitMQ: Message broker for sending OTP events.
JWT: JSON Web Tokens for user authentication.
Docker: Containerization of services.
Swagger: API documentation.
Prerequisites
Java 17 or higher
Maven for dependency management
Docker and Docker Compose for running services
PostgreSQL, Redis, and RabbitMQ (can be run via Docker)
Setup Instructions
1. Clone the Repository
2. Run Services with Docker Compose (docker-compose up -d)
3. Run the Application


API Endpoints 

User Management

1. Register User
URL: /user/create
Method: POST
Request Body:
Response:
200 OK: User registered successfully, OTP sent.
400 Bad Request: User already exists or invalid input.
   
2. Login User
URL: /user/login
Method: POST
Request Body:
Response:
200 OK: Returns a JWT token.
401 Unauthorized: Invalid credentials.
   
3. Get User Details
URL: /user/details
Method: GET
Headers:
Authorization: Bearer <JWT_TOKEN>
Response:
200 OK: Returns user details.
404 Not Found: User not found.
   
OTP Management
1. Verify OTP
URL: /otp/verify
Method: POST
Request Body:
Response:
200 OK: OTP verified successfully.
400 Bad Request: Invalid OTP or expired.
