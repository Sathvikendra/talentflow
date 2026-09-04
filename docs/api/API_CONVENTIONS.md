# TalentFlow API Conventions

**Project:** TalentFlow
**Purpose:** AI-Assisted Talent Fulfillment and Candidate Workflow Platform

This document defines the conventions used by TalentFlow REST APIs.

The purpose is to provide a predictable, consistent API contract between the Angular frontend and Spring Boot backend.

This document defines API conventions, not the complete API specification. Individual endpoints will be documented as features are implemented.

---

# 1. API Base Path

All TalentFlow APIs use versioned endpoints.

```text
/api/v1
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

API versioning allows future versions to coexist without immediately breaking existing clients.

---

# 2. Resource Naming

REST resources should use plural nouns.

Preferred:

```text
/candidates
/requirements
/submissions
/interviews
/evaluations
/notifications
```

Avoid:

```text
/getCandidates
/createCandidate
/fetchRequirements
/deleteCandidate
```

The HTTP method communicates the operation.

---

# 3. HTTP Methods

TalentFlow follows standard HTTP methods.

| Method | Purpose                                   |
| ------ | ----------------------------------------- |
| GET    | Retrieve resources                        |
| POST   | Create a resource or execute an operation |
| PUT    | Replace an existing resource              |
| PATCH  | Partially update a resource               |
| DELETE | Delete a resource                         |

Examples:

```text
GET    /api/v1/candidates
GET    /api/v1/candidates/101
POST   /api/v1/candidates
PATCH  /api/v1/candidates/101
DELETE /api/v1/candidates/101
```

---

# 4. Resource Identification

Resources are identified using their IDs.

Example:

```text
GET /api/v1/candidates/101
```

Nested resources may be used when the relationship is important to the operation.

Example:

```text
GET /api/v1/requirements/200/candidates
```

However, excessive nesting should be avoided.

---

# 5. Authentication

Protected APIs require JWT authentication.

The client sends the token using the standard Authorization header:

```text
Authorization: Bearer <JWT_TOKEN>
```

Authentication endpoints such as login are publicly accessible where appropriate.

Example:

```text
POST /api/v1/auth/login
```

Protected resources require a valid JWT.

---

# 6. Authorization

Authentication and authorization are separate concerns.

Authentication answers:

> Who is the user?

Authorization answers:

> What is the user allowed to do?

TalentFlow uses role-based access control.

Current roles:

```text
ADMIN
REVIEWER
VIEWER
```

Authorization rules will be enforced at the backend.

The frontend may hide unauthorized UI actions, but the backend must always enforce the actual permission.

---

# 7. Successful Responses

Successful responses should return the resource or result directly rather than unnecessarily wrapping it inside a generic `success/data` structure.

Example:

```json
{
  "id": 101,
  "name": "John Doe",
  "email": "john@example.com",
  "experience": 4
}
```

For collections:

```json
[
  {
    "id": 101,
    "name": "John Doe"
  },
  {
    "id": 102,
    "name": "Jane Doe"
  }
]
```

The exact response DTO will depend on the endpoint.

---

# 8. HTTP Status Codes

TalentFlow APIs should use meaningful HTTP status codes.

| Status | Meaning               | Typical Usage                                |
| ------ | --------------------- | -------------------------------------------- |
| 200    | OK                    | Successful GET/update                        |
| 201    | Created               | Successful resource creation                 |
| 204    | No Content            | Successful deletion                          |
| 400    | Bad Request           | Invalid request                              |
| 401    | Unauthorized          | Missing/invalid authentication               |
| 403    | Forbidden             | Authenticated but insufficient permissions   |
| 404    | Not Found             | Resource does not exist                      |
| 409    | Conflict              | Resource/workflow conflict                   |
| 422    | Unprocessable Entity  | Semantically invalid input where appropriate |
| 500    | Internal Server Error | Unexpected server error                      |

---

# 9. Error Response Format

All API errors should follow a consistent structure.

Example:

```json
{
  "success": false,
  "message": "Requirement not found",
  "timestamp": "2026-09-05T18:30:00Z",
  "path": "/api/v1/requirements/123"
}
```

The `success` field is intentionally used for errors to make the error contract explicit.

Standard fields:

```text
success
message
timestamp
path
```

Additional fields may be added when useful.

---

# 10. Validation Errors

Validation failures should provide enough information for the frontend to identify the invalid fields.

Example:

```json
{
  "success": false,
  "message": "Validation failed",
  "timestamp": "2026-09-05T18:30:00Z",
  "path": "/api/v1/candidates",
  "errors": [
    {
      "field": "email",
      "message": "Invalid email address"
    },
    {
      "field": "experience",
      "message": "Experience must be greater than or equal to 0"
    }
  ]
}
```

Validation should be performed on the backend even when frontend validation exists.

Frontend validation improves user experience.

Backend validation protects the API.

---

# 11. Query Parameters

Query parameters should be used for filtering, sorting, searching, and pagination.

Example:

```text
GET /api/v1/candidates?skill=java
```

Multiple filters:

```text
GET /api/v1/candidates?skill=java&experienceMin=3
```

Search:

```text
GET /api/v1/candidates?search=spring
```

Query parameter names should use camelCase.

---

# 12. Pagination

Collection endpoints that may return large datasets should support pagination.

Initial convention:

```text
?page=0&size=20
```

Example:

```text
GET /api/v1/candidates?page=0&size=20
```

Pagination responses should provide metadata.

Example:

```json
{
  "content": [
    {
      "id": 101,
      "name": "John Doe"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 125,
  "totalPages": 7
}
```

The exact implementation may use Spring Data's pagination capabilities.

---

# 13. Sorting

Sorting should use a query parameter.

Example:

```text
GET /api/v1/candidates?sort=name,asc
```

Multiple sorting criteria may be supported when required.

Example:

```text
GET /api/v1/candidates?sort=experience,desc&sort=name,asc
```

---

# 14. Date and Time

API timestamps should use ISO 8601 format.

Example:

```text
2026-09-05T18:30:00Z
```

The backend should preferably use appropriate Java time types such as:

```text
Instant
LocalDate
LocalDateTime
```

depending on the business requirement.

For events representing an exact point in time, timezone-aware values should be preferred.

---

# 15. Request Bodies

JSON should be the default format for API request bodies.

Example:

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "experience": 4
}
```

Request DTOs should be used rather than accepting database entities directly.

Examples:

```text
CreateCandidateRequest
UpdateCandidateRequest
CreateRequirementRequest
LoginRequest
```

---

# 16. Response DTOs

API responses should use DTOs.

Examples:

```text
CandidateResponse
RequirementResponse
SubmissionResponse
InterviewResponse
DashboardResponse
```

Database entities should not normally be returned directly from controllers.

This prevents the API contract from becoming tightly coupled to the database model.

---

# 17. Endpoint Naming Examples

## Authentication

```text
POST /api/v1/auth/login
```

## Users

```text
GET    /api/v1/users
GET    /api/v1/users/{id}
POST   /api/v1/users
PATCH  /api/v1/users/{id}
```

## Requirements

```text
GET    /api/v1/requirements
GET    /api/v1/requirements/{id}
POST   /api/v1/requirements
PATCH  /api/v1/requirements/{id}
```

## Candidates

```text
GET    /api/v1/candidates
GET    /api/v1/candidates/{id}
POST   /api/v1/candidates
PATCH  /api/v1/candidates/{id}
```

## Candidate Submissions

```text
GET  /api/v1/submissions
GET  /api/v1/submissions/{id}
POST /api/v1/submissions
```

## Evaluations

```text
GET  /api/v1/evaluations/{id}
POST /api/v1/evaluations
PATCH /api/v1/evaluations/{id}
```

## Interviews

```text
GET  /api/v1/interviews
GET  /api/v1/interviews/{id}
POST /api/v1/interviews
PATCH /api/v1/interviews/{id}
```

## AI

AI-specific endpoints will be defined when AI functionality is implemented.

Possible examples:

```text
POST /api/v1/ai/resume-parse
POST /api/v1/ai/match
POST /api/v1/ai/interview-questions
POST /api/v1/ai/assessment-summary
```

These are examples only and are not considered finalized endpoints.

---

# 18. Workflow Operations

Candidate workflow transitions should not be represented as arbitrary database updates.

For example, instead of:

```text
PATCH /api/v1/submissions/101
{
  "status": "L1_CLEARED"
}
```

a dedicated workflow operation may be used when appropriate:

```text
POST /api/v1/submissions/101/transitions
```

Example request:

```json
{
  "targetStatus": "L1_CLEARED",
  "comments": "Candidate cleared L1 technical interview"
}
```

The backend must validate whether the transition is permitted.

This ensures that workflow rules remain centralized in the business layer.

---

# 19. Idempotency and Duplicate Operations

Operations that may accidentally be submitted multiple times should be designed carefully.

Examples include:

* Interview scheduling.
* Candidate submission.
* Workflow transitions.
* Notification creation.

Where necessary, the backend should prevent duplicate operations through validation and database constraints.

Idempotency mechanisms may be introduced later if required.

---

# 20. API Security Rules

Never expose:

```text
Passwords
JWT secrets
Gemini API keys
Database credentials
Internal security configuration
```

Sensitive information must never appear in API responses.

Passwords must never be returned from user endpoints.

---

# 21. API Documentation

All public backend endpoints should eventually be documented using OpenAPI/Swagger.

API documentation should include:

* Endpoint
* HTTP method
* Authentication requirement
* Request parameters
* Request body
* Response body
* HTTP status codes
* Validation errors
* Authorization requirements

Swagger/OpenAPI documentation will be generated and maintained alongside the backend.

---

# 22. API Design Principle

Before creating an endpoint, ask:

1. What resource is being accessed?
2. What HTTP method represents the operation?
3. Does the endpoint require authentication?
4. Which roles are allowed?
5. What request DTO is required?
6. What response DTO should be returned?
7. What validation is required?
8. What errors can occur?
9. Does the operation affect workflow state?
10. Does the operation require database transaction boundaries?

The goal is to keep TalentFlow APIs predictable, secure, and easy for the Angular frontend to consume.

---

# 23. Convention Over Exception

These conventions are the default for TalentFlow.

A feature may deviate from them when there is a legitimate architectural or business reason.

Significant deviations should be documented and reviewed before implementation.
