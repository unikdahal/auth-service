# Auth Service

A modern, generic, and extensible authentication service designed for use across multiple websites and applications. Built with Java, Spring Boot, and Clean/Hexagonal Architecture principles.

## Features
- **Generic Domain Model:** Easily extendable user, role, and credential types.
- **Pluggable Authentication:** Support for multiple authentication strategies (password, OTP, etc.).
- **JWT-based Token Management:** Secure, stateless authentication with refresh tokens.
- **Password Policy & Security:** Strong password validation, encoding, and breach checks.
- **Notification System:** Pluggable notification channels (email, SMS, etc.).
- **OpenAPI/Swagger Documentation:** Auto-generated, interactive API docs.
- **Validation & Error Handling:** Custom exceptions, validation annotations, and clear error responses.
- **Testable & Modular:** JUnit, Testcontainers, and MapStruct for robust testing and mapping.
- **Fully Configurable:** All paths, endpoints, and settings are configurable via properties.

## Architecture
- **auth-core:** Domain, application services, and ports (interfaces).
- **auth-infra:** Infrastructure adapters (DB, email, cache, security).
- **auth-api:** REST API, controllers, DTOs, and OpenAPI docs.

Follows Clean/Hexagonal Architecture for maximum flexibility and testability.

## Getting Started
### Prerequisites
- Java 17+
- Maven 3.8+
- Redis (optional, for token management)

### Build & Run
```sh
mvn clean install
cd auth-api
mvn spring-boot:run
```

### Configuration
The service is designed to be highly configurable. Key configuration properties include:

```yaml
# API configuration
api:
  # Authentication endpoints configuration
  auth:
    base-path: /api/auth
    register-path: /register
    login-path: /login
    token-type: Bearer

  # Token endpoints configuration
  token:
    base-path: /api/token
    refresh-path: /refresh

  # API documentation configuration
  docs:
    title: Authentication Service API
    description: Generic Authentication API for multiple websites and applications
    version: 1.0.0
    group: auth

# Security configuration
security:
  jwt:
    secret: ${JWT_SECRET:your-secret-key-should-be-at-least-32-chars-long}
    access-token-validity-seconds: 3600
    refresh-token-validity-seconds: 86400
    token-type: Bearer
```

See `application.yml` for a complete list of configuration options.

### API Documentation
- After running, visit: `http://localhost:8080/swagger-ui.html` for interactive OpenAPI docs.

## Usage & Extension
- **Add new authentication strategies:** Implement `AuthenticationStrategyPort` and register in the service.
- **Add new notification channels:** Implement `NotificationServicePort`.
- **Customize user model:** Extend `BaseUser` and update factory/repository ports.
- **Integrate with your website:** Use the REST API or embed the core module as a library.
- **Configure for your needs:** Customize all paths, endpoints, and settings via properties.

## Security Best Practices
- Use HTTPS in production.
- Configure JWT secrets and expiration in environment variables.
- Enable CORS as needed for your frontend(s).
- Use strong password policies and breach checks.

## Testing
- Unit and integration tests are provided using JUnit and Testcontainers.
- Run all tests:
  ```sh
  mvn test
  ```

## Contributing
- Fork the repo and submit PRs for new features, bug fixes, or improvements.
- Please add/maintain JavaDoc and tests for all public APIs.

## License
MIT

---

### Example: Registering a User (API)
**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
  "email": "user@example.com",
  "username": "user1",
  "password": "StrongP@ssw0rd!"
}
```

### Example: Authenticating a User (API)
**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "username": "user1",
  "password": "StrongP@ssw0rd!"
}
```

### Example: Refreshing a Token (API)
**Endpoint:** `POST /api/token/refresh`

**Request Body:**
```json
{
  "refreshToken": "..."
}
```

---

For more details, see the code and JavaDoc in each module.
