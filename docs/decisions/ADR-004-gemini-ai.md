# ADR-004: Gemini for AI Assistance

**Status:** Accepted
**Date:** 2026-09-05

## Context

TalentFlow includes AI-assisted functionality for supporting candidate workflow activities.

Planned capabilities include:

* Resume text analysis.
* Requirement and resume comparison.
* Matched skill identification.
* Missing skill identification.
* Evidence extraction.
* Interview question generation.
* AI-generated assessment summaries.

The AI system is intended to assist human reviewers rather than make autonomous hiring decisions.

## Decision

TalentFlow will use the **Google Gemini API** as its initial generative AI provider.

Gemini will be integrated through the Spring Boot backend.

The Angular frontend will not directly communicate with the Gemini API.

Architecture:

```text
Angular
   ↓
Spring Boot
   ↓
TalentFlow AI Service
   ↓
Gemini API
```

## Human Decision Requirement

AI functionality must remain assistive.

The AI must NOT:

* Automatically reject candidates.
* Automatically select candidates.
* Automatically hire candidates.
* Make final hiring decisions.
* Replace human evaluation.

Final candidate decisions remain with authorized human users.

## Reasoning

Gemini was selected for the initial implementation because it provides a practical generative AI API suitable for demonstrating AI integration within the TalentFlow architecture.

The abstraction between the TalentFlow AI service and external provider is intentional.

The application should avoid tightly coupling business logic directly to provider-specific API calls.

## Alternatives Considered

Other AI providers or locally hosted models may be considered in future versions.

No additional AI provider is required for V1.

## Security

The Gemini API key must never be stored in source code or committed to Git.

It will be provided through environment-specific configuration.

Example:

```text
GEMINI_API_KEY=<environment-specific-key>
```

## Consequences

### Positive

* Demonstrates real AI integration.
* Keeps AI communication centralized in the backend.
* Allows the frontend to consume a stable TalentFlow API.
* Makes future provider replacement easier.

### Trade-offs

* Dependency on an external AI provider.
* API availability and quotas may affect development.
* AI output is probabilistic and must be treated as assistive information.

## Implementation Notes

The AI architecture will be documented separately in:

```text
docs/architecture/ai-architecture.md
```

AI functionality will be implemented after the core candidate workflow is operational.
