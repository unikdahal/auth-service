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

## API Reference

The authentication service provides the following RESTful endpoints:

### Authentication Endpoints

#### Register User
- **URL:** `/api/auth/register`
- **Method:** `POST`
- **Description:** Registers a new user and returns access/refresh tokens
- **Request Body:**
  ```json
  {
    "email": "user@example.com",
    "username": "username",
    "password": "SecurePassword123!",
    "roles": ["USER"]
  }
  ```
- **Success Response:** (200 OK)
  ```json
  {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer"
  }
  ```
- **Error Response:** (400 Bad Request)
  ```json
  {
    "accessToken": null,
    "refreshToken": null,
    "tokenType": "error: Email already exists"
  }
  ```

#### User Login
- **URL:** `/api/auth/login`
- **Method:** `POST`
- **Description:** Authenticates a user and returns access/refresh tokens
- **Request Body:**
  ```json
  {
    "usernameOrEmail": "user@example.com",
    "password": "SecurePassword123!"
  }
  ```
- **Success Response:** (200 OK)
  ```json
  {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer"
  }
  ```
- **Error Response:** (401 Unauthorized)
  ```json
  {
    "accessToken": null,
    "refreshToken": null,
    "tokenType": "error: Invalid credentials"
  }
  ```

#### Logout
- **URL:** `/api/auth/logout`
- **Method:** `POST`
- **Description:** Logs out a user by invalidating their refresh token
- **Request Body:**
  ```json
  {
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```
- **Success Response:** (200 OK)
- **Error Response:** (401 Unauthorized)

#### Refresh Token
- **URL:** `/api/auth/refresh`
- **Method:** `POST`
- **Description:** Obtains a new access token using a valid refresh token
- **Request Body:**
  ```json
  {
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```
- **Success Response:** (200 OK)
  ```json
  {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": null,
    "tokenType": "Bearer"
  }
  ```
- **Error Response:** (401 Unauthorized)
  ```json
  {
    "accessToken": null,
    "refreshToken": null,
    "tokenType": "error: Invalid or expired refresh token"
  }
  ```

#### Check Authentication
- **URL:** `/api/auth/check-auth`
- **Method:** `POST`
- **Description:** Validates if an access token is valid and not expired
- **Request Body:**
  ```
  eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
  ```
- **Success Response:** (200 OK)
- **Error Response:** (401 Unauthorized)

### Implementation in Client Applications

#### Frontend (JavaScript)

```javascript
// Register a new user
async function registerUser(userData) {
  const response = await fetch('/api/auth/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData)
  });
  return await response.json();
}

// Login
async function login(credentials) {
  const response = await fetch('/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(credentials)
  });
  return await response.json();
}

// Making authenticated requests
function makeAuthenticatedRequest(url, accessToken) {
  return fetch(url, {
    headers: { 'Authorization': `Bearer ${accessToken}` }
  });
}

// Refresh token when access token expires
async function refreshToken(refreshToken) {
  const response = await fetch('/api/auth/refresh', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ refreshToken })
  });
  return await response.json();
}

// Logout
function logout(refreshToken) {
  return fetch('/api/auth/logout', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ refreshToken })
  });
}
```

#### Other Services (Java)

```java
@Service
public class AuthenticationClient {
    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public AuthenticationClient(RestTemplate restTemplate, 
                              @Value("${auth.service.url}") String authServiceUrl) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
    }

    public boolean validateToken(String token) {
        try {
            restTemplate.postForEntity(
                authServiceUrl + "/api/auth/check-auth",
                token,
                Void.class
            );
            return true;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return false;
            }
            throw e;
        }
    }
}
```

## Security Best Practices
- Use HTTPS in production.
- Configure JWT secrets and expiration in environment variables.
- Enable CORS as needed for your frontend(s).
- Use strong password policies and breach checks.

## Token-Based Authentication
This service implements stateless JWT-based authentication with the following characteristics:

- **Access Tokens:** Short-lived (default 1 hour) tokens for API access
- **Refresh Tokens:** Longer-lived tokens (default 24 hours) to obtain new access tokens
- **Stateless Validation:** Tokens can be validated without database lookups
- **Token Structure:** JWT tokens with standardized claims:
  - `sub`: User ID
  - `typ`: Token type (access or refresh)
  - `username`: User's username
  - `roles`: User's authorization roles
  - Standard JWT claims (`iat`, `exp`)

For logout functionality in this stateless model, clients should:
1. Remove tokens from local storage
2. Use short-lived access tokens to minimize risk

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
