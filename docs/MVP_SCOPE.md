# TalentFlow MVP Scope

**Project:** TalentFlow
**Tagline:** AI-Assisted Talent Fulfillment and Candidate Workflow Platform

---

# 1. Purpose

This document defines the Minimum Viable Product (MVP) scope for TalentFlow.

TalentFlow has many possible features and capabilities. The MVP is intentionally limited to the core talent fulfillment workflow required to demonstrate:

* Full-stack development
* Spring Boot backend architecture
* Angular frontend architecture
* MySQL database design
* JWT authentication
* Role-based access control
* Candidate workflow management
* Interview management
* Resume processing
* AI-assisted candidate analysis
* Dashboard analytics
* Automated testing
* CI/CD fundamentals

The objective is to build a complete, coherent, production-style portfolio application rather than a collection of disconnected features.

---

# 2. MVP Philosophy

TalentFlow will be developed incrementally.

We will not build every module simultaneously.

Each phase should produce a working increment of the application.

The preferred development model is:

```text
Design
   ↓
Database
   ↓
Backend API
   ↓
Frontend
   ↓
Integration
   ↓
Testing
   ↓
Documentation
   ↓
Review
```

Features should be developed as vertical slices wherever practical.

---

# 3. MVP Architecture

The MVP will use a **Modular Monolith** architecture.

```text
                    TalentFlow
                        │
        ┌───────────────┴───────────────┐
        │                               │
    Angular UI                    Spring Boot API
        │                               │
        │                    ┌──────────┼──────────┐
        │                    │          │          │
        │                   Auth     Workflow      AI
        │                    │          │          │
        │                    └──────────┼──────────┘
        │                               │
        └───────────────────────────────┤
                                        ↓
                                      MySQL
```

External AI communication:

```text
Spring Boot
     ↓
TalentFlow AI Service
     ↓
Gemini API
```

The application will remain a single deployable backend application.

Microservices are explicitly out of scope for V1.

---

# 4. MVP Technology Stack

## Frontend

* Angular 22
* TypeScript
* Angular Material
* SCSS
* RxJS

## Backend

* Java 21 LTS
* Spring Boot 4.x
* Spring Security
* Spring Data JPA
* Hibernate
* Maven Wrapper

## Database

* MySQL 8
* MySQL Workbench
* Flyway

## AI

* Google Gemini API
* Apache PDFBox

## API

* REST
* OpenAPI / Swagger

## Security

* JWT
* Spring Security
* RBAC

## Development

* Git
* GitHub
* VS Code
* IntelliJ IDEA
* Postman or Bruno

## CI

* GitHub Actions

---

# 5. MVP User Roles

TalentFlow V1 will support three roles.

| Role     | Responsibility                                              |
| -------- | ----------------------------------------------------------- |
| ADMIN    | Manage users and system-level configuration                 |
| REVIEWER | Manage requirements, candidates, submissions and interviews |
| VIEWER   | Read-only access to permitted information                   |

Authorization must be enforced by the backend.

The frontend must never be treated as the security boundary.

---

# 6. Core Business Workflow

The MVP candidate workflow is:

```text
Client Requirement
        ↓
Candidate Search
        ↓
Candidate Profile Added
        ↓
Human Profile Review
        ↓
L1 Interview
        ↓
L1 Result
    ┌───┴────┐
    ↓        ↓
 Rejected  Cleared
              ↓
       Client Interview Pending
              ↓
       Client Interview Scheduled
              ↓
       Client Interview Conducted
          ┌───┴────┐
          ↓        ↓
       Rejected  Accepted
                    ↓
            Requirement Filled
                    ↓
            Requirement Closed
```

Every workflow transition must be validated.

Every transition must be recorded in status history.

---

# 7. Phase 0 - Foundation

## Objective

Establish the technical and architectural foundation before feature development.

## Scope

### Repository

* GitHub repository
* `main` branch
* Feature branch strategy
* Pull Request workflow
* Code review process
* `.gitignore`

### Development Environment

* Java 21
* Node.js 22.x
* Angular CLI 22
* MySQL 8
* Git
* VS Code
* IntelliJ IDEA
* Postman or Bruno

### Architecture

* Modular Monolith decision
* Backend architecture
* Frontend architecture
* AI architecture
* API conventions
* Database architecture
* Workflow architecture

### Database

* Database design
* Entity relationships
* Constraints
* Indexes
* ER diagram
* Flyway strategy
* Initial migration

### Security Foundation

* JWT architecture
* RBAC design
* Password hashing strategy
* Security configuration strategy
* Secret management

JWT implementation itself belongs to Phase 1.

### CI

GitHub Actions should eventually validate:

