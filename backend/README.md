# MEDISTA — Backend

REST API for MEDISTA-V2, the medical attention management system of
Instituto Superior Universitario TEC Azuay. Modular monolith built with
Java 21 and Spring Boot 3.3.

## Table of Contents
1. Overview
2. Tech Stack
3. Architecture
4. Project Structure
5. Prerequisites
6. Getting Started
7. Configuration
8. Database & Migrations
9. Running Tests
10. API Documentation
11. Security
12. Observability
13. Build & Docker
14. Conventions
15. Troubleshooting
16. Related Documentation

---

## 1. Overview
What the backend does, in 3–4 lines. Mention the 8 modules (M1–M8) and
the clients it serves (Angular web app, Kotlin Android app).

## 2. Tech Stack
Table: Java 21 | Spring Boot 3.3 | Spring Security + JWT (RS256) |
PostgreSQL 16 | Redis 7 | Flyway | Hibernate Envers | MapStruct |
JasperReports | Bucket4j | FCM | WebSocket/STOMP | ArchUnit | JUnit 5

## 3. Architecture
- Module-first modular monolith with `api/` and `internal/` isolation
- Rule enforced by ArchUnit tests
- Short module table: M1 patient, M2 medicalattendance, M3 referral,
  M4 clinicalhistory, M5 dashboard, M6 notification, M7 security,
  M8 administration
- Link to the full design docs (don't duplicate them)

## 4. Project Structure
Condensed tree: config/, common/, exception/, one folder per module,
resources/db/migration, resources/reports, test/architecture.

## 5. Prerequisites
JDK 21, Maven, Docker + Docker Compose, (PostgreSQL/Redis via Compose).

## 6. Getting Started
1. Clone the repo
2. Start dependencies (`docker compose up -d postgres redis`)
3. Configure environment variables (see §7)
4. `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev`
5. Verify: health endpoint + Swagger UI URL

## 7. Configuration
- Spring profiles: dev / prod / test
- Table of environment variables (name, description, required, default)
- Where JWT keys and FCM credentials are expected (path only, never values)
- `.env.example` reference

## 8. Database & Migrations
- Flyway location and naming (`V1__*.sql`)
- How to add a migration
- Rule: never edit an applied migration
- Envers audit tables (`*_aud`)

## 9. Running Tests
- Unit, integration, architecture (ArchUnit)
- Commands for each
- Coverage report (if you use JaCoCo)

## 10. API Documentation
- Swagger/OpenAPI URL (dev only)
- Link to the API contracts doc
- Standard error format (`ErrorResponse`, `ValidationErrorResponse`)
  and the status-code table

## 11. Security
- JWT auth flow (summary), roles
- Rate limiting, audit log (HMAC-SHA256)
- LOPDP / MSP compliance note
- How to report a vulnerability

## 12. Observability
Actuator endpoints, Prometheus metrics, logging conventions.

## 13. Build & Docker
- `./mvnw clean package`
- Dockerfile usage
- Link to the deployment docs (Docker Compose + Nginx)

## 14. Conventions
- Class naming table (Xxx, XxxService, XxxServiceImpl, ...)
- Package naming (`medicalattendance`, not `medical-attendance`)
- Conventional Commits examples
- Branch strategy

## 15. Troubleshooting
Common errors (port 8080 in use, Flyway checksum mismatch,
Redis connection refused, invalid JWT key).

## 16. Related Documentation
Links to docs/ (ERS, data model, API contracts, sequence diagrams).