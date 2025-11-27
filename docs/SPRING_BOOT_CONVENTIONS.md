# Java Spring Boot Conventions

> A guide for developers coming from PHP/TypeScript/Laravel background

## Table of Contents

- [Project Structure](#project-structure)
- [Naming Conventions](#naming-conventions)
- [Key Annotations](#key-annotations)
- [Laravel to Spring Boot Comparison](#laravel-to-spring-boot-comparison)
- [Typical Package Structure](#typical-package-structure)
- [Common Patterns](#common-patterns)
- [Quick Tips](#quick-tips)

---

## Project Structure

| Laravel | Spring Boot |
|---------|-------------|
| `app/Http/Controllers` | `src/main/java/.../controller` |
| `app/Models` | `src/main/java/.../model` or `entity` |
| `app/Services` | `src/main/java/.../service` |
| `app/Repositories` | `src/main/java/.../repository` |
| `resources/views` | `src/main/resources/templates` |
| `.env` | `application.properties` or `application.yml` |
| `routes/web.php` | Annotations in Controller classes |
| `routes/api.php` | Annotations in Controller classes |
| `database/migrations` | `src/main/resources/db/migration` (Flyway) |
| `composer.json` | `pom.xml` (Maven) or `build.gradle` (Gradle) |

---

## Naming Conventions

| Concept | Convention | Example |
|---------|------------|---------|
| Classes | PascalCase | `UserController`, `OrderService` |
| Interfaces | PascalCase | `UserRepository`, `PaymentGateway` |
| Methods | camelCase | `findById()`, `getAllUsers()` |
| Variables | camelCase | `userName`, `orderList` |
| Constants | UPPER_SNAKE_CASE | `MAX_SIZE`, `DEFAULT_TIMEOUT` |
| Packages | all lowercase | `com.chanthorn.first.controller` |
| Database Tables | snake_case | `user_orders`, `product_categories` |
| REST Endpoints | kebab-case | `/api/user-orders`, `/api/v1/products` |

---

## Key Annotations

### Controller Annotations

```java
// REST API Controller (returns JSON)
@RestController
@RequestMapping("/api/users")
public class UserController {
    // ...
}

// Web Controller (returns views/templates)
@Controller
@RequestMapping("/users")
public class UserWebController {
    // ...
}
```

### HTTP Method Annotations

| Annotation | HTTP Method | Laravel Equivalent |
|------------|-------------|-------------------|
| `@GetMapping("/users")` | GET | `Route::get('/users', ...)` |
| `@PostMapping("/users")` | POST | `Route::post('/users', ...)` |
| `@PutMapping("/users/{id}")` | PUT | `Route::put('/users/{id}', ...)` |
| `@PatchMapping("/users/{id}")` | PATCH | `Route::patch('/users/{id}', ...)` |
| `@DeleteMapping("/users/{id}")` | DELETE | `Route::delete('/users/{id}', ...)` |

### Dependency Injection Annotations

```java
@Service          // Business logic layer (like Laravel Service classes)
@Repository       // Data access layer (like Laravel Repository pattern)
@Component        // Generic Spring-managed component
@Configuration    // Configuration class (like config/*.php files)
@Autowired        // Inject dependency (like Laravel's constructor injection)
```

### Request Parameter Annotations

```java
@PathVariable     // URL path variable: /users/{id}
@RequestBody      // JSON request body
@RequestParam     // Query parameters: ?name=value
@RequestHeader    // HTTP headers
@CookieValue      // Cookie values
```

### Validation Annotations

```java
@Valid            // Enable validation on request body
@NotNull          // Field cannot be null
@NotBlank         // String cannot be null or empty
@Size(min, max)   // String/Collection size constraints
@Email            // Valid email format
@Min / @Max       // Numeric range
@Pattern          // Regex pattern
```

---

## Laravel to Spring Boot Comparison

### Route Definition

```php
// Laravel
Route::get('/users/{id}', [UserController::class, 'show']);
Route::post('/users', [UserController::class, 'store']);
Route::put('/users/{id}', [UserController::class, 'update']);
Route::delete('/users/{id}', [UserController::class, 'destroy']);
```

```java
// Spring Boot
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public User show(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    public User store(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public void destroy(@PathVariable Long id) {
        userService.delete(id);
    }
}
```

### Request Validation

```php
// Laravel
public function store(Request $request) {
    $validated = $request->validate([
        'name' => 'required|string|max:255',
        'email' => 'required|email|unique:users',
    ]);
}
```

```java
// Spring Boot - DTO with validation
public class CreateUserDto {
    @NotBlank(message = "Name is required")
    @Size(max = 255)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // Getters and Setters
}

// Controller
@PostMapping
public User store(@Valid @RequestBody CreateUserDto dto) {
    return userService.create(dto);
}
```

### Dependency Injection

```php
// Laravel
class UserController extends Controller {
    public function __construct(
        private UserService $userService
    ) {}
}
```

```java
// Spring Boot
@RestController
public class UserController {

    private final UserService userService;

    // Constructor injection (recommended)
    public UserController(UserService userService) {
        this.userService = userService;
    }
}

// Or with Lombok
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
}
```

### Eloquent vs JPA Repository

```php
// Laravel Eloquent
User::find($id);
User::all();
User::where('email', $email)->first();
User::create(['name' => 'John', 'email' => 'john@example.com']);
```

```java
// Spring Data JPA
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByNameContaining(String name);
}

// Usage in Service
userRepository.findById(id);
userRepository.findAll();
userRepository.findByEmail(email);
userRepository.save(new User("John", "john@example.com"));
```

### Environment Variables

```env
# Laravel .env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=myapp
```

```properties
# Spring Boot application.properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/myapp
spring.datasource.username=root
spring.datasource.password=secret
spring.jpa.hibernate.ddl-auto=update
```

---

## Typical Package Structure

```
com.chanthorn.first/
├── FirstApplication.java        # Main entry point (like public/index.php)
│
├── controller/                  # HTTP Controllers
│   ├── UserController.java
│   └── ProductController.java
│
├── service/                     # Business Logic Layer
│   ├── UserService.java
│   └── impl/
│       └── UserServiceImpl.java
│
├── repository/                  # Data Access Layer (like Eloquent)
│   └── UserRepository.java
│
├── model/ (or entity/)          # Database Entities (like Laravel Models)
│   └── User.java
│
├── dto/                         # Data Transfer Objects
│   ├── request/
│   │   └── CreateUserRequest.java
│   └── response/
│       └── UserResponse.java
│
├── config/                      # Configuration Classes
│   ├── SecurityConfig.java
│   └── WebConfig.java
│
├── exception/                   # Custom Exceptions
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
│
└── util/                        # Utility/Helper Classes
    └── DateUtils.java
```

---

## Common Patterns

### Response Entity (HTTP Responses)

```java
// Return with specific HTTP status
@GetMapping("/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    return userRepository.findById(id)
        .map(ResponseEntity::ok)                              // 200 OK
        .orElse(ResponseEntity.notFound().build());           // 404 Not Found
}

@PostMapping
public ResponseEntity<User> createUser(@RequestBody User user) {
    User saved = userRepository.save(user);
    return ResponseEntity
        .status(HttpStatus.CREATED)                           // 201 Created
        .body(saved);
}
```

### Exception Handling (like Laravel Exception Handler)

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
}
```

### Service Layer Pattern

```java
// Interface
public interface UserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    void deleteById(Long id);
}

// Implementation
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
```

---

## Quick Tips

### 1. Lombok - Reduce Boilerplate

```java
// Without Lombok
public class User {
    private Long id;
    private String name;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// With Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
}
```

### 2. Optional for Null Safety

```java
// Like TypeScript's undefined handling
Optional<User> user = userRepository.findById(id);

// Check if present
if (user.isPresent()) {
    return user.get();
}

// Or use functional style
return user.orElseThrow(() -> new ResourceNotFoundException("User not found"));

// Or with default value
return user.orElse(new User());
```

### 3. Running the Application

```bash
# Development
./mvnw spring-boot:run

# Build JAR
./mvnw clean package

# Run JAR
java -jar target/first-0.0.1-SNAPSHOT.jar

# With specific profile (like Laravel environments)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### 4. Hot Reload (like Laravel's auto-reload)

Add to `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

### 5. Logging (like Laravel's Log facade)

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public void processUser(User user) {
        log.info("Processing user: {}", user.getName());
        log.debug("User details: {}", user);
        log.error("Error processing user", exception);
    }
}

// Or with Lombok
@Slf4j
@Service
public class UserService {
    public void processUser(User user) {
        log.info("Processing user: {}", user.getName());
    }
}
```

---

## Useful Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Baeldung Tutorials](https://www.baeldung.com/)
- [Spring Initializr](https://start.spring.io/) - Project generator

---

*Happy coding! 🚀*
