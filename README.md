# Syntex Outlet Store (SYOS)

A multi-user, multi-tier, client-server retail system built using Java EE and adhering to Clean Architecture principles. This system replaces the original single-user command-line interface with a modern, scalable, and concurrent solution, enabling both employees and customers to interact via a user-friendly GUI.

---

## 🧾 Overview

SYOS was designed to:
- Support multiple users simultaneously (cashiers and online customers)
- Streamline point-of-sale operations
- Provide real-time inventory and billing updates
- Ensure maintainability via Clean Architecture

---

## 🧠 Key Features

### 👥 Multi-User Support
- Concurrent access for employees and customers
- Session-based authentication and user-specific dashboards

### 🖥️ Graphical User Interface (GUI)
- Responsive JSP-based interface
- Login, registration, catalog, cart, checkout, billing, and admin dashboards

### 🧱 Clean Architecture
- **Client Tier:** JSPs for user interaction  
- **Business Logic Tier:** Servlets and services  
- **Data Tier:** DAOs for database interaction

### 🔄 Concurrency & Scalability
- Thread pools with blocking queues for login, checkout, and employee operations
- CompletableFutures for non-blocking execution

---

## 🛠️ Technologies Used

- Java EE (Servlets, JSP)
- JDBC
- HTML/CSS/JS (Fetch API, jQuery)
- ThreadPoolExecutor, LinkedBlockingQueue
- Postman (for automated testing)
- JUnit, Mockito (for unit testing)

---

## 🧪 Testing

### ✔️ Unit Testing
- 74% code coverage using JUnit & Mockito
- Tests for servlets, services, and DAOs

### 🤖 Automated Client Testing
- Login, product management, category updates, and checkout flow tested via Postman with 50+ iterations

---

## 🔐 Authentication

- Role-based login system for `employee` and `customer`
- Session management and role-specific redirections

---

## 🗃️ Architecture

