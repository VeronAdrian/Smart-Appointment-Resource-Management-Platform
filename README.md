# Smart Appointment & Resource Management Platform

A comprehensive multi-tenant platform for managing appointments, staff schedules, and shared resources (e.g., rooms, equipment, vehicles). Built with Spring Boot, Vaadin (Java UI), Spring Security, and in-memory backend with full role-based access control and tenant isolation.

## 🎯 Core Features

### 1. User & Role Management
- **Roles**: Admin, Staff, Client, Super Admin (for multi-tenancy)
- **Authentication**: Spring Security with in-memory user storage
- **Authorization**: Role-based + resource-based access control
- **User Registration**: Self-service registration with automatic tenant assignment

### 2. Appointment & Scheduling System
- **Staff Availability**: Staff can define availability slots with date/time ranges
- **Real-time Booking**: Clients can book appointments in real-time
- **Conflict Detection**: Automatic prevention of overlapping reservations
- **Notifications**: Email + SMS notifications (mock, ready for Twilio integration)

### 3. Resource Management
- **Shared Resources**: Define resources (gym equipment, meeting rooms, vehicles, etc.)
- **Reservation System**: Priority-based reservation workflow (LOW, MEDIUM, HIGH, URGENT)
- **Approval Workflow**: Admin/Staff must approve all resource reservations
- **Usage Analytics**: Track resource usage, top resources, reservations by priority/status

### 4. Multi-Tenancy
- **Tenant Isolation**: Each company/organization has its own isolated data space
- **Discriminator Column Strategy**: Uses `tenantId` field in all entities (ready for Spring Data + Hibernate)
- **Centralized Super Admin Dashboard**: Manage all tenants from one interface
- **Automatic Data Isolation**: All queries automatically filter by current tenant context

## 🛠️ Technology Stack

- **Backend**: Java 21+, Spring Boot 3.2.5, Spring Security
- **UI**: Vaadin 24.3.6 (100% Java, no JavaScript required)
- **Build**: Maven (multi-module project)
- **Storage**: H2 In-Memory Database (JPA) & Elasticsearch (Docker)

## 📁 Project Structure

```
smart-platform-parent/
├── user-service/          # Main service with all features
│   ├── models/            # User, Role, Tenant, Appointment, Resource, etc.
│   ├── services/          # Business logic (in-memory storage)
│   ├── views/             # Vaadin UI pages
│   └── config/            # Spring Security configuration
├── appointment-service/    # (Reserved for future microservices split)
├── billing-service/       # (Reserved for future microservices split)
└── notification-service/  # (Reserved for future microservices split)
```

## 🚀 Quick Start

### Prerequisites
- JDK 21 or newer
- Maven 3.9+
- Web browser

### Setup & Run

1. **Start Infrastructure:**
   ```bash
   docker-compose up -d
   ```

2. **Navigate to user-service:**
   ```bash
   cd smart-platform-parent/user-service
   ```

3. **Build and run:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access the application:**
   - Open [http://localhost:8080](http://localhost:8080) in your browser
   - You'll be redirected to Spring Security login page

5. **Test Credentials:**
   - **Super Admin**: `super` / `super` (can manage all tenants)
   - **Admin**: `admin` / `admin` (full admin rights within tenant)
   - **Staff**: `staff` / `staff` (can create availability slots, approve reservations)
   - **Client**: `client` / `client` (can book appointments, reserve resources)
   - Or register new users via `/register` (assigned CLIENT role)

## 📍 Available Routes

### Public Routes
- `/` - Main landing page
- `/login` - Spring Security login (automatic redirect)
- `/register` - User registration

### Role-Based Dashboards
- `/admin-dashboard` - Admin dashboard (ADMIN only)
- `/staff-dashboard` - Staff dashboard (STAFF only)
- `/client-dashboard` - Client dashboard (CLIENT only)
- `/super-dashboard` - Super Admin dashboard (SUPER_ADMIN only)

### Appointment Features
- `/staff-availability` - Define availability slots (STAFF)
- `/client-booking` - Book appointments (CLIENT)

### Resource Features
- `/resource-management` - Create/manage resources (ADMIN/STAFF)
- `/resource-reservation` - Reserve resources with priority (All authenticated users)
- `/resource-approval` - Approve/reject reservations (ADMIN/STAFF)
- `/resource-analytics` - View usage analytics (ADMIN/SUPER_ADMIN)

### Multi-Tenancy
- `/tenant-management` - Manage tenants/organizations (SUPER_ADMIN only)

## 🔐 Security & Access Control

- **Spring Security**: All routes require authentication (except login/register)
- **Role-Based Access**: Views automatically check user roles and redirect unauthorized users
- **Tenant Isolation**: All data queries automatically filter by current tenant context
- **Super Admin**: Tenant-agnostic, can access all tenants

## 📊 Features in Detail

### Multi-Tenancy
- Each organization/company is a separate tenant
- Super Admin can create, activate, suspend, or delete tenants
- All users, appointments, resources are automatically isolated by tenant
- Discriminator column strategy (`tenantId`) - ready for database migration

### Appointment System
- Staff define availability with date/time ranges
- Clients see available slots and book in real-time
- Automatic conflict detection prevents double-booking
- Email/SMS notifications on booking (mock implementation)

### Resource Management
- Create resources with types, descriptions, max reservation hours
- Priority-based reservation system (URGENT > HIGH > MEDIUM > LOW)
- Approval workflow: All reservations require Admin/Staff approval
- Analytics dashboard shows usage statistics, top resources, status breakdowns

## 🔄 Future Enhancements (Ready for Implementation)

- **Database Integration**: Migrate from in-memory to PostgreSQL with Spring Data JPA
- **Real Email/SMS**: Integrate Twilio or similar for actual notifications
- **JWT Authentication**: Add JWT tokens for REST API access
- **OAuth2**: Google/Microsoft login integration
- **Payment Integration**: Stripe/PayPal for subscription billing
- **Microservices**: Split into separate services (appointment, billing, notification)
- **Event-Driven**: Add Kafka/RabbitMQ for async event handling

## 📝 Notes

- **In-Memory Storage**: All data is stored in memory (lost on restart). Perfect for demos and development.
- **Mock Notifications**: Email/SMS notifications print to console. Ready for real integration.
- **Plaintext Passwords**: Demo only! Use proper password encoding in production.
- **No Database Required**: Runs completely standalone for quick testing.
