# Requirements

## Essential Requirements

### Development Environment
- **Java Development Kit (JDK) 21 or newer**
  - Confirm with `javac -version`
- **Apache Maven 3.9+**
  - Confirm with `mvn -version`
- **Docker & Docker Compose**
  - Required for running Elasticsearch
  - Confirm with `docker -v` and `docker-compose -v`
- **Web Browser** (recommended: Chrome, Firefox, Edge)
- **No Node.js/npm required for Vaadin-based Java UI**

# How to Run
- Clone/download the repo
- Start Infrastructure (Elasticsearch):
  - `docker-compose up -d`
- Run the Application:
  - `cd smart-platform-parent/user-service`
  - `mvn spring-boot:run`
  - Open [http://localhost:8080](http://localhost:8080) in a browser
- Log in as:
  - admin/admin (ADMIN), staff/staff (STAFF), client/client (CLIENT), super/super (SUPER_ADMIN)

# Optional Tools
- Recommended IDE: IntelliJ IDEA, Eclipse, VS Code (with Java support)
- Git (for version tracking)
