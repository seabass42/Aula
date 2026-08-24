# Aula — Spanish tutoring portal

Spring Boot portal for a private Spanish tutor and their students. Students sign in,
work through practice problems, and see per-topic accuracy. Answers are graded with
Spanish-specific rules: an answer that is correct except for accent marks is treated
differently from one that is simply wrong.

## Stack

Java 21 · Spring Boot 3.3 · Spring Security · Spring Data JPA · PostgreSQL 16 ·
Flyway · Thymeleaf

## Running it

```bash
docker compose up -d          # Postgres on :5432
./mvnw spring-boot:run        # app on :8080
```

Open http://localhost:8080 and sign in as `student@example.com` / `changeme`.
There is also `tutor@example.com` / `changeme`.

Seed data is created by `DataSeeder`, which is annotated `@Profile("dev")` — it will
not run in production. **Delete or change those accounts before deploying anywhere
public.**

## How the pieces fit

```
  browser
     │
     ▼
  SecurityFilterChain ─── /login, /css, /js  → open
     │                    /tutor/**          → ROLE_TUTOR
     │                    everything else    → authenticated
     ▼
  Controller ── CurrentUser ──► User entity
     │
     ├── ProblemRepository.findNextUnmasteredFor(studentId)
     │      └─ picks a problem this student has never gotten right
     │
     ├── AnswerGrader.grade(expected, submitted)
     │      └─ CORRECT | MISSING_ACCENTS | INCORRECT
     │
     └── AttemptRepository.save(...)  → feeds the dashboard
```

`ddl-auto: validate` means Hibernate never creates or alters tables. Flyway owns the
schema; Hibernate only checks that the entities still match it and fails at startup
if they have drifted.

## Adding a schema change

Never edit a migration that has already run — Flyway records its checksum and will
refuse to start. Add a new file:

```
src/main/resources/db/migration/V2__add_help_requests.sql
```

## Not built yet

- Tutor UI for creating and editing problems (currently seeded only)
- Help requests: student submits a question or an essay, tutor replies
- Student self-signup (accounts are seeded by hand for now)
- Tests
