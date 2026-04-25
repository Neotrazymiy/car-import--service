# Car Import Service

## About the project
Backend REST application for managing cars and importing car data from CSV files.

The project includes authentication and authorization with JWT, scope-based access control, and API documentation with Swagger.

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
- OAuth2 Resource Server
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

## Quick Start
1. Clone the repository
2. Configure `application.properties`
3. Set Auth0 configuration
4. Create and configure PostgreSQL database
5. Run the application with:

```bash
mvn spring-boot:run
