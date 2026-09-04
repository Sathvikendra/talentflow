# ADR-003: Database Migration Strategy

**Project:** TalentFlow
**Status:** Accepted
**Date:** 2026-09-05

---

## 1. Decision

TalentFlow will use **Flyway** for version-controlled database schema migrations.

Database schema changes must be introduced through Flyway migration scripts rather than manually modifying the database schema.

Hibernate/JPA will be used for object-relational mapping and persistence, but Hibernate will **not** be responsible for automatically creating or modifying the database schema in shared or production environments.

---

## 2. Context

TalentFlow is being developed by a two-person development team.

The application uses:

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* MySQL 8
* Flyway

Without a migration strategy, developers may make database changes manually using MySQL Workbench.

For example:

```text
Developer A
    ↓
Creates table manually in MySQL
    ↓
Works locally

Developer B
    ↓
Clones Git repository
    ↓
Table does not exist
    ↓
Application behaves differently
```

This creates environment drift.

The database schema therefore needs to become part of the application's version-controlled source code.

---

## 3. Migration Architecture

The database evolution process will be:

```text
Developer
    ↓
Create Flyway migration
    ↓
Git
    ↓
Pull / Clone
    ↓
Spring Boot
    ↓
Flyway
    ↓
MySQL
```

The migration files are committed to Git.

Each developer's database is updated by running the application with Flyway enabled.

---

## 4. Migration Location

Flyway migration scripts will be stored inside the Spring Boot application:

```text
backend/
└── talentflow-api/
    └── src/
        └── main/
            └── resources/
                └── db/
                    └── migration/
```

Example:

```text
db/
└── migration/
    ├── V1__initial_schema.sql
    ├── V2__add_notification_table.sql
    ├── V3__add_ai_assessment.sql
    └── V4__add_indexes.sql
```

---

## 5. Migration Naming Convention

Versioned migrations will follow:

```text
V<version>__<description>.sql
```

Examples:

```text
V1__initial_schema.sql
V2__add_notification_table.sql
V3__add_ai_assessment.sql
V4__add_indexes.sql
V5__add_candidate_resume_fields.sql
```

Rules:

* Version numbers must be unique.
* Versions must increase sequentially.
* Descriptions should be concise and meaningful.
* Use lowercase words separated by underscores.
* Do not rename an already-applied migration.
* Do not modify an already-applied migration.
* Create a new migration for subsequent changes.

---

## 6. Example Migration

Example:

```sql
CREATE TABLE notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    message VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id)
        REFERENCES user(id)
);
```

The exact schema will be finalized during database design.

This example is only demonstrating the migration mechanism.

---

## 7. Immutability of Applied Migrations

Once a migration has been successfully executed against a shared environment, it must be treated as immutable.

Do not modify:

```text
V1__initial_schema.sql
```

after it has been applied.

Instead, create:

```text
V2__modify_schema.sql
```

This allows Flyway to maintain a reliable history of database evolution.

---

## 8. Hibernate/JPA Strategy

Hibernate/JPA will manage:

```text
Java Entity
      ↕
Database Records
```

Hibernate will NOT be used as the source of truth for database schema evolution.

The database schema source of truth will be:

```text
Flyway migrations
```

Therefore, automatic schema modification should not be used for shared or production environments.

The intended configuration will eventually use:

```text
spring.jpa.hibernate.ddl-auto=validate
```

This tells Hibernate to validate that the database schema is compatible with the entity mappings without modifying the schema.

---

## 9. Source of Truth

TalentFlow will have a clear separation of responsibilities:

```text
Flyway
   ↓
Database schema

Hibernate/JPA
   ↓
Object-relational mapping
```

Therefore:

> Flyway defines what the database looks like. Hibernate defines how Java objects interact with that database.

---

## 10. Developer Workflow

When a developer needs to modify the database:

```text
1. Identify required schema change
        ↓
2. Create a new Flyway migration
        ↓
3. Test migration locally
        ↓
4. Commit migration to Git
        ↓
5. Push feature branch
        ↓
6. Create Pull Request
        ↓
7. Review migration
        ↓
8. Merge into main
```

Another developer can then pull the latest code and Flyway will apply the new migration to their local database.

