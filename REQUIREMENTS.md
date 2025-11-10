# System Requirements for Smart Appointment & Resource Management Platform

## Developer/Build Prerequisites

- **Java JDK 21** or later
  - Required for compiling and running backend and Vaadin UI
  - Set `JAVA_HOME` environment variable to your JDK path
- **Apache Maven 3.9+**
  - Used for building and managing dependencies
  - Make sure `mvn` is available in your system PATH
- **Git** (for version control)
- **Internet Connection**
  - For automatic Maven dependency downloading

## Optional for Full Local Development
- **PostgreSQL Database** (for production/reliable dev/testing)
- **Docker** and **Docker Compose**
  - To easily spin up databases, caches, and more
- **Redis**
  - (Future) For Caching and Session management
- **(Planned) Kafka or RabbitMQ**
  - For event/messaging features
- **Modern web browser** (Chrome, Edge, Firefox, etc.)
  - To access and test the Vaadin Java UI

## Directory Structure

- `smart-platform-parent/`: The Maven parent containing all modules (services)
- Each service module (user-service, appointment-service, etc.) contains its own source and `pom.xml`

---

## How to Check Java/Maven Setup

Open a command prompt or terminal and run:

```
java -version
javac -version
mvn -version
```
