# Smart Appointment & Resource Management Platform

A multi-tenant appointment, scheduling, and resource management system. Built with Spring Boot, Vaadin, and a modular, scalable architecture.

## Features
- User & Role Management (Admin, Staff, Client, Super Admin)
- Appointment Booking & Staff Schedules
- Resource Reservation (Rooms, Equipment, etc.)
- Multi-Tenancy (organization/company isolation)
- Modular services: user-service, appointment-service, billing-service, notification-service
- Java Web UI with Vaadin (inside user-service)

## Technology Stack
- **Java 21**, **Spring Boot 3**, **Vaadin 24**
- Maven multi-module project
- PostgreSQL (recommended for prod)
- (Planned) Kafka/RabbitMQ, Redis, Elasticsearch, Stripe/PayPal, Docker

## Development Prerequisites
- Java JDK 21 or later
- Apache Maven 3.9+
- (Optional for DB/features) PostgreSQL, Redis, Docker Compose

## Setup & Run (Backend with Vaadin Web UI)

1. **Clone the repository & enter project root**
   ```sh
   cd "Smart Appointment & Resource Management Platform/smart-platform-parent"
   ```

2. **Build the full project:**
   ```sh
   mvn clean install
   ```

3. **Run the Vaadin Java UI (user-service):**
   ```sh
   cd user-service
   mvn spring-boot:run
   ```

4. **Open in your browser:**
   http://localhost:8080

Initial UI is a demonstration landing/login/dashboard page. Expand as needed.

---

## Directory Structure
```
smart-platform-parent/
  pom.xml
  user-service/
  appointment-service/
  billing-service/
  notification-service/
```

- `user-service` contains the web UI and user management logic.
- Add business logic/services as needed per module.

---

## Contributing & Extending
- Add new microservices as modules in the parent pom.
- Use Spring Boot starters and managed dependencies (see pom.xml).
- For frontend customization, Vaadin views are found in `user-service/src/main/java/com/smartplatform/user/views/`.

---

## License
Specify license here.
