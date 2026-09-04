# TalentFlow Development Guidelines

**Project:** TalentFlow
**Purpose:** AI-Assisted Talent Fulfillment and Candidate Workflow Platform

This document defines the coding, naming, API, database, Git, and architectural conventions followed by the TalentFlow development team.

The purpose is to maintain consistency, readability, maintainability, and smooth collaboration between developers.

---

# 1. General Principles

The TalentFlow codebase should follow these principles:

* Keep code simple and readable.
* Prefer meaningful names over comments.
* Follow separation of concerns.
* Keep business logic out of controllers/components.
* Avoid unnecessary duplication.
* Do not expose sensitive information in source code.
* Do not commit secrets, passwords, API keys, tokens, or production credentials.
* Use DTOs for API communication instead of exposing database entities directly.
* Validate input at appropriate application boundaries.
* Prefer small, focused classes and methods.
* Follow existing project conventions before introducing a new pattern.

---

# 2. Backend Development Guidelines

## 2.1 Java Naming Conventions

| Element     | Convention       | Example                      |
| ----------- | ---------------- | ---------------------------- |
| Class       | PascalCase       | `CandidateSubmissionService` |
| Interface   | PascalCase       | `CandidateRepository`        |
| Method      | camelCase        | `getCandidateById()`         |
| Variable    | camelCase        | `candidateId`                |
| Constant    | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT`            |
| Package     | lowercase        | `com.talentflow.candidate`   |
| Enum        | PascalCase       | `SubmissionStatus`           |
| Enum values | UPPER_SNAKE_CASE | `L1_PENDING`                 |

### Examples

```java
public class CandidateSubmissionService {
    
    private final CandidateSubmissionRepository candidateSubmissionRepository;

    public CandidateSubmissionDto getSubmissionById(Long submissionId) {
        // implementation
    }
}
```

---

# 3. Spring Boot Architecture

TalentFlow follows:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Additional supporting layers may include:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database

DTO
Entity
Mapper
Exception
Security
Validation
```

## 3.1 Controllers

Controllers are responsible for:

* Receiving HTTP requests.
* Validating request structure.
* Calling appropriate services.
* Returning HTTP responses.

Controllers should NOT contain:

* Business logic.
* Database queries.
* Complex workflow decisions.
* AI processing logic.

Example:

```text
CandidateController
        ↓
CandidateService
        ↓
CandidateRepository
```

---

# 4. Services

Services contain application and business logic.

Examples:

```text
CandidateService
RequirementService
SubmissionService
WorkflowService
EvaluationService
InterviewService
AiAssessmentService
```

Workflow validation must happen in the service/business layer rather than directly inside controllers.

---

# 5. Repositories

Repositories are responsible for database access.

Examples:

```text
CandidateRepository
RequirementRepository
CandidateSubmissionRepository
L1EvaluationRepository
ClientInterviewRepository
```

Repositories should not contain business workflow decisions.

---

# 6. DTO Conventions

DTOs are used for communication between the API and clients.

Examples:

```text
CandidateDto
CreateCandidateRequest
UpdateCandidateRequest
CandidateResponse
RequirementDto
LoginRequest
LoginResponse
```

Database entities should not normally be returned directly from REST controllers.

---

# 7. Dependency Injection

Use constructor-based dependency injection.

Preferred:

```java
public CandidateService(
        CandidateRepository candidateRepository) {
    this.candidateRepository = candidateRepository;
}
```

Avoid field injection such as:

```java
@Autowired
private CandidateRepository candidateRepository;
```

---

# 8. Exception Handling

Use centralized exception handling.

Application-specific exceptions should be meaningful.

Examples:

```text
CandidateNotFoundException
RequirementNotFoundException
InvalidWorkflowTransitionException
UnauthorizedException
```

API errors should follow a consistent response structure.

Example:

```json
{
  "success": false,
  "message": "Candidate not found",
  "timestamp": "2026-09-05T12:00:00",
  "path": "/api/v1/candidates/123"
}
```

---

# 9. REST API Conventions

TalentFlow APIs use:

```text
/api/v1/
```

Examples:

```text
/api/v1/auth/login
/api/v1/users
/api/v1/requirements
/api/v1/candidates
/api/v1/submissions
/api/v1/evaluations
/api/v1/interviews
/api/v1/ai
/api/v1/notifications
/api/v1/dashboard
```

Use HTTP methods according to their intended purpose:

```text
GET     → Retrieve
POST    → Create
PUT     → Replace/update
PATCH   → Partial update
DELETE  → Delete
```

Use plural resource names where appropriate:

```text
/candidates
/requirements
/submissions
/interviews
```

---

# 10. Workflow Rules

TalentFlow uses a controlled candidate workflow.

Valid workflow states include:

```text
PROFILE_RECEIVED
L1_PENDING
L1_SCHEDULED
L1_CLEARED
L1_REJECTED
CI_PENDING
CI_SCHEDULED
CI_ACCEPTED
CI_REJECTED
WITHDRAWN
```

Workflow transitions must be validated by the business/service layer.

Every workflow state change must create a corresponding `StatusHistory` record.

Invalid transitions must be rejected.

---

# 11. Angular Development Guidelines

Angular will follow a modular structure.

The primary architectural categories are:

```text
core/
shared/
pages/
components/
services/
guards/
interceptors/
models/
```

The exact folder structure will be finalized when the Angular application is bootstrapped.

---

# 12. Angular Naming Conventions

