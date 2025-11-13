# Smart Appointment & Resource Management Platform

A multi-tenant platform to manage appointments, staff schedules, and shared resources (e.g., rooms, equipment, vehicles). Built with Spring Boot, Vaadin (Java UI), and in-memory demo backend with role-secured dashboards.

## Core Features
- User, role, and organization (tenant) management
- Appointment booking, staff scheduling
- Resource reservation and conflict detection
- Role-based dashboards: Admin, Staff, Client, Super Admin
- In-memory authentication with Spring Security

## Technology Stack
- Java 21+, Spring Boot 3.x, Vaadin, Spring Security
- Maven (modular multi-project)

## Structure
- `smart-platform-parent`: Maven multi-module root
  - `user-service`: Handles user management, registration, authentication, role dashboards
  - `appointment-service`, `billing-service`, `notification-service`: Service modules (boilerplate)

## Setup & Usage
1. `cd smart-platform-parent/user-service`
2. `mvn spring-boot:run`
3. Go to [http://localhost:8080](http://localhost:8080)
4. Log in using:
   - admin / admin (ADMIN)
   - staff / staff (STAFF)
   - client / client (CLIENT)
   - super / super (SUPER_ADMIN)
   - (Or register new CLIENT users via the /register page)
5. Only dashboards matching your user's roles will be visible and accessible.