```text
Pull Request
     ↓
GitHub Actions
     ↓
Backend Build
     ↓
Backend Tests
     ↓
Frontend Build
     ↓
Frontend Tests
     ↓
PASS / FAIL
```

### Documentation

* README
* Development setup
* Development guidelines
* API conventions
* Architecture documentation
* Database documentation
* Workflow documentation
* ADRs

---

# 8. Phase 1 - Authentication

## Objective

Establish secure user authentication and authorization.

## Features

```text
User
Login
JWT
Roles
RBAC
Password hashing
```

## MVP capabilities

* User creation by ADMIN
* Login
* Password verification
* JWT generation
* JWT validation
* Role-based authorization
* Protected API endpoints
* Protected Angular routes
* Authentication interceptor
* Logout

## Roles

```text
ADMIN
REVIEWER
VIEWER
```

## Exit Criteria

A user should be able to:

```text
Login
   ↓
Receive JWT
   ↓
Access authorized APIs
   ↓
Access permitted UI
```

Unauthorized users must receive appropriate HTTP responses.

---

# 9. Phase 2 - Requirements

## Objective

Allow users to manage client requirements.

## Features

```text
Create RR
View RR
View RR details
Update RR
Close RR
Requirement dashboard
```

## Requirement information

The MVP should capture information such as:

* RR number
* Job title
* Required skills
* Experience
* Location
* Onshore / Offshore
* Description
* Status
* Created date
* Closing date

## Requirement states

At minimum:

```text
OPEN
CLOSED
```

## Exit Criteria

A reviewer should be able to:

```text
Create Requirement
       ↓
View Requirement
       ↓
Update Requirement
       ↓
Close Requirement
```

---

# 10. Phase 3 - Candidates

## Objective

Create and manage candidate profiles.

## Features

```text
Candidate creation
Resume upload
Resume storage
Resume parsing
Candidate profile
```

## Candidate information

The MVP should support:

* Name
* Email
* Phone
* Experience
* Skills
* Resume
* Resume text
* Profile information
* Created date

## Resume Processing

Initial flow:

```text
PDF Resume
     ↓
PDFBox
     ↓
Extracted Text
     ↓
Candidate Profile
```

Resume parsing should extract useful textual information without making hiring decisions.

---

# 11. Phase 4 - Submission and Workflow

## Objective

Connect candidates to requirements and introduce the central TalentFlow state machine.

## Features

```text
CandidateSubmission
Workflow state machine
Valid transitions
StatusHistory
```

## Submission

A candidate can be submitted against a specific requirement.

Example:

```text
Candidate A
     +
Requirement RR-1001
     ↓
CandidateSubmission
```

## Workflow States

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

## Rules

The system must prevent invalid transitions.

Example:

```text
PROFILE_RECEIVED
        ↓
L1_PENDING
```

is valid.

But:

```text
PROFILE_RECEIVED
        ↓
CI_ACCEPTED
```

is invalid.

Every valid transition must create a `StatusHistory` record.

---

# 12. Phase 5 - L1 Interview

## Objective

Allow reviewers to schedule and evaluate the first-level interview.

## Features

```text
Schedule L1
Conduct L1
Record evaluation
Clear candidate
Reject candidate
Add comments
```

## Evaluation

The interviewer should be able to record:

* Interviewer
* Date
* Technical observations
* Comments
* Recommendation
* Result

Possible results:

```text
CLEARED
REJECTED
```

The final decision is made by the human interviewer.

AI does not determine the result.

---

# 13. Phase 6 - Client Interview

## Objective

Manage the client interview stage.

## Features

```text
Schedule client interview
Record interview
Accept candidate
Reject candidate
Add comments
```

Possible results:

```text
ACCEPTED
REJECTED
```

A successful client interview should transition the submission toward:

```text
Requirement Filled
       ↓
Requirement Closed
```

The exact relationship between accepted submissions and requirement closure will be enforced by business rules.

---

# 14. Phase 7 - AI Assistance

## Objective

Introduce AI-assisted functionality after the core workflow is stable.

AI is an assistant, not a decision-maker.

## Features

### Resume Parsing

```text
Resume PDF
    ↓
PDFBox
    ↓
Resume Text
```

### Skill Extraction

```text
Resume Text
    ↓
Gemini
    ↓
Extracted Skills
```

### Requirement Matching

Input:

```text
Requirement
+
Candidate Resume
```

Output:

```text
Matched Skills
Missing Skills
Evidence
Summary
```

### Evidence

The system should provide evidence from the candidate's resume where practical.

Example:

```text
Matched Skill:
Java

Evidence:
Candidate resume mentions 3 years of Java development.
```

### Missing Skills

Example:

```text
Required:
AWS

Resume Evidence:
No clear evidence found.
```

The system should avoid presenting absence of evidence as absolute proof that the candidate lacks the skill.

### Interview Questions

