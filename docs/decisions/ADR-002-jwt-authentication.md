# ADR-002: JWT Authentication

**Status:** Accepted
**Date:** 2026-09-05

## Context

TalentFlow requires authenticated access to protected resources such as:

* Candidate information
* Requirements
* Candidate submissions
* Evaluations
* Interviews
* Dashboard information
* Administrative functionality

The Angular frontend communicates with the Spring Boot backend through REST APIs.

The application therefore requires a stateless authentication mechanism suitable for a REST-based architecture.

## Decision

TalentFlow will use **JSON Web Tokens (JWT)** for API authentication.

The Spring Boot backend will authenticate users and issue JWTs after successful login.

The Angular frontend will send the JWT with subsequent protected API requests using:

```text
Authorization: Bearer <JWT_TOKEN>
```

Spring Security will validate the token and enforce authorization rules.

## Authorization

TalentFlow will use role-based access control.

Initial roles:

```text
ADMIN
REVIEWER
VIEWER
```

Authentication determines the identity of the user.

Authorization determines what that user is permitted to do.

Backend authorization is mandatory even when the frontend hides unauthorized functionality.

## Alternatives Considered

### Server-side Session Authentication

Not selected for the initial REST API architecture because JWT provides a straightforward stateless authentication model between the Angular frontend and Spring Boot backend.

### OAuth2 / OpenID Connect

Not selected for V1 because TalentFlow does not currently require integration with an external identity provider.

OAuth2/OIDC can be introduced later if the application's authentication requirements expand.

## Reasoning

JWT provides:

* Stateless API authentication.
* Straightforward Angular integration.
* Spring Security support.
* Role-based authorization.
* A suitable foundation for the TalentFlow portfolio architecture.

## Security Considerations

JWT secrets must never be committed to Git.

The secret will be provided through environment-specific configuration.

Example:

```text
JWT_SECRET=<environment-specific-secret>
```

Passwords must never be stored in plain text.

Passwords will be securely hashed using an appropriate password encoder supported by Spring Security.

JWT expiration and other security settings will be configured during implementation.

## Consequences

### Positive

* Stateless authentication.
* Easy frontend/API integration.
* Clear separation between authentication and authorization.
* Well supported by Spring Security.

### Trade-offs

* Token lifecycle must be managed carefully.
* Token storage on the frontend requires security consideration.
* Token expiration and refresh behavior must be designed appropriately.

## Implementation Notes

JWT authentication will be implemented during the Authentication phase of TalentFlow development.

The exact token claims, expiration policy, password hashing configuration, and frontend token storage strategy will be finalized during implementation.
