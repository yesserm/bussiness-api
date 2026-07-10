# Local Docker Workflow

This project is a Maven multi-module Spring Boot monorepo. The root `compose.yaml`
is the supported local Docker entrypoint for Windows + WSL.

## Prerequisites

- Docker available inside WSL.
- Internet access the first time Docker builds images, so Maven can download dependencies.
- Java 25 and Maven 3.9.x locally if you want to run tests outside Docker.

## Validate

From Windows PowerShell in the project root:

```powershell
wsl docker compose config
wsl docker compose --profile all config
```

Run the test suite locally:

```powershell
mvn test
```

The Maven wrapper is configured for Maven 3.9.16, but the first wrapper run needs
network access to `repo.maven.apache.org`.

## Start The Stack

Infrastructure only:

```powershell
wsl docker compose up --build
```

Core application stack:

```powershell
wsl docker compose --profile core up --build
```

All services, including skeleton business services:

```powershell
wsl docker compose --profile all up --build
```

Stop and remove containers, networks, and local database volumes:

```powershell
wsl docker compose down -v
```

## Exposed Endpoints

- API Gateway: `http://localhost:8080/actuator/health`
- Eureka: `http://localhost:8761`
- Config Server: `http://localhost:8888/actuator/health`
- RabbitMQ Management: `http://localhost:15672` (`guest` / `guest`)

Most service ports are internal to the Compose network. The gateway routes to
`auth-service`, `user-service`, and `business-service` by container DNS name.

## Profiles

- No profile: RabbitMQ, Eureka Server, and Config Server.
- `core`: adds Postgres, auth-service, user-service, business-service, and api-gateway.
- `business`: adds the skeleton business services and their Postgres containers.
- `all`: starts core, business services, and the legacy monolith retained for migration reference.

## Notes

Each service has its own Postgres container and keeps H2 defaults for non-Docker
local runs. Compose enables Eureka registration and points config clients to
`http://config-server:8888`.

If WSL Docker exits containers with status `255` after successful startup logs,
inspect the Docker/WSL runtime first. The application logs should show whether
Spring Boot started before the runtime stopped the containers.
