# Car Import Service

## About the project
Backend REST application for managing cars and importing car data from CSV files.

The project includes authentication and authorization with JWT, scope-based access control, and API documentation with Swagger.

## Live Demo
- [Swagger UI](https://car-import-service.onrender.com/swagger-ui/index.html)

You can test public endpoints directly from Swagger.

- ### Example
1. Get all makes:
GET /api/v1/makes

#### Pageable example
```json
{
  "page": 0,
  "size": 2,
  "sort": ["name,asc"]
}
```

2. Find car by id:
GET /api/v1/cars/{id}

Example ID:
00000000-0000-0000-0000-000000000021

```md
## Authentication

Protected endpoints require a JWT Bearer token from Auth0.

Example:

Authorization: Bearer <your_token>
```

### Live Status
⚠️ The application is hosted on Render (free tier), so the first request may take up to 30–60 seconds due to cold start.

## Features
- REST API for managing cars
- CSV import for bulk car creation
- Filtering and pagination for car search
- Authentication with Auth0
- Authorization based on JWT scopes
- Swagger / OpenAPI documentation
- Role and permission-based access to protected endpoints
- Database integration with PostgreSQL
- Layered architecture with DTOs and service layer

## Tech Stack
- Java
- Spring Boot
- Spring Security
- JWT
- Auth0
- Hibernate / JPA
- PostgreSQL
- Swagger / OpenAPI
- JUnit / Mockito

## Security
The application uses JWT-based authentication and validates:
- token issuer
- token audience
- access scopes

Examples of protected scopes:
- `cars:create`
- `cars:update`
- `cars:delete`

Public GET endpoints are available for reading car data, while modifying operations require authorization and proper scopes.

## API Features
- Get all cars with filtering and pagination
- Get car by ID
- Create a new car
- Update car by ID
- Delete car by ID
- Import cars from CSV file

## Architecture
The application is built using a layered architecture:
- Controller layer
- Service layer
- Repository layer
- DTO layer

## API Documentation
Swagger UI is available after starting the application locally.

## What I learned
- Building secured REST APIs with Spring Security
- Working with Auth0 and JWT-based authentication
- Implementing scope-based authorization
- Importing structured data from CSV files
- Designing filtering and pagination for REST endpoints
- Documenting APIs with Swagger

## Quick Start (Docker)

1. Clone the repository
```bash
git clone https://github.com/Neotrazymiy/car-import-service.git
cd car-import-service
```

2. Start PostgreSQL using Docker:
```bash
docker-compose up -d
```

3. Run the application:
```bash
mvn spring-boot:run
```

4. Open Swagger UI:
http://localhost:8080/swagger-ui/index.html

