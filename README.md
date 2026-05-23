# Eagle Bank MVP

## Overview
Eagle Bank MVP is a clean, extensible REST API built with Java 21 and Spring Boot 3. It implements a core subset of banking features focusing on user management and account creation with production-style structure, security, and testing.

The implementation includes several core capabilities:
1. **User Management**: Registration, profile retrieval, updates, and account deletion.
2. **Account Management**: Support for multiple bank accounts per user (Checking/Savings).
3. **Transaction Processing**: Secure transfers between accounts and transaction history.

## Tech Stack
- **Java 21**
- **Spring Boot 3.2.5**
- **Spring Security + JWT** (Authentication & Authorization)
- **Spring Data JPA + H2** (Persistence)
- **Spring Cache** (ConcurrentMapCacheManager)
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
The project includes:
- **Unit Tests**: Focus on service logic and mapping.
- **Integration Tests**: Comprehensive end-to-end flows using `MockMvc` and an in-memory H2 database, covering user lifecycle, authentication, and banking transactions.

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
- `GET /v1/users/{userId}`: Fetch user details (Requires Auth, ownership check).
- `PATCH /v1/users/{userId}`: Update user details.
- `DELETE /v1/users/{userId}`: Delete user.

### Accounts
- `POST /v1/accounts`: Create a bank account for the authenticated user.
  - Payload: `accountType` (CHECKING/SAVINGS), `name`.
- `GET /v1/accounts`: List all accounts for the authenticated user.
- `GET /v1/accounts/{accountNumber}`: Fetch account details.
- `PATCH /v1/accounts/{accountNumber}`: Update account details.
- `DELETE /v1/accounts/{accountNumber}`: Close/Delete a bank account.

### Transactions
- `POST /v1/accounts/{accountNumber}/transactions`: Create a new transaction (Deposit/Withdrawal/Transfer).
- `GET /v1/accounts/{accountNumber}/transactions`: List transactions for an account.
- `GET /v1/accounts/{accountNumber}/transactions/{transactionId}`: Fetch specific transaction details.



## Design Decisions
- **Layered Architecture**: Controller -> Service -> Repository for clear separation of concerns.
- **Security**: JWT-based stateless authentication ensures scalability and meets requirements.
- **Caching**: Used `@Cacheable` on `getUser` to demonstrate performance optimization for read-heavy operations.
- **Extensibility**: Solid foundation for financial services with full transaction support.
- **Exception Handling**: Global exception handler ensures consistent API responses for errors.
