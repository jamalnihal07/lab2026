# GoPro Backend (Java Spring Boot)

This is a minimal Spring Boot backend for the GoPro demo app. It uses a file-based JSON store located at `backend-data.json` for persistence (suitable for demos).

Requirements
- Java 17+
- Maven

Run locally

```bash
cd src/main/GoPro/backend
mvn spring-boot:run
```

APIs (examples)
- POST /api/login { email, password, type }
- GET /api/driver/{id}
- GET /api/rides
- GET /api/rides/active
- POST /api/rides { pickup, dropoff, fare, passengerName }
- POST /api/rides/{id}/start
- POST /api/rides/{id}/complete
- POST /api/driver/{id}/status { status }

Notes
- The server stores and updates `backend-data.json` in the backend folder. Keep backups if needed.
- This is a demo backend — not production hardened.