---

## 11. Never Modify the Database Manually for Application Schema Changes

Developers should not use MySQL Workbench to make permanent application schema changes such as:

```text
CREATE TABLE
ALTER TABLE
DROP COLUMN
ADD COLUMN
CREATE INDEX
```

Instead, create a Flyway migration.

For example:

Instead of manually executing:

```sql
ALTER TABLE candidate
ADD COLUMN resume_file_name VARCHAR(255);
```

create:

```text
V5__add_candidate_resume_file_name.sql
```

containing the required SQL.

---

## 12. Local Development

Each developer will maintain their own local MySQL database.

The database itself does not need to be committed to Git.

The schema definition is represented by:

```text
Flyway migration files
```

Therefore:

```text
Developer A
MySQL
   ↑
Flyway
   ↑
Git

Developer B
MySQL
   ↑
Flyway
   ↑
Git
```

Both developers should converge on the same schema.

---

## 13. Test Environment

Automated tests should use a controlled database configuration.

The exact testing strategy will be decided during implementation.

Possible approaches include:

* Dedicated test database.
* Testcontainers.
* H2 where appropriate.

For database behavior that depends on MySQL-specific features, testing against MySQL is preferred.

The final testing strategy will be documented separately.

---

## 14. Production Environment

Production database changes must also be applied through Flyway.

The intended deployment flow is:

```text
Git
 ↓
CI/CD
 ↓
Build application
 ↓
Deploy application
 ↓
Flyway
 ↓
Production MySQL
```

Production schema changes must never depend on a developer manually executing SQL.

---

## 15. Migration Review

Database migrations are application code and must go through code review.

Reviewers should verify:

* Correct table/column definitions.
* Primary keys.
* Foreign keys.
* Constraints.
* Indexes.
* Nullable vs non-nullable fields.
* Data types.
* Potential data loss.
* Migration ordering.
* Backward compatibility where relevant.
* Performance impact.

---

## 16. Rollback Strategy

Flyway migrations should not rely on automatic rollback of already-applied production migrations.

When a migration needs to be reversed, the preferred approach is to create a new forward migration.

Example:

```text
V7__add_candidate_status.sql
V8__remove_candidate_status.sql
```

rather than modifying `V7`.

For destructive migrations, additional care and backups are required.

---

## 17. Migration History

Flyway maintains its migration history inside the database.

This allows the application to determine which migrations have already been executed.

Conceptually:

```text
Git Migration Files
        ↓
      Flyway
        ↓
flyway_schema_history
        ↓
      MySQL
```

This prevents the same migration from being executed repeatedly.

---

## 18. Benefits for TalentFlow

Using Flyway provides:

### Consistency

Both developers can reproduce the same database schema.

### Version Control

Database changes become part of Git.

### Traceability

The team can see how the schema evolved.

### Collaboration

Developers no longer need to communicate manual database changes separately.

### Deployment Safety

Production schema changes can be automated.

### Reproducibility

A new developer can build the database from the migration history.

---

## 19. Final Architecture Decision

TalentFlow will use:

```text
MySQL
   ↑
Flyway
   ↑
SQL Migration Files
   ↑
Git
```

Hibernate/JPA will be used for persistence:

```text
Spring Boot
    ↓
Spring Data JPA
    ↓
Hibernate
    ↓
MySQL
```

But schema ownership remains:

```text
Flyway → Database Schema
Hibernate → ORM / Persistence
```

This separation will be maintained throughout the project.

---

## 20. Decision Summary

| Area                                   | Decision                          |
| -------------------------------------- | --------------------------------- |
| Database                               | MySQL 8                           |
| ORM                                    | Hibernate/JPA                     |
| Migration tool                         | Flyway                            |
| Schema source of truth                 | Flyway migrations                 |
| Migration storage                      | Git                               |
| Migration location                     | `src/main/resources/db/migration` |
| Hibernate schema modification          | Disabled                          |
| Hibernate schema validation            | Enabled                           |
| Manual production schema changes       | Not allowed                       |
| Migration review                       | Required                          |
| Production migrations                  | Flyway                            |
| Migration modification after execution | Not allowed                       |
