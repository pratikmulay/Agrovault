# AgroVault
### Cold Storage Discovery & Booking Platform

> A production-grade backend system that connects farmers with cold storage facilities across Maharashtra — enabling real-time discovery, capacity booking, and temperature monitoring at scale.

[![Java](https://img.shields.io/badge/Java-25-orange?style=flat-square&logo=java)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org)
[![Redis](https://img.shields.io/badge/Redis-Caching-red?style=flat-square&logo=redis)](https://redis.io)
[![JWT](https://img.shields.io/badge/Auth-JWT-black?style=flat-square&logo=jsonwebtokens)](https://jwt.io)
[![Maven](https://img.shields.io/badge/Build-Maven-purple?style=flat-square&logo=apachemaven)](https://maven.apache.org)

---

## The Problem

Over **60% of agricultural produce** in India is wasted post-harvest due to lack of accessible cold storage infrastructure. Farmers have no reliable way to discover available cold storage near them, and storage owners have no platform to manage bookings and capacity efficiently.

**AgroVault** solves this by acting as a centralized platform — farmers discover and book storage, owners manage capacity and monitor temperature, and admins oversee the entire ecosystem.

---

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        REST API Layer                        │
│         AuthController  │  StorageController                │
│         BookingController  │  TemperatureLogController      │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                      Service Layer                           │
│    AuthService │ StorageService │ BookingService             │
│    TemperatureLogService │ NotificationService               │
└──────────┬───────────────────────────────┬──────────────────┘
           │                               │
┌──────────▼──────────┐       ┌────────────▼────────────────┐
│   PostgreSQL DB      │       │        Redis Cache           │
│  users │ cities      │       │  storages:city:{name}        │
│  storages │ bookings │       │  storage:availability:{id}   │
│  temperature_logs    │       │  all-storages                │
└─────────────────────┘       └─────────────────────────────┘
```

---

## Key Technical Highlights

### Concurrency-Safe Booking Engine
Booking creation is fully protected against race conditions. Uses **JPA Optimistic Locking** (`@Version`) combined with `@Transactional` to guarantee that capacity check, deduction, booking record creation, and storage update happen atomically — with full rollback on any failure step.

```
Request → Check Capacity → Deduct Capacity → Create Booking → Update Storage
                                    ↓ any failure
                              Full Rollback ← ← ← ← ← ←
```

### 3-Tier Redis Caching Strategy
Reduces PostgreSQL load by up to **80%** on repeat queries through targeted caching with 10-minute TTL and precise cache eviction on write operations.

| Cache Key | Purpose | Evicted When |
|---|---|---|
| `storages:city:{cityName}` | City-based storage discovery | New storage added or updated |
| `storage:availability:{id}` | Individual storage capacity | Booking created or cancelled |
| `all-storages` | Full storage listing | Any storage modification |

### Event-Driven Temperature Monitoring
Background scheduler runs every **60 seconds**, scanning all 20 storage facilities against their defined temperature thresholds. On breach detection, a `TemperatureAlertEvent` is published asynchronously via Spring's event system — decoupled from the main request thread.

```
Scheduler (60s) → Fetch Latest Logs → Compare Thresholds
                                              ↓ breach detected
                              Publish TemperatureAlertEvent
                                              ↓
                              NotificationService (Async Listener)
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 3.x |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Authentication | Spring Security + JWT |
| Caching | Redis |
| Event System | Spring ApplicationEvent |
| Scheduler | Spring @Scheduled |
| Build Tool | Maven |
| Password Hashing | BCrypt |
| Frontend | React, Tailwind CSS, Axios |

---

## API Overview

### Authentication
```
POST   /auth/register       Register as FARMER, STORAGE_OWNER or ADMIN
POST   /auth/login          Login and receive JWT token
GET    /auth/me             Get current logged-in user details
```

### Storage
```
GET    /storages                    List all cold storages
GET    /storages/city?city=Nashik   Discover storages in a city (FARMER)
GET    /storages/owner              Owner's registered storages (STORAGE_OWNER)
POST   /storages                    Register new storage (STORAGE_OWNER)
PUT    /storages/{id}               Update storage details (STORAGE_OWNER)
PUT    /storages/{id}/capacity      Update available capacity
```

### Bookings
```
POST   /bookings              Create a new booking (FARMER)
GET    /bookings/{id}         Get booking details
GET    /bookings/user         Farmer's booking history (FARMER)
GET    /bookings/all          All bookings across platform (ADMIN)
PUT    /bookings/{id}/status  Update booking status (ADMIN)
```

### Temperature Monitoring
```
POST   /temperature-logs      Log temperature reading (STORAGE_OWNER)
GET    /cities                List all available cities
```

---

## Role-Based Access Control

| Feature | FARMER | STORAGE_OWNER | ADMIN |
|---|---|---|---|
| Discover storages by city | Yes | No | Yes |
| Create booking | Yes | No | No |
| View own bookings | Yes | No | No |
| Register cold storage | No | Yes | No |
| Log temperature | No | Yes | No |
| View all bookings | No | No | Yes |
| Update booking status | No | No | Yes |

---

## Database Schema

```
users ──────────────────────────────────────────────────────┐
  id (UUID) │ name │ email │ password │ role │ created_at    │
                                                             │
cities ──────────────────────────────────────────┐          │
  id │ name │ latitude │ longitude               │          │
                                                 │          │
storages ─────────────────────────────────────── ┼ ─────────┤
  id (UUID) │ name │ city_id (FK) │ owner_id (FK)│          │
  total_capacity │ available_capacity │ version  │          │
  temperature_min │ temperature_max               │          │
                                                 │          │
bookings ────────────────────────────────────────┼──────────┤
  id (UUID) │ farmer_id (FK) │ storage_id (FK)   │          │
  produce_type │ quantity │ start_date │ end_date │          │
  status │ created_at                            │          │
                                                 │          │
temperature_logs ────────────────────────────────┘          │
  id │ storage_id (FK) │ temperature │ humidity │ recorded_at│
```

---

## Seeded Demo Data

The application auto-seeds the database on first startup with realistic Maharashtra data:

| Entity | Count | Details |
|---|---|---|
| Cities | 10 | Nashik, Pune, Nagpur, Chh. Sambhajinagar, Ahmednagar, Solapur, Latur, Satara, Beed, Sangamner |
| Users | 20 | 1 Admin, 7 Storage Owners, 12 Farmers |
| Cold Storages | 20 | 2 per city across 3 temperature zones |
| Bookings | 12 | Mixed statuses — PENDING, CONFIRMED, COMPLETED, CANCELLED |
| Temperature Logs | 40 | 2 per storage, 5 intentional breach logs for monitoring demo |

### Temperature Zones
| Zone | Range | Suitable For |
|---|---|---|
| Frozen | -5°C to 5°C | Meat, Fish, Ice Cream |
| Chilled | 2°C to 8°C | Grapes, Pomegranate, Dairy |
| Cool Room | 8°C to 15°C | Onions, Tomatoes, Potatoes |

---

## Quick Start

### Prerequisites
- Java 25
- PostgreSQL running on `localhost:5432`
- Redis running on `localhost:6379`
- Maven

### Run
```bash
git clone https://github.com/pratikmulay/agrovault.git
cd agrovault
mvn spring-boot:run
```

Database tables are auto-created and seeded on first run.

### Test Accounts
| Role | Email | Password |
|---|---|---|
| Admin | admin@agrovault.com | admin123 |
| Storage Owner | owner1@agrovault.com | owner123 |
| Farmer | farmer1@agrovault.com | farmer123 |

### Sample Request
```bash
# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "farmer1@agrovault.com", "password": "farmer123"}'

# Discover storages in Nashik (use token from login)
curl -X GET "http://localhost:8080/storages/city?city=Nashik" \
  -H "Authorization: Bearer <your_token>"
```

---

## Project Structure

```
src/main/java/com/agrovault/
├── controller/        REST API endpoints
├── service/           Business logic interfaces
│   └── impl/          Service implementations
├── repository/        Spring Data JPA repositories
├── entity/            JPA entity classes
├── dto/
│   ├── request/       Incoming request DTOs
│   └── response/      Outgoing response DTOs
├── security/          JWT filter, SecurityConfig, UserDetailsService
├── config/            Redis config, DataSeeder, LoggingFilter
├── event/             BookingCreatedEvent, TemperatureAlertEvent
├── scheduler/         Temperature monitoring scheduler
└── exception/         Custom exceptions, GlobalExceptionHandler
```

---

## Standard API Response Format

Every endpoint returns a consistent envelope:

```json
{
  "success": true,
  "message": "Storages fetched successfully",
  "data": [ ... ]
}
```

Error response:
```json
{
  "success": false,
  "message": "Insufficient storage capacity",
  "data": null
}
```

---

## Error Handling

| Exception | HTTP Status | Scenario |
|---|---|---|
| ResourceNotFoundException | 404 | Entity not found |
| BadRequestException | 400 | Invalid input |
| InsufficientCapacityException | 409 | Booking exceeds available capacity |
| ForbiddenException | 403 | Unauthorized resource access |
| OptimisticLockingFailureException | 409 | Concurrent booking conflict |
| MethodArgumentNotValidException | 422 | Validation failure |

---

*Built with Java 25 · Spring Boot 3.x · PostgreSQL · Redis*
