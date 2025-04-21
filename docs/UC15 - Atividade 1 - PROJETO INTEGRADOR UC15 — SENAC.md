# PROJETO INTEGRADOR UC15 — SENAC

# TECHNICAL DOCUMENTATION

**João Carlos Roveda Ostrovski**</br>
**System name**: OV-FMA \- Ostro Veda Finance Management Application (Aplicação de Gerenciamento Financeiro)  
**Versão**: *

# Project description

Financial management; income, expenditure, tracking; financial budget and investment registration.  
Services are used and deployed in four modules:

1. **Gateway**: all requests need to be directed to the gateway for proper authorization; services will deny access otherwise.  
2. **User**: registration, user data querying and updates (new password, email, username, etc.),  
3. **Authentication**: user authentication;  
4. **Finance**: Finance Manager API for registering, querying and updating financial information;  
   1. Requires a registered and authenticated user (JWT).

# Services and structure

* Spring Boot;  
  * Dependency injection;  
* Spring Data JPA;  
  * Hibernate ORM;  
* Spring Security;  
  * Endpoints permission management;  
* H2 Database;  
  * dev profile;  
* MySQL JDBC;  
  * Driver for database connection;  
* Apache Kafka;  
  * Message system for asynchronous consumer/producer communications.  
* Lombok;  
  * Boilerplate code reduction using annotations;  
* Jakarta EL, Hibernate Validator and Jakarta Validation API;  
  * Annotation validation;  
* Swagger (OpenAPI);  
  * API documentation;  
* Testing;  
  * Mockito;  
  * JUnit;

## Gateway:

* Spring Data Redis;  
  * Cache NoSQL key-value;  
* Spring Reactive Gateway  
  * Services entry point;  
* OAuth2 Resource Server  
  * JWT;  
  * RBAC;

### Functionalities:

* Routing for the desired services;  
* Security of services calls.

## User:

* Spring Web (MVC)

### Functionalities:

* Registration;  
* User data updates;

## Authentication:

* Spring Web (MVC)

Requires a registered user from the user service.

### Functionalities:

* Authentication;

## Finance:

* Spring Web (MVC)

Requires a registered and authenticated user from the user service.

### Functionalities:

* Registering of:  
  * Income;  
  * Expenditures;  
  * Investments;  
* Query and updates of registered data;

# Non-functional

* Programming Language: Java;  
* Database: MySQL;  
* OS: Windows.

# Functional

## Users:

* Register;  
* Authenticate;  
* Update and query their data;  
* Access the financial application for:  
  * Inserting and querying data.