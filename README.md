# PYQ Portal — Academia Full-Stack Project

A full-stack **Previous Year Question Paper Portal** for IIITA students to browse, upload, and download question papers, enroll in courses, and manage subject subscriptions.

---

## Tech Stack

### Backend
- **Java 21** + **Spring Boot 3.2.5**
- **PostgreSQL** via Supabase (connection pooler)
- **Cloudinary** — PDF storage
- **Apache Kafka** — async PDF indexing pipeline
- **Elasticsearch** — full-text paper search
- **Redis** — response caching
- **Apache PDFBox** — PDF text extraction
- **JWT** (JJWT 0.12.3) — stateless authentication
- **Spring Security 6** — role-based access control
- **Springdoc OpenAPI** — Swagger UI at `/swagger-ui.html`
- **Lombok** + **Spring Data JPA**

### Frontend
- **Angular 18**
- **Bootstrap 5**
- **TypeScript**
- JWT interceptor for authenticated requests

---

## Features

### Authentication
- IIITA college email (`@iiita.ac.in`) only
- **Sign In** — validates email + name against DB
- **New User** — registers on first use (no password)
- Role-based access: `STUDENT` / `ADMIN`

### Question Papers
- Browse papers with filters: branch, exam type, year, free-text search
- Download PDFs (Cloudinary-hosted)
- Upload papers (Admin only) — PDF stored in Cloudinary, indexed via Kafka → Elasticsearch
- Duplicate detection via MD5 hash
- Admin can delete papers

### Courses
- Admin creates courses (title, description, weeks, lessons, price, image)
- Students browse and enroll / unenroll
- Enrolled count shown per course
- My Courses page for enrolled students

### Subscriptions
- Students subscribe to subjects
- Manage subscriptions from the Subscriptions page

### Admin Panel (`/admin/papers`)
- **Papers tab** — view all papers, delete, paginate
- **Users tab** — change user roles (STUDENT ↔ ADMIN)
- **Courses tab** — create and delete courses

---

## Project Structure

```
AcademiaProject/
├── Backend/backend/
│   └── src/main/java/com/pyqportal/
│       ├── auth/          # JWT auth, login, register
│       ├── paper/         # Paper entity, upload, search, download
│       ├── course/        # Course entity, enrollment
│       ├── subscription/  # Subject subscriptions
│       ├── user/          # User entity, role management
│       ├── search/        # Elasticsearch documents & service
│       ├── indexworker/   # Kafka consumer — PDF indexing
│       ├── storage/       # Cloudinary upload service
│       ├── config/        # Security, Redis, Kafka, Elasticsearch
│       ├── exception/     # Global exception handler
│       └── common/        # Shared DTOs (PageResponse)
│
└── Frontend/src/app/
    ├── Components/
    │   ├── login/           # Sign In / New User toggle
    │   ├── paper-dashboard/ # Browse & download papers
    │   ├── upload-papers/   # Upload PDF (admin only)
    │   ├── admin-papers/    # Admin panel (papers/users/courses)
    │   ├── courses/         # Course catalog + enroll
    │   ├── my-courses/      # Enrolled courses
    │   ├── subscriptions/   # Subject subscriptions
    │   └── cards/           # Dashboard home
    └── Services/
        ├── auth/            # AuthService, guards, JWT interceptor
        ├── paper_dashboard/ # Paper API service
        ├── course/          # Course API service
        └── subscription/    # Subscription API service
```

---

## Local Setup

### Prerequisites
- Java 21
- Node.js 18+
- Docker (for Kafka, Redis, Elasticsearch)
- Supabase account (PostgreSQL)
- Cloudinary account

### 1. Clone

```bash
git clone https://github.com/Dheeraj6392/AcademiaProject.git
cd AcademiaProject
```

### 2. Start Docker services

```bash
cd Backend/backend
docker compose up -d
```

This starts Kafka, Zookeeper, Redis, and Elasticsearch.

### 3. Configure secrets

Create `Backend/backend/src/main/resources/application-local.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://<supabase-pooler-host>:6543/postgres?sslmode=require
    username: postgres.<project-ref>
    password: <your-password>

cloudinary:
  cloud-name: <your-cloud-name>
  api-key: <your-api-key>
  api-secret: <your-api-secret>

jwt:
  secret: <min-32-char-secret-key>
  expiration: 86400000
```

> `application-local.yml` is gitignored — never commit secrets.

### 4. Run Backend

```bash
cd Backend/backend
chmod +x mvnw
./mvnw spring-boot:run
```

Backend starts at `http://localhost:8080`.
Swagger UI: `http://localhost:8080/swagger-ui.html`

### 5. Run Frontend

```bash
cd Frontend
npm install
ng serve
```

Frontend starts at `http://localhost:4200`.

---

## API Endpoints

### Auth
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | `/api/v1/auth/register` | Public |
| POST | `/api/v1/auth/login` | Public |
| GET  | `/api/v1/auth/me` | Authenticated |

### Papers
| Method | Endpoint | Access |
|--------|----------|--------|
| GET    | `/api/v1/papers` | Authenticated |
| POST   | `/api/v1/papers` | Admin |
| GET    | `/api/v1/papers/{id}/download` | Authenticated |
| DELETE | `/api/v1/papers/{id}` | Admin |

### Courses
| Method | Endpoint | Access |
|--------|----------|--------|
| GET    | `/api/v1/courses` | Authenticated |
| POST   | `/api/v1/courses` | Admin |
| GET    | `/api/v1/courses/my` | Authenticated |
| POST   | `/api/v1/courses/{id}/enroll` | Authenticated |
| DELETE | `/api/v1/courses/{id}/enroll` | Authenticated |
| DELETE | `/api/v1/courses/{id}` | Admin |

### Subscriptions
| Method | Endpoint | Access |
|--------|----------|--------|
| GET    | `/api/v1/subscriptions` | Authenticated |
| POST   | `/api/v1/subscriptions` | Authenticated |
| DELETE | `/api/v1/subscriptions/{id}` | Authenticated |

### Users (Admin)
| Method | Endpoint | Access |
|--------|----------|--------|
| GET    | `/api/v1/users` | Admin |
| PATCH  | `/api/v1/users/{id}/role` | Admin |

---

## Database Tables (auto-created by Hibernate)

| Table | Description |
|-------|-------------|
| `users` | Email, name, role |
| `papers` | Title, subject, branch, year, exam type, file URL, MD5 hash |
| `courses` | Title, description, weeks, lessons, price, image URL |
| `enrollments` | User ↔ Course join (unique constraint) |
| `subscriptions` | User ↔ Subject |

---

## Making Yourself Admin

1. Register via the app
2. Go to **Supabase → Table Editor → users**
3. Change your `role` column to `ADMIN`
4. Log out and sign in again (refreshes JWT)

---

## Contribution

Pull requests are welcome. Feel free to open issues for bugs or feature requests.

## Support

If you find this project useful, give it a ⭐ on GitHub!