| Element     | Convention                     | Example                       |
| ----------- | ------------------------------ | ----------------------------- |
| Component   | kebab-case files               | `candidate-list.component.ts` |
| Service     | kebab-case + `.service.ts`     | `candidate.service.ts`        |
| Guard       | kebab-case + `.guard.ts`       | `auth.guard.ts`               |
| Interceptor | kebab-case + `.interceptor.ts` | `auth.interceptor.ts`         |
| Model       | kebab-case + `.model.ts`       | `candidate.model.ts`          |
| Page        | descriptive kebab-case         | `candidate-details`           |

Angular classes should use PascalCase.

Examples:

```typescript
CandidateListComponent
CandidateService
AuthGuard
AuthInterceptor
Candidate
CandidateDetailsComponent
```

---

# 13. Angular Responsibilities

## Components

Components are responsible primarily for:

* Displaying data.
* Handling user interaction.
* Calling appropriate services.
* Managing component-level UI state.

Avoid putting large business rules directly inside components.

## Services

Services handle:

* API communication.
* Shared application logic.
* Reusable operations.
* State/data coordination where appropriate.

## Guards

Guards handle route access control.

Example:

```text
AuthGuard
RoleGuard
```

## Interceptors

Interceptors handle cross-cutting HTTP behavior.

Examples:

```text
JWT token attachment
HTTP error handling
Request/response behavior
```

---

# 14. Database Conventions

Database naming should use `snake_case`.

Examples:

```text
candidate
candidate_submission
l1_evaluation
client_interview
status_history
created_at
updated_at
candidate_id
requirement_id
```

Primary keys should normally follow:

```text
id
```

Foreign keys should follow:

```text
<entity>_id
```

Examples:

```text
candidate_id
requirement_id
submission_id
user_id
```

Database schema changes must be managed through Flyway migrations.

Hibernate must not be used to automatically modify the production database schema.

---

# 15. API and Database Separation

The following layers should remain separate:

```text
Angular Model
      ↓
API DTO
      ↓
Service
      ↓
Entity
      ↓
Database
```

Do not tightly couple Angular models directly to database entities.

---

# 16. AI Development Guidelines

AI functionality is assistive only.

The AI system may:

* Compare requirements and resumes.
* Identify matching skills.
* Identify missing skills.
* Extract relevant evidence.
* Generate interview questions.
* Generate human-readable assessment summaries.

The AI system must NOT:

* Automatically reject candidates.
* Automatically select candidates.
* Automatically hire candidates.
* Make final hiring decisions.
* Replace human evaluation.

Final decisions remain with authorized human users.

AI-generated results should be treated as assistance and should be reviewable by humans.

---

# 17. Security Guidelines

Never commit:

```text
Gemini API keys
JWT secrets
Database passwords
Production credentials
Personal access tokens
.env files
```

Secrets must be supplied through environment-specific configuration or environment variables.

Examples:

```text
GEMINI_API_KEY
JWT_SECRET
DB_USERNAME
DB_PASSWORD
```

Never hardcode secrets inside:

```text
Java source code
Angular source code
application.yml
GitHub commits
README files
```

---

# 18. Git Branch Naming

The main branch is:

```text
main
```

Feature branches should follow:

```text
feature/<feature-name>
```

Examples:

```text
feature/authentication
feature/candidates
feature/requirements
feature/workflow
feature/ai-assessment
feature/dashboard
```

Bug fixes:

```text
fix/<issue-name>
```

Examples:

```text
fix/login-validation
fix/candidate-status
```

---

# 19. Commit Message Convention

Commit messages should be meaningful and concise.

Preferred format:

```text
<type>: <description>
```

Examples:

```text
feat: add JWT authentication
feat: add candidate CRUD APIs
fix: correct workflow transition validation
docs: update development guidelines
refactor: simplify candidate service
test: add candidate service tests
chore: update dependencies
```

Common types:

```text
feat
fix
docs
refactor
test
chore
```

Avoid commits such as:

```text
changes
update
final
final2
working
stuff
asdf
```

---

# 20. Pull Request Guidelines

Before creating a pull request:

1. Pull the latest `main`.
2. Ensure the branch builds successfully.
3. Run relevant tests.
4. Review your own changes.
5. Remove debugging code.
6. Ensure no secrets are included.
7. Create a clear pull request description.

Preferred flow:

```text
feature branch
      ↓
commit
      ↓
push
      ↓
Pull Request
      ↓
Code Review
      ↓
Merge into main
```

Direct pushes to `main` should be avoided once branch protection is enabled.

---

# 21. Code Review Guidelines

Code reviews should focus on:

* Correctness.
* Security.
* Maintainability.
* Readability.
* Architecture.
* Test coverage.
* API consistency.
* Database impact.
* Workflow correctness.

The purpose of code review is to improve the codebase, not merely approve or reject someone's work.

---

# 22. Testing Guidelines

Tests should be added alongside features.

Backend testing will include:

```text
Unit Tests
Integration Tests
Controller/API Tests
Repository Tests where appropriate
```

Frontend testing will cover important:

```text
Components
Services
Guards
Interceptors
```

Critical workflow transitions should have automated tests.

---

# 23. Documentation Guidelines

Important architectural or technical decisions should be documented.

Architecture documentation will live under:

```text
docs/
```

Examples:

```text
docs/DEVELOPMENT_GUIDELINES.md
docs/DEVELOPMENT_SETUP.md
docs/architecture/
docs/database/
docs/api/
docs/workflow/
docs/decisions/
```

---

# 24. Golden Rule

When adding new code, ask:

> Does this follow the existing architecture, naming conventions, security rules, and responsibilities defined in this document?

If the answer is no, either change the implementation or document why the new approach is necessary.

Consistency is more valuable than introducing patterns simply because they are newer or more complex.