Gemini may generate suggested interview questions based on:

```text
Requirement
+
Candidate Resume
```

### AI Summary

The system may generate an evidence-based summary such as:

```text
The candidate demonstrates experience with Java,
Spring Boot and MySQL. The resume does not provide
clear evidence of AWS experience.
```

---

# 15. AI Restrictions

AI must NOT:

```text
Automatically reject candidates
Automatically accept candidates
Automatically hire candidates
Make final hiring decisions
Assign a hiring score
Determine candidate eligibility autonomously
```

Human reviewers remain responsible for decisions.

The UI should clearly distinguish:

```text
AI Assistance
```

from:

```text
Human Decision
```

---

# 16. Phase 8 - Dashboard

## Objective

Provide a high-level operational view of TalentFlow.

## Dashboard Metrics

```text
Open Requirements
Closed Requirements
Pending L1
Pending Client Interviews
Accepted
Rejected
Average Aging
Onshore Requirements
Offshore Requirements
```

## Dashboard principles

The dashboard should provide useful operational information without becoming an analytics platform.

Advanced BI functionality is outside the V1 MVP.

---

# 17. Phase 9 - Testing, Polish and Deployment

## Objective

Make the MVP stable, demonstrable and deployable.

## Testing

### Backend

* Unit tests
* Service tests
* Controller/API tests
* Repository tests where required
* Workflow transition tests
* Security tests

### Frontend

* Component tests
* Service tests
* Guard/interceptor tests
* Critical workflow tests

### Integration

At minimum verify:

```text
Angular
   ↓
Spring Boot
   ↓
MySQL
```

and:

```text
Spring Boot
   ↓
Gemini
```

where AI functionality is enabled.

---

# 18. MVP Exit Criteria

TalentFlow V1 is considered complete when a user can perform the following end-to-end workflow:

```text
Login
  ↓
Create Requirement
  ↓
Create Candidate
  ↓
Upload Resume
  ↓
Parse Resume
  ↓
Submit Candidate
  ↓
Review Candidate
  ↓
Schedule L1
  ↓
Record L1 Result
  ↓
Schedule Client Interview
  ↓
Record Client Result
  ↓
Accept Candidate
  ↓
Fill Requirement
  ↓
Close Requirement
```

Additionally:

```text
AI Assessment
     ↓
Matched Skills
     ↓
Missing Skills
     ↓
Evidence
     ↓
Interview Questions
     ↓
AI Summary
```

must be available as an assistive capability.

---

# 19. What Is Explicitly Out of Scope for V1

To prevent scope creep, the following are not part of the initial MVP.

## Recruitment Platform Features

* Public job portal
* Candidate self-registration
* External recruiter accounts
* Real client accounts
* Real job-board integrations
* LinkedIn integration
* Email automation platform
* SMS integration
* Calendar integrations
* Payroll
* Offer management
* Employee onboarding
* Background verification

## Advanced AI

* Autonomous candidate ranking
* Autonomous candidate rejection
* Autonomous hiring recommendations
* Predictive hiring decisions
* Personality inference
* Facial analysis
* Voice analysis
* Sentiment-based hiring decisions
* AI interview conducting
* Autonomous recruiter agents

## Infrastructure

* Microservices
* Kubernetes
* Service mesh
* Event-driven distributed architecture
* Multi-region deployment
* Complex cloud infrastructure

These may be considered in future versions only if there is a clear product requirement.

---

# 20. Development Ownership Strategy

TalentFlow will not divide the team into permanent frontend and backend silos.

Both Sathvik and Vinitha should understand:

```text
Frontend
Backend
Database
Authentication
Workflow
AI
Testing
CI/CD
Documentation
```

Ownership will rotate between milestones.

Example:

| Sprint         | Sathvik      | Vinitha                   |
| -------------- | ------------ | ------------------------- |
| Authentication | Backend      | Angular + API Integration |
| Requirements   | Angular      | Backend                   |
| Candidates     | Backend      | Angular                   |
| Workflow       | Angular      | Backend                   |
| AI             | AI + Backend | AI UI + Testing           |
| Dashboard      | Backend + DB | Angular                   |

Ownership does not mean exclusive knowledge.

Both developers should participate in:

* Code review
* Architecture discussions
* Database discussions
* Testing
* Debugging
* Documentation

---

# 21. Code Review Strategy

Every significant feature should have peer review.

```text
Developer A
     ↓
Feature Branch
     ↓
Pull Request
     ↓
Developer B Review
     ↓
Changes if required
     ↓
Approval
     ↓
Merge
```

Both developers should regularly review code outside their primary implementation area.

This ensures shared system knowledge.

---

# 22. Vertical Slice Development

Whenever practical, features should be implemented as complete vertical slices.

For example:

