# Eagle Bank MVP

## Overview
Eagle Bank MVP is a clean, extensible REST API built with Java 21 and Spring Boot 3. It implements a core subset of banking features focusing on user management and account creation with production-style structure, security, and testing.

### MVP Scope
The implementation focuses on 3 main capabilities:
1. **Create User**: Registration with validation and duplicate detection.
2. **Fetch User**: Secure retrieval of user profile (self-only) with caching.
3. **Create Bank Account**: Automatic account number generation and initial balance setup.

## Tech Stack
- **Java 21**
- **Spring Boot 3.2.5**
- **Spring Security + JWT** (Authentication & Authorization)
- **Spring Data JPA + H2** (Persistence)
- **Spring Cache** (ConcurrentMapCacheManager)
- **MapStruct** (Object Mapping)
- **Lombok** (Boilerplate reduction)
- **Springdoc-OpenAPI** (Swagger documentation)
- **JUnit 5 & Mockito** (Testing)

## Run Instructions
### Prerequisites
- JDK 21
- Maven 3.x

### Build and Test
```bash
mvn clean test
```

### Run Application
```bash
mvn spring-boot:run
```
The API will be available at `http://localhost:8080`.
Swagger UI: `http://localhost:8080/swagger-ui.html`

## Authentication Flow
1. **Create User**: `POST /v1/users` (No token required).
2. **Login**: `POST /v1/auth/login` with email and password to receive a JWT.
3. **Protected Calls**: Include the JWT in the `Authorization` header as `Bearer <token>`.

## Implemented Endpoints

### Authentication
- `POST /v1/auth/login`: Authenticate and get JWT.

### Users
- `POST /v1/users`: Create a new user.
  - Payload: `firstName`, `lastName`, `email`, `password`.
- `GET /v1/users/{userId}`: Fetch user details (Requires Auth, ownership check).

### Accounts
- `POST /v1/accounts`: Create a bank account for the authenticated user.
  - Payload: `accountType` (CHECKING/SAVINGS).

## Bruno Usage
A Bruno API collection is provided in the `/bruno` directory.
1. Open Bruno.
2. Import the collection from the `/bruno` folder.
3. Select the `Local` environment.
4. Run "Create User" to register.
5. Run "Login" to get the token (automatically saved to environment).
6. Run protected requests (Get User, Create Account).

## Design Decisions
- **Layered Architecture**: Controller -> Service -> Repository for clear separation of concerns.
- **Security**: JWT-based stateless authentication ensures scalability and meets requirements.
- **Caching**: Used `@Cacheable` on `getUser` to demonstrate performance optimization for read-heavy operations.
- **Extensibility**: Included placeholder entities for Transactions to show the path for future development.
- **Exception Handling**: Global exception handler ensures consistent API responses for errors.

## Definition of Done Verification
- [x] Project compiles.
- [x] OpenAPI docs exposed.
- [x] JWT auth implemented.
- [x] 3 MVP endpoints implemented.
- [x] Validation and Exception handling consistent.
- [x] Caching enabled.
- [x] Unit tests passing (Service layer).
- [x] Bruno collection included.
- [x] README complete.
