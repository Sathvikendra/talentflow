# ADR-001: Modular Monolith Architecture

**Status:** Accepted
**Date:** 2026-09-05

## Context

TalentFlow is an AI-assisted talent fulfillment and candidate workflow platform being developed as a portfolio project by a two-person development team.

The application contains multiple business areas including:

* Authentication
* Users
* Requirements
* Candidates
* Candidate submissions
* Evaluations
* Interviews
* Workflow management
* AI assistance
* Notifications
* Dashboard analytics

The project requires clear separation of responsibilities but does not require independently deployable services.

Introducing microservices would add unnecessary operational complexity for the current scope.

## Decision

TalentFlow will use a **Modular Monolith architecture**.

The application will be deployed as a single Spring Boot application while maintaining clear internal module boundaries.

Conceptually:

```text
TalentFlow Backend
│
├── auth
├── user
├── requirement
├── candidate
├── submission
├── evaluation
├── interview
├── workflow
├── ai
├── notification
└── dashboard
```

Each module will have clear responsibilities and should minimize unnecessary coupling with other modules.

## Alternatives Considered

### Microservices

Rejected for the initial version because:

* The project does not require distributed deployment.
* It would introduce additional infrastructure.
* It would increase development and debugging complexity.
* The development team consists of two developers.
* Independent scaling is not currently required.

### Traditional Monolithic Structure

A tightly coupled monolith was not selected because it could allow business responsibilities to become mixed together as the application grows.

The modular monolith provides stronger internal boundaries while retaining simple deployment.

## Reasoning

The modular monolith provides a balance between:

```text
Simplicity
    +
Clear Architecture
    +
Maintainability
    +
Future Extensibility
```

It allows TalentFlow to demonstrate enterprise-style architectural thinking without introducing infrastructure that is unnecessary for V1.

## Consequences

### Positive

* Single deployment.
* Easier local development.
* Easier debugging.
* Lower infrastructure complexity.
* Clear module boundaries.
* Easier database transaction management.
* Suitable for a two-person development team.
* Can potentially evolve into services later if genuine scaling requirements emerge.

### Trade-offs

* Modules share the same application process.
* Modules share the same database.
* Strong discipline is required to prevent unwanted coupling.
* Independent deployment of modules is not available.

## Implementation Notes

The backend will follow a layered architecture within the modular monolith:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

DTOs, entities, validation, security, exception handling, and workflow logic will have clearly defined responsibilities.

Microservices, Kubernetes, service discovery, message brokers, and other distributed infrastructure are not part of the initial TalentFlow architecture.
