# Repository Guidelines

## Project Structure & Module Organization

This is a Java 25 Spring Boot 4.1 Maven application. Main code lives under `src/main/java/dev/yesserm/demosb4`, organized by responsibility:

- `controller/` exposes HTTP endpoints.
- `service/` and `service/impl/` contain business logic.
- `repository/` contains Spring Data JPA repositories.
- `model/` and `dto/` define persistence entities and API data shapes.
- `config/` contains application and client configuration.
- `client/` contains outbound API integration code.

Runtime configuration is in `src/main/resources/application.properties`. Flyway migrations live in `src/main/resources/db/migration` and should use versioned names such as `V2__add_user_email.sql`. Tests live in `src/test/java`; HTTP scratch files are in `src/test/http`.

## Build, Test, and Development Commands

Use the Maven wrapper so builds are consistent across machines:

- `./mvnw test` or `mvnw.cmd test`: run the JUnit/Spring Boot test suite.
- `./mvnw spring-boot:run` or `mvnw.cmd spring-boot:run`: start the app locally.
- `./mvnw clean package` or `mvnw.cmd clean package`: compile, test, and build the application artifact.

On Windows PowerShell, prefer `.\mvnw.cmd test` and `.\mvnw.cmd spring-boot:run`.

## Coding Style & Naming Conventions

Follow standard Java formatting with 4-space indentation and one public top-level type per file. Use package names under `dev.yesserm.demosb4`. Name classes by role, for example `UserController`, `UserService`, `UserServiceImpl`, and `UserRepository`. Keep controllers thin; place business rules in services and persistence concerns in repositories. Use constructor injection for Spring components when adding dependencies.

## Testing Guidelines

The project uses JUnit Jupiter with Spring Boot test support. Place tests in the same package path under `src/test/java`. Name test classes after the unit or slice under test, such as `UserServiceImplTests` or `UserControllerTests`. Run `.\mvnw.cmd test` before opening a pull request. Add focused tests for new service logic, repository queries, validation rules, and security-sensitive behavior.

## Commit & Pull Request Guidelines

Recent commits use concise subjects, including scoped feature messages such as `feat(architecture) - Add structure to clean project`. Prefer imperative, scoped messages: `feat(user) - Add user lookup endpoint` or `fix(security) - Restrict actuator access`.

Pull requests should include a short description, test results, linked issues when applicable, and screenshots or HTTP examples for API-facing changes. Mention new migrations, configuration keys, or security behavior explicitly.

## Security & Configuration Tips

Do not commit secrets or environment-specific credentials. Keep local overrides outside version control, and document required properties when adding external integrations.
