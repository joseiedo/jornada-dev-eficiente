# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Build and Test
- `./mvnw clean test` - Clean build and run all tests
- `./mvnw clean compile` - Clean build without running tests
- `./mvnw test` - Run tests only
- `./mvnw spring-boot:run` - Run the application locally
- `./mvnw clean package` - Build JAR file
- `./mvnw clean install` - Build and install to local Maven repository

### Single Test Execution
- `./mvnw test -Dtest=ApplicationTests` - Run specific test class
- `./mvnw test -Dtest=ApplicationTests#healthCheck` - Run specific test method

## Project Architecture

### Technology Stack
- **Spring Boot 3.5.4** with Java 21
- **Spring Data JPA** for database persistence
- **H2 Database** for development/testing (in-memory)
- **JUnit 5** with MockMvc for testing
- **Maven** build system with wrapper

### Package Structure
```
br.com.joseiedo.desafioyfood/
├── Application.java              # Main Spring Boot application
└── controllers/                  # REST API controllers
    └── HealthCheck.java          # Basic health endpoint
```

### Current Implementation
- Basic Spring Boot application with health check endpoint (`/health`)
- H2 in-memory database configured but no entities defined yet
- MockMvc test setup for API testing
- Ready for implementing food delivery domain features

### Development Patterns
- Follow **Controller-Service-Repository** pattern
- Use `@RestController` for API endpoints
- Use Spring Data JPA for data persistence
- Write tests using `@SpringBootTest` and `MockMvc`
- Package structure follows domain-driven organization

### Database Configuration
- H2 in-memory database runs automatically
- No additional configuration needed for development
- JPA/Hibernate handles schema generation

### Expected Domain Features
Based on project name "yfood" (food delivery challenge):
- Food catalog management
- Order processing
- Coupon/discount system
- Purchase workflow
- Customer management
- Vamos implementar uma aplicação específica para processar pagamentos relativos a um aplicativo famoso de pedido de comida, o YFood :P.

A ideia é que nossa aplicação liste as possíveis formas de pagamento para uma pessoa dado um restaurante e depois que a gente processe o pagamento em função da escolha final da pessoa.

Esse é um serviço crucial no contexto do YFood. É a conclusão da experiência do usuário e precisamos atender o máximo de pedidos que for possível sob as condições mais extremas.
- There is no need to use a service class.
Foque em coesão, código que lida com o estado de um objeto, precisa estar no objeto.
Testes não devem mockar banco de dados.
Testes precisam focar em pontos extremos (edge cases)
Códigos de mapeamentos DTO -> entidade precisam ficar na classe do DTO
Códigos de mapeamentos entidade -> DTO precisam ficar na classe do DTOn
- Use @Deprecated in all empty constructors that are needed because of JPA. 
Add the used annotations in the fields also in the constructor parameters (just for documentation, it doesnt matter if the validation will ocurr)
- do not automatically add getters and setters that are not being used
- For tests, avoid mocks. To create entities data, use the object mother pattern, ex:
UsuarioFactory is a static class with a createWithFormaDePagamentoCartao method and many others.
- In this project I want to simulate 2 services:
pagamentos
and pedidos.

both should communicate with each other using HTTP