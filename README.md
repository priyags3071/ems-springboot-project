# 🏢 Employee Management System

A full-stack **Employee Management System** built with **Java Spring Boot** and a responsive vanilla JS frontend. Designed as a portfolio project demonstrating real-world backend architecture, RESTful API design, and clean UI implementation.

---

## 🛠️ Tech Stack

| Layer      | Technology                              |
|------------|-----------------------------------------|
| Language   | Java 17                                 |
| Framework  | Spring Boot 3.2                         |
| ORM        | Spring Data JPA / Hibernate             |
| Database   | H2 (in-memory, dev) / MySQL (prod-ready)|
| Build Tool | Maven                                   |
| Frontend   | HTML5, CSS3, Vanilla JS                 |
| Validation | Jakarta Bean Validation                 |

---

## ✨ Features

- **CRUD Operations** — Create, Read, Update, Delete employees
- **Real-time Search** — Search across name, email, department, position
- **Department Filter** — Filter employees by department
- **Status Management** — Active / Inactive / On Leave tracking
- **Dashboard Stats** — Live workforce metrics
- **Input Validation** — Server-side + client-side validation
- **Duplicate Detection** — Email uniqueness enforced
- **H2 Console** — Browse the database at `/h2-console`
- **Seed Data** — 10 demo employees pre-loaded on startup

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Run the Application
```bash
git clone https://github.com/yourusername/employee-management-system.git
cd employee-management-system
mvn spring-boot:run
```

Open your browser at: **http://localhost:8080**

H2 Console: **http://localhost:8080/h2-console**
- JDBC URL: `jdbc:h2:mem:emsdb`
- Username: `sa` | Password: *(leave blank)*

---

## 📡 REST API Reference

Base URL: `http://localhost:8080/api/employees`

| Method | Endpoint              | Description               |
|--------|-----------------------|---------------------------|
| GET    | `/`                   | Get all employees         |
| GET    | `/{id}`               | Get employee by ID        |
| POST   | `/`                   | Create new employee       |
| PUT    | `/{id}`               | Update employee           |
| DELETE | `/{id}`               | Delete employee           |
| GET    | `/?search=keyword`    | Search employees          |
| GET    | `/?department=name`   | Filter by department      |
| GET    | `/departments`        | List all departments      |
| GET    | `/stats`              | Get dashboard statistics  |

### Example: Create Employee
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Doe",
    "email": "jane.doe@company.com",
    "department": "Engineering",
    "position": "Software Engineer",
    "salary": 85000,
    "joinDate": "2024-01-15",
    "status": "ACTIVE"
  }'
```

---

## 🏗️ Project Structure

```
src/main/java/com/ems/
├── EmsApplication.java          # Spring Boot entry point
├── model/
│   └── Employee.java            # JPA Entity with validation
├── repository/
│   └── EmployeeRepository.java  # Spring Data JPA repository
├── service/
│   └── EmployeeService.java     # Business logic layer
├── controller/
│   └── EmployeeController.java  # REST API endpoints
└── exception/
    └── ResourceNotFoundException.java

src/main/resources/
├── application.properties       # App configuration
├── data.sql                     # Seed data
└── static/
    └── index.html               # SPA frontend
```

---

## 📐 Architecture

```
Frontend (HTML/CSS/JS)
        ↕ REST (JSON)
EmployeeController  ← Handles HTTP, validation
        ↕
EmployeeService     ← Business logic, transactions
        ↕
EmployeeRepository  ← Spring Data JPA
        ↕
H2 / MySQL Database
```

---

## 👤 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [yourlinkedin](https://linkedin.com/in/yourlinkedin)