```text
Requirement Feature
       │
       ├── Database
       ├── Entity
       ├── Repository
       ├── Service
       ├── Controller
       ├── DTO
       ├── Angular Service
       ├── Angular Component
       ├── Validation
       └── Tests
```

Avoid creating a large empty backend architecture first and postponing integration until the end.

---

# 23. First Development Vertical Slice

Before implementing the complete MVP, the team will build one tiny end-to-end slice.

```text
STEP 1
Create/verify GitHub repository
        ↓
STEP 2
Configure branches and protection
        ↓
STEP 3
Create project skeleton
        ↓
STEP 4
Bootstrap Spring Boot
        ↓
STEP 5
Bootstrap Angular
        ↓
STEP 6
Connect Angular → Spring Boot
        ↓
STEP 7
Connect Spring Boot → MySQL
        ↓
STEP 8
Add Flyway
        ↓
STEP 9
Establish package architecture
        ↓
STEP 10
Create health-check API
        ↓
STEP 11
Add basic frontend health-check screen
        ↓
STEP 12
Run tests
        ↓
STEP 13
Commit
        ↓
STEP 14
Pull Request
        ↓
STEP 15
Review and merge
```

The objective is to prove that the development environment works on both developers' machines before feature development begins.

---

# 24. Definition of Done

A feature is considered complete only when:

```text
[ ] Database changes implemented
[ ] Flyway migration created where required
[ ] Backend implementation completed
[ ] DTOs implemented
[ ] Validation implemented
[ ] API implemented
[ ] Frontend implementation completed
[ ] Authentication/authorization considered
[ ] Error handling implemented
[ ] Tests added
[ ] API documented
[ ] Relevant project documentation updated
[ ] Code reviewed
[ ] No secrets committed
[ ] CI passes
[ ] Pull Request merged
```

---

# 25. MVP Development Sequence

The agreed development sequence is:

```text
PHASE 0
Foundation
        ↓
PHASE 1
Authentication
        ↓
PHASE 2
Requirements
        ↓
PHASE 3
Candidates
        ↓
PHASE 4
Submission + Workflow
        ↓
PHASE 5
L1 Interview
        ↓
PHASE 6
Client Interview
        ↓
PHASE 7
AI Assistance
        ↓
PHASE 8
Dashboard
        ↓
PHASE 9
Testing + Polish + Deployment
```

---

# 26. MVP Success Criteria

The TalentFlow MVP should demonstrate that the team can design and build a complete enterprise-style full-stack application.

The finished system should demonstrate:

```text
Angular
        ↓
REST API
        ↓
Spring Boot
        ↓
Business Logic
        ↓
JPA / Hibernate
        ↓
MySQL
```

alongside:

```text
JWT
 ↓
Spring Security
 ↓
RBAC
```

and:

```text
Resume
 ↓
PDFBox
 ↓
Text
 ↓
Gemini
 ↓
AI Assistance
```

and:

```text
GitHub
 ↓
Pull Request
 ↓
GitHub Actions
 ↓
Build + Tests
 ↓
Merge
```

The result should be a coherent, working TalentFlow platform rather than a collection of technology demonstrations.

---

# 27. Foundation Freeze

Once the following are approved, the foundation will be considered frozen:

```text
[ ] MVP scope
[ ] Database design
[ ] System architecture
[ ] Backend architecture
[ ] Frontend architecture
[ ] AI architecture
[ ] Candidate workflow
[ ] API conventions
[ ] Security architecture
[ ] Development guidelines
[ ] Git workflow
[ ] CI strategy
```

After foundation freeze, changes should be introduced deliberately through normal development and, where appropriate, ADRs.

The team should avoid repeatedly redesigning the foundation while implementing features.

---

# 28. Current Status

```text
GitHub Repository             ✅
Development Environment       ✅
Git Workflow                  ✅
Development Guidelines        ✅
API Conventions               ✅
Flyway Strategy               ✅
Documentation Structure       ✅
MVP Definition                ✅
```

Remaining foundation work before feature development:

```text
Database Design                ⏳
ER Diagram                    ⏳
System Architecture           ⏳
Backend Architecture          ⏳
Frontend Architecture         ⏳
AI Architecture               ⏳
Candidate Workflow Document   ⏳
CI Configuration              ⏳
Spring Boot Bootstrap         ⏳
Angular Bootstrap             ⏳
```

---

# 29. Final Principle

TalentFlow should follow one simple rule:

> **Build the smallest complete system that demonstrates the entire talent fulfillment lifecycle, then improve it incrementally.**

Do not optimize for the number of features.

Optimize for:

```text
Architecture
+
Code Quality
+
Correct Workflow
+
Security
+
Testing
+
AI Assistance
+
Developer Experience
```

That is the TalentFlow MVP.
