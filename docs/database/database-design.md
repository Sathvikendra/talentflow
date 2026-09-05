# TalentFlow Database Design

**Project:** TalentFlow
**Version:** V1 MVP
**Database:** MySQL 8
**ORM:** JPA / Hibernate
**Migration Tool:** Flyway

---

# 1. Purpose

This document defines the database architecture for TalentFlow V1.

The database is designed to support the complete talent fulfillment workflow:

```text
Client
   ↓
Requirement
   ↓
Candidate
   ↓
Candidate Submission
   ↓
L1 Interview
   ↓
Client Interview
   ↓
Acceptance / Rejection
   ↓
Requirement Filled
   ↓
Requirement Closed
```

The database must support:

* User authentication
* Role-based access control
* Client requirements
* Candidate profiles
* Resume storage metadata
* Candidate-to-requirement submissions
* Workflow state management
* L1 interviews
* Client interviews
* Status history
* AI-assisted assessment
* Notifications
* Dashboard analytics
* Auditability

---

# 2. Database Design Principles

TalentFlow follows these principles:

1. Every major business object has its own table.
2. Primary keys use generated numeric identifiers.
3. Foreign keys enforce relationships.
4. Business identifiers such as RR numbers are separate from database IDs.
5. Database names use `snake_case`.
6. Table names use `snake_case`.
7. Column names use `snake_case`.
8. Timestamps use UTC.
9. Database schema changes are managed through Flyway.
10. Hibernate must not automatically modify the schema.
11. Entities are not exposed directly through REST APIs.
12. Sensitive credentials must never be stored in plaintext.
13. Historical workflow changes must be preserved.
14. Deletion must not destroy important business history.
15. AI output must be distinguishable from human decisions.

---

# 3. Database Naming Convention

## Tables

Use singular logical entities represented using snake_case.

Examples:

```text
user
client
requirement
candidate
candidate_submission
l1_evaluation
client_interview
status_history
ai_assessment
notification
```

## Columns

Use:

```text
snake_case
```

Examples:

```text
first_name
created_at
scheduled_at
requirement_id
```

## Primary Keys

Standard:

```text
id
```

## Foreign Keys

Use:

```text
<entity>_id
```

Examples:

```text
client_id
candidate_id
requirement_id
submission_id
```

---

# 4. Entity Overview

The V1 database contains the following core entities:

```text
user
client
requirement
skill
candidate
resume_document
candidate_skill
requirement_skill
candidate_submission
l1_evaluation
client_interview
status_history
ai_assessment
notification
```

Relationship overview:

```text
                         ┌──────────────┐
                         │     User     │
                         └──────┬───────┘
                                │
                     ┌──────────┴──────────┐
                     ↓                     ↓
              L1 Evaluation          Notification


┌──────────────┐
│    Client    │
└──────┬───────┘
       │ 1:N
       ↓
┌──────────────┐
│ Requirement  │
└──────┬───────┘
       │
       │ N:M through requirement_skill
       ↓
┌──────────────┐
│    Skill     │
└──────────────┘


┌──────────────┐
│  Candidate   │
└──────┬───────┘
       │
       ├──────────────→ Resume Document
       │
       ├── N:M ───────→ Skill
       │
       ↓
Candidate Submission
       │
       ├──────────────→ Requirement
       │
       ├──────────────→ L1 Evaluation
       │
       ├──────────────→ Client Interview
       │
       ├──────────────→ Status History
       │
       └──────────────→ AI Assessment
```

---

# 5. User Table

## Purpose

Stores TalentFlow application users.

These are internal TalentFlow users such as:

* Administrators
* Reviewers
* Viewers

Candidates and clients are not application users in V1.

---

## Table

```text
user
```

## Columns

| Column        | Type         | Null | Description                 |
| ------------- | ------------ | ---: | --------------------------- |
| id            | BIGINT       |   NO | Primary key                 |
| username      | VARCHAR(100) |   NO | Unique login username       |
| email         | VARCHAR(255) |   NO | User email                  |
| password_hash | VARCHAR(255) |   NO | BCrypt/secure password hash |
| first_name    | VARCHAR(100) |   NO | First name                  |
| last_name     | VARCHAR(100) |   NO | Last name                   |
| role          | VARCHAR(30)  |   NO | ADMIN, REVIEWER, VIEWER     |
| is_active     | BOOLEAN      |   NO | Whether account is active   |
| last_login_at | DATETIME     |  YES | Last successful login       |
| created_at    | DATETIME     |   NO | Creation timestamp          |
| updated_at    | DATETIME     |   NO | Last modification timestamp |

## Constraints

```text
username UNIQUE
email UNIQUE
role NOT NULL
password_hash NOT NULL
```

## Important

Never store:

```text
plain_password
```

Only the password hash is stored.

---

# 6. Client Table

## Purpose

Represents the organization/client that raises a talent requirement.

This table is necessary because a Requirement should belong to a specific client.

---

## Table

```text
client
```

## Columns

| Column        | Type         | Null | Description                 |
| ------------- | ------------ | ---: | --------------------------- |
| id            | BIGINT       |   NO | Primary key                 |
| client_code   | VARCHAR(50)  |   NO | Internal client identifier  |
| client_name   | VARCHAR(255) |   NO | Organization/client name    |
| industry      | VARCHAR(100) |  YES | Industry/domain             |
| contact_name  | VARCHAR(150) |  YES | Client contact              |
| contact_email | VARCHAR(255) |  YES | Client contact email        |
| contact_phone | VARCHAR(30)  |  YES | Client contact phone        |
| notes         | TEXT         |  YES | Additional information      |
| is_active     | BOOLEAN      |   NO | Client active status        |
| created_at    | DATETIME     |   NO | Creation timestamp          |
| updated_at    | DATETIME     |   NO | Last modification timestamp |

## Constraints

```text
client_code UNIQUE
```

---

# 7. Requirement Table

## Purpose

Represents a client requirement/RR.

A requirement describes the position that TalentFlow needs to fulfill.

---

## Table

```text
requirement
```

## Columns

| Column               | Type         | Null | Description                     |
| -------------------- | ------------ | ---: | ------------------------------- |
| id                   | BIGINT       |   NO | Primary key                     |
| rr_number            | VARCHAR(50)  |   NO | Business requirement identifier |
| client_id            | BIGINT       |   NO | Client who raised requirement   |
| title                | VARCHAR(200) |   NO | Job/requirement title           |
| description          | TEXT         |   NO | Full requirement description    |
| employment_type      | VARCHAR(50)  |  YES | Full-time, contract, etc.       |
| location             | VARCHAR(200) |  YES | Work location                   |
| work_mode            | VARCHAR(30)  |   NO | ONSITE, HYBRID, REMOTE          |
| onshore_or_offshore  | VARCHAR(20)  |   NO | ONSHORE / OFFSHORE              |
| min_experience_years | DECIMAL(4,1) |  YES | Minimum experience              |
| max_experience_years | DECIMAL(4,1) |  YES | Maximum experience              |
| positions_count      | INT          |   NO | Number of openings              |
| priority             | VARCHAR(20)  |  YES | LOW, MEDIUM, HIGH, CRITICAL     |
| status               | VARCHAR(30)  |   NO | OPEN, FILLED, CLOSED            |
| created_by           | BIGINT       |   NO | User who created requirement    |
| opened_at            | DATETIME     |   NO | Requirement opening time        |
| target_fill_date     | DATE         |  YES | Desired fulfillment date        |
| closed_at            | DATETIME     |  YES | Requirement closure time        |
| created_at           | DATETIME     |   NO | Creation timestamp              |
| updated_at           | DATETIME     |   NO | Last modification timestamp     |

## Relationships

```text
Client 1 ─────── N Requirement

User 1 ───────── N Requirement
```

`created_by` references `user.id`.

## Important Business Rules

A requirement should not be permanently deleted after candidates have been associated with it.

Closing a requirement should update its status rather than delete its record.

---

# 8. Skill Table

## Purpose

Provides a controlled skill vocabulary.

Skills are used by:

* Requirements
* Candidates
* AI matching

---

## Table

```text
skill
```

## Columns

| Column          | Type         | Null | Description                        |
| --------------- | ------------ | ---: | ---------------------------------- |
| id              | BIGINT       |   NO | Primary key                        |
| name            | VARCHAR(100) |   NO | Skill name                         |
| normalized_name | VARCHAR(100) |   NO | Normalized searchable name         |
| category        | VARCHAR(100) |  YES | Programming, Cloud, Database, etc. |
| created_at      | DATETIME     |   NO | Creation timestamp                 |

## Constraints

```text
normalized_name UNIQUE
```

Example:

```text
Java
Spring Boot
Angular
MySQL
AWS
Docker
Python
```

---

# 9. Requirement Skill Table

## Purpose

Associates skills with a requirement.

A requirement may require multiple skills.

---

## Table

```text
requirement_skill
```

## Columns

| Column         | Type         | Null | Description              |
| -------------- | ------------ | ---: | ------------------------ |
| requirement_id | BIGINT       |   NO | Requirement              |
| skill_id       | BIGINT       |   NO | Skill                    |
| importance     | VARCHAR(20)  |   NO | REQUIRED / PREFERRED     |
| minimum_years  | DECIMAL(4,1) |  YES | Minimum skill experience |
| created_at     | DATETIME     |   NO | Creation timestamp       |

## Primary Key

Composite:

```text
(requirement_id, skill_id)
```

## Relationship

```text
Requirement N ───── N Skill
```

through:

```text
requirement_skill
```

---

# 10. Candidate Table

## Purpose

Stores candidate profile information.

---

## Table

```text
candidate
```

## Columns

| Column                 | Type         | Null | Description                   |
| ---------------------- | ------------ | ---: | ----------------------------- |
| id                     | BIGINT       |   NO | Primary key                   |
| candidate_reference    | VARCHAR(50)  |   NO | Internal candidate identifier |
| first_name             | VARCHAR(100) |   NO | First name                    |
| last_name              | VARCHAR(100) |  YES | Last name                     |
| email                  | VARCHAR(255) |   NO | Email                         |
| phone                  | VARCHAR(30)  |  YES | Phone                         |
| total_experience_years | DECIMAL(4,1) |  YES | Total experience              |
| current_title          | VARCHAR(200) |  YES | Current/most recent title     |
| current_company        | VARCHAR(255) |  YES | Current/most recent company   |
| current_location       | VARCHAR(200) |  YES | Candidate location            |
| preferred_location     | VARCHAR(200) |  YES | Preferred location            |
| notice_period_days     | INT          |  YES | Notice period                 |
| profile_summary        | TEXT         |  YES | Human/AI-assisted summary     |
| status                 | VARCHAR(30)  |   NO | ACTIVE, INACTIVE, WITHDRAWN   |
| created_by             | BIGINT       |   NO | User who created profile      |
| created_at             | DATETIME     |   NO | Creation timestamp            |
| updated_at             | DATETIME     |   NO | Last modification timestamp   |

## Constraints

```text
candidate_reference UNIQUE
```

Email should be indexed but should not necessarily be globally unique because duplicate or shared contact information may occur in dummy datasets.

---

# 11. Resume Document Table

## Purpose

Stores metadata about candidate resumes.

The actual PDF should not be stored directly in the relational database for V1 unless there is a specific requirement.

The database stores metadata and the storage location.

---

## Table

```text
resume_document
```

## Columns

| Column             | Type          | Null | Description                       |
| ------------------ | ------------- | ---: | --------------------------------- |
| id                 | BIGINT        |   NO | Primary key                       |
| candidate_id       | BIGINT        |   NO | Candidate                         |
| original_file_name | VARCHAR(255)  |   NO | Uploaded filename                 |
| stored_file_name   | VARCHAR(255)  |   NO | Internal storage filename         |
| storage_path       | VARCHAR(1000) |   NO | File storage location             |
| content_type       | VARCHAR(100)  |   NO | Expected application/pdf          |
| file_size_bytes    | BIGINT        |   NO | File size                         |
| file_hash          | VARCHAR(128)  |  YES | File integrity/deduplication hash |
| extracted_text     | LONGTEXT      |  YES | Text extracted from PDF           |
| parsing_status     | VARCHAR(30)   |   NO | PENDING, COMPLETED, FAILED        |
| uploaded_by        | BIGINT        |   NO | User who uploaded resume          |
| uploaded_at        | DATETIME      |   NO | Upload timestamp                  |
| created_at         | DATETIME      |   NO | Creation timestamp                |

## Relationship

```text
Candidate 1 ───── N ResumeDocument
```

V1 can designate one resume as the current/active resume at the application level.

---

# 12. Candidate Skill Table

## Purpose

Associates skills with a candidate.

Skills may come from:

* User-entered candidate information
* Resume extraction
* AI extraction

---

## Table

```text
candidate_skill
```

## Columns

| Column           | Type         | Null | Description            |
| ---------------- | ------------ | ---: | ---------------------- |
| candidate_id     | BIGINT       |   NO | Candidate              |
| skill_id         | BIGINT       |   NO | Skill                  |
| proficiency      | VARCHAR(30)  |  YES | Optional proficiency   |
| years_experience | DECIMAL(4,1) |  YES | Skill experience       |
| source           | VARCHAR(30)  |   NO | MANUAL / RESUME / AI   |
| created_at       | DATETIME     |   NO | Creation timestamp     |
| updated_at       | DATETIME     |   NO | Modification timestamp |

## Primary Key

```text
(candidate_id, skill_id)
```

---

# 13. Candidate Submission Table

## Purpose

Represents a candidate being considered for a specific requirement.

This is one of the most important tables in TalentFlow.

A candidate can be submitted to multiple requirements.

A requirement can have multiple candidate submissions.

---

## Table

```text
candidate_submission
```

## Columns

| Column           | Type        | Null | Description                  |
| ---------------- | ----------- | ---: | ---------------------------- |
| id               | BIGINT      |   NO | Primary key                  |
| candidate_id     | BIGINT      |   NO | Candidate                    |
| requirement_id   | BIGINT      |   NO | Requirement                  |
| current_status   | VARCHAR(30) |   NO | Current workflow state       |
| submitted_by     | BIGINT      |   NO | User who submitted candidate |
| assigned_to      | BIGINT      |  YES | Reviewer responsible         |
| submission_notes | TEXT        |  YES | Submission notes             |
| assigned_at      | DATETIME    |  YES | Assignment time              |
| created_at       | DATETIME    |   NO | Submission time              |
| updated_at       | DATETIME    |   NO | Last modification time       |

## Unique Constraint

Recommended:

```text
(candidate_id, requirement_id)
```

This prevents accidentally creating multiple active submissions of the same candidate for the same requirement.

If future business rules allow resubmission after withdrawal/rejection, this constraint can be revisited.

---

# 14. Workflow Status

The `candidate_submission.current_status` column stores:

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

The workflow itself is not controlled merely by changing this column.

The application service layer must validate every transition.

---

# 15. L1 Evaluation Table

## Purpose

Stores the human evaluation performed during the L1 interview.

---

## Table

```text
l1_evaluation
```

## Columns

| Column             | Type          | Null | Description                   |
| ------------------ | ------------- | ---: | ----------------------------- |
| id                 | BIGINT        |   NO | Primary key                   |
| submission_id      | BIGINT        |   NO | Candidate submission          |
| interviewer_id     | BIGINT        |   NO | Internal interviewer          |
| scheduled_at       | DATETIME      |  YES | Interview time                |
| duration_minutes   | INT           |  YES | Expected duration             |
| meeting_link       | VARCHAR(1000) |  YES | Meeting URL                   |
| conducted_at       | DATETIME      |  YES | Actual interview time         |
| result             | VARCHAR(30)   |  YES | CLEARED / REJECTED            |
| technical_comments | TEXT          |  YES | Technical observations        |
| overall_comments   | TEXT          |  YES | Interview comments            |
| recommendation     | VARCHAR(30)   |  YES | Optional human recommendation |
| created_at         | DATETIME      |   NO | Creation timestamp            |
| updated_at         | DATETIME      |   NO | Last modification timestamp   |

## Relationship

```text
CandidateSubmission 1 ───── N L1Evaluation
User 1 ──────────────────── N L1Evaluation
```

Although V1 may normally have one L1 evaluation per submission, the table supports historical evaluations if an interview must be rescheduled or repeated.

---

# 16. Client Interview Table

## Purpose

Stores the client-side interview process.

The client does not need to be an authenticated TalentFlow user in V1.

---

## Table

```text
client_interview
```

## Columns

| Column                   | Type          | Null | Description                 |
| ------------------------ | ------------- | ---: | --------------------------- |
| id                       | BIGINT        |   NO | Primary key                 |
| submission_id            | BIGINT        |   NO | Candidate submission        |
| client_interviewer_name  | VARCHAR(150)  |  YES | Client interviewer          |
| client_interviewer_email | VARCHAR(255)  |  YES | Client interviewer email    |
| scheduled_at             | DATETIME      |  YES | Scheduled interview         |
| duration_minutes         | INT           |  YES | Expected duration           |
| meeting_link             | VARCHAR(1000) |  YES | Meeting URL                 |
| conducted_at             | DATETIME      |  YES | Actual interview time       |
| result                   | VARCHAR(30)   |  YES | ACCEPTED / REJECTED         |
| comments                 | TEXT          |  YES | Client interview comments   |
| created_at               | DATETIME      |   NO | Creation timestamp          |
| updated_at               | DATETIME      |   NO | Last modification timestamp |

## Relationship

```text
CandidateSubmission 1 ───── N ClientInterview
```

---

# 17. Status History Table

## Purpose

Provides a complete audit trail of candidate workflow transitions.

This table is mandatory for TalentFlow.

---

## Table

```text
status_history
```

## Columns

| Column            | Type         | Null | Description           |
| ----------------- | ------------ | ---: | --------------------- |
| id                | BIGINT       |   NO | Primary key           |
| submission_id     | BIGINT       |   NO | Candidate submission  |
| old_status        | VARCHAR(30)  |  YES | Previous state        |
| new_status        | VARCHAR(30)  |   NO | New state             |
| changed_by        | BIGINT       |   NO | User responsible      |
| transition_reason | VARCHAR(255) |  YES | Reason for transition |
| comments          | TEXT         |  YES | Additional comments   |
| changed_at        | DATETIME     |   NO | Transition timestamp  |

## Relationship

```text
CandidateSubmission 1 ───── N StatusHistory
User 1 ──────────────────── N StatusHistory
```

## Example

```text
PROFILE_RECEIVED
       ↓
L1_PENDING
```

creates:

```text
old_status = PROFILE_RECEIVED
new_status = L1_PENDING
changed_by = <user>
changed_at = <timestamp>
```

The status history must never be overwritten.

---

# 18. AI Assessment Table

## Purpose

Stores AI-generated assistance associated with a candidate submission.

AI output is not a hiring decision.

---

## Table

```text
ai_assessment
```

## Columns

| Column              | Type         | Null | Description                   |
| ------------------- | ------------ | ---: | ----------------------------- |
| id                  | BIGINT       |   NO | Primary key                   |
| submission_id       | BIGINT       |   NO | Candidate submission          |
| resume_document_id  | BIGINT       |  YES | Resume used for analysis      |
| model_name          | VARCHAR(100) |  YES | Gemini model used             |
| prompt_version      | VARCHAR(30)  |  YES | Prompt/template version       |
| assessment_status   | VARCHAR(30)  |   NO | PENDING, COMPLETED, FAILED    |
| matched_skills      | JSON         |  YES | Matched skills                |
| missing_skills      | JSON         |  YES | Missing/unverified skills     |
| evidence            | JSON         |  YES | Resume evidence               |
| interview_questions | JSON         |  YES | Suggested interview questions |
| summary             | TEXT         |  YES | AI-generated summary          |
| raw_response        | JSON         |  YES | Structured AI response        |
| error_message       | TEXT         |  YES | Error details                 |
| created_at          | DATETIME     |   NO | Assessment creation time      |
| completed_at        | DATETIME     |  YES | Completion time               |

## Important

AI output must be treated as assistive information.

It must never be interpreted by the database as:

```text
hire
reject
rank
score
select
```

There is intentionally no:

```text
ai_decision
```

column.

---

# 19. Notification Table

## Purpose

Stores application notifications.

Examples:

* New candidate submitted
* L1 interview scheduled
* L1 evaluation completed
* Client interview scheduled
* Candidate accepted
* Candidate rejected
* Requirement closed

---

## Table

```text
notification
```

## Columns

| Column         | Type          | Null | Description                              |
| -------------- | ------------- | ---: | ---------------------------------------- |
| id             | BIGINT        |   NO | Primary key                              |
| user_id        | BIGINT        |   NO | Recipient                                |
| type           | VARCHAR(50)   |   NO | Notification category                    |
| message        | VARCHAR(1000) |   NO | Notification text                        |
| reference_type | VARCHAR(50)   |  YES | Requirement, submission, interview, etc. |
| reference_id   | BIGINT        |  YES | Related entity ID                        |
| is_read        | BOOLEAN       |   NO | Read state                               |
| created_at     | DATETIME      |   NO | Creation timestamp                       |
| read_at        | DATETIME      |  YES | Read timestamp                           |

## Relationship

```text
User 1 ───── N Notification
```

---

# 20. Complete Relationship Model

The primary relationships are:

```text
Client
  │
  │ 1:N
  ↓
Requirement
  │
  │ N:M
  ↓
Skill
```

```text
Candidate
  │
  ├── 1:N ── ResumeDocument
  │
  └── N:M ── Skill
```

```text
Candidate
      │
      │ 1:N
      ↓
CandidateSubmission
      │
      ├── N:1 ── Requirement
      │
      ├── 1:N ── L1Evaluation
      │
      ├── 1:N ── ClientInterview
      │
      ├── 1:N ── StatusHistory
      │
      └── 1:N ── AiAssessment
```

```text
User
  │
  ├── 1:N → Requirement
  ├── 1:N → Candidate
  ├── 1:N → CandidateSubmission
  ├── 1:N → L1Evaluation
  ├── 1:N → StatusHistory
  ├── 1:N → ResumeDocument
  └── 1:N → Notification
```

---

# 21. Foreign Key Summary

| Child Table          | Foreign Key        | Parent               |
| -------------------- | ------------------ | -------------------- |
| requirement          | client_id          | client               |
| requirement          | created_by         | user                 |
| requirement_skill    | requirement_id     | requirement          |
| requirement_skill    | skill_id           | skill                |
| candidate            | created_by         | user                 |
| resume_document      | candidate_id       | candidate            |
| resume_document      | uploaded_by        | user                 |
| candidate_skill      | candidate_id       | candidate            |
| candidate_skill      | skill_id           | skill                |
| candidate_submission | candidate_id       | candidate            |
| candidate_submission | requirement_id     | requirement          |
| candidate_submission | submitted_by       | user                 |
| candidate_submission | assigned_to        | user                 |
| l1_evaluation        | submission_id      | candidate_submission |
| l1_evaluation        | interviewer_id     | user                 |
| client_interview     | submission_id      | candidate_submission |
| status_history       | submission_id      | candidate_submission |
| status_history       | changed_by         | user                 |
| ai_assessment        | submission_id      | candidate_submission |
| ai_assessment        | resume_document_id | resume_document      |
| notification         | user_id            | user                 |

---

# 22. Audit Fields

Major transactional tables should contain:

```text
created_at
updated_at
```

Where ownership is relevant:

```text
created_by
updated_by
```

Not every table requires every audit column.

Historical tables such as `status_history` use their own event timestamp.

All application timestamps represent UTC instants.

---

# 23. Primary Key Strategy

All main entities use:

```text
BIGINT
```

with database-generated IDs.

Example:

```text
id BIGINT AUTO_INCREMENT PRIMARY KEY
```

Business identifiers should not be used as primary keys.

Examples:

```text
rr_number
candidate_reference
client_code
```

are business identifiers and remain separate from database IDs.

---

# 24. Index Strategy

Indexes should support the most common application queries.

## User

```text
UNIQUE(username)
UNIQUE(email)
INDEX(role)
INDEX(is_active)
```

## Client

```text
UNIQUE(client_code)
INDEX(client_name)
INDEX(is_active)
```

## Requirement

```text
UNIQUE(rr_number)
INDEX(client_id)
INDEX(status)
INDEX(created_at)
INDEX(target_fill_date)
```

## Candidate

```text
UNIQUE(candidate_reference)
INDEX(email)
INDEX(status)
INDEX(created_at)
```

## Submission

```text
INDEX(candidate_id)
INDEX(requirement_id)
INDEX(current_status)
INDEX(assigned_to)
INDEX(created_at)
```

## Status History

```text
INDEX(submission_id, changed_at)
INDEX(changed_by)
```

## L1 Evaluation

```text
INDEX(submission_id)
INDEX(interviewer_id)
INDEX(scheduled_at)
INDEX(result)
```

## Client Interview

```text
INDEX(submission_id)
INDEX(scheduled_at)
INDEX(result)
```

## AI Assessment

```text
INDEX(submission_id)
INDEX(assessment_status)
INDEX(created_at)
```

## Notification

```text
INDEX(user_id, is_read)
INDEX(created_at)
```

---

# 25. Delete Strategy

TalentFlow should avoid destructive deletion of business records.

## User

Do not delete users who have created historical records.

Use:

```text
is_active = false
```

## Client

Use:

```text
is_active = false
```

## Requirement

Use status:

```text
CLOSED
```

rather than deleting.

## Candidate

Prefer:

```text
status = INACTIVE
```

or:

```text
status = WITHDRAWN
```

depending on business context.

## Candidate Submission

Do not delete completed workflow history.

Use:

```text
WITHDRAWN
```

where appropriate.

## Status History

Never delete workflow history as part of normal application operation.

---

# 26. Data Required From the Client

When a client creates a requirement, TalentFlow needs to capture enough information to search for and evaluate candidates.

## Client Information

```text
Client name
Client code
Industry
Client contact name
Client contact email
Client contact phone
Additional notes
```

## Requirement Information

```text
RR number
Job title
Job description
Required skills
Preferred skills
Minimum experience
Maximum experience
Number of openings
Location
Work mode
Onshore / Offshore
Employment type
Priority
Requirement opening date
Target fill date
```

## Skills

For each skill:

```text
Skill name
Importance
Minimum experience
```

Example:

```text
Java
REQUIRED
3 years

AWS
PREFERRED
1 year

Spring Boot
REQUIRED
2 years
```

---

# 27. Data Required From TalentFlow Users

Internal users need to provide:

```text
Username
Email
Password
First name
Last name
Role
```

For requirements:

```text
Requirement information
```

For candidates:

```text
Candidate profile
Resume
Submission information
Interview information
Evaluation information
```

---

# 28. Candidate Data Required

Candidate creation should capture:

```text
First name
Last name
Email
Phone
Total experience
Current title
Current company
Current location
Preferred location
Notice period
Resume
```

Additional skills may be:

```text
Manually entered
Extracted from resume
Extracted by AI
```

The source should be tracked.

---

# 29. L1 Interview Data Required

The interviewer should provide:

```text
Candidate submission
Interviewer
Scheduled date/time
Duration
Meeting link
Technical observations
Overall comments
Result
```

Result:

```text
CLEARED
REJECTED
```

The result is a human decision.

---

# 30. Client Interview Data Required

TalentFlow should capture:

```text
Candidate submission
Client interviewer name
Client interviewer email
Scheduled date/time
Duration
Meeting link
Interview comments
Result
```

Result:

```text
ACCEPTED
REJECTED
```

---

# 31. AI Data Flow

AI processing requires:

```text
Candidate Resume
        +
Requirement
        +
Candidate Skills
        ↓
AI Service
        ↓
Gemini
```

The AI response can contain:

```text
Matched skills
Missing/unverified skills
Evidence
Suggested interview questions
Summary
```

The database stores the structured result in `ai_assessment`.

---

# 32. AI Evidence Model

AI output should distinguish between:

```text
Evidence Found
```

and:

```text
No Clear Evidence Found
```

For example:

```json
{
  "skill": "AWS",
  "evidence": null,
  "status": "NO_CLEAR_EVIDENCE"
}
```

This is preferable to storing:

```text
candidate_lacks_aws = true
```

because absence of evidence in a resume does not necessarily prove absence of the skill.

---

# 33. Workflow and Database Responsibilities

The database stores the current state:

```text
candidate_submission.current_status
```

The database also stores the history:

```text
status_history
```

The Spring Boot service layer is responsible for determining whether a transition is valid.

Example:

```text
L1_PENDING
     ↓
L1_SCHEDULED
```

Valid.

But:

```text
L1_PENDING
     ↓
CI_ACCEPTED
```

Invalid.

The database should enforce structural integrity through foreign keys and constraints.

The application service should enforce workflow/business rules.

---

# 34. Requirement Status

Requirement status:

```text
OPEN
FILLED
CLOSED
```

Suggested lifecycle:

```text
OPEN
 ↓
FILLED
 ↓
CLOSED
```

A requirement may also be closed without being filled if the business process allows cancellation.

If that behavior is required, we should later introduce:

```text
CANCELLED
```

rather than abusing `CLOSED`.

---

# 35. Candidate Status

Candidate status represents the general profile state, not the candidate's status for a particular requirement.

Suggested values:

```text
ACTIVE
INACTIVE
WITHDRAWN
```

The candidate's progress for a specific requirement is stored in:

```text
candidate_submission.current_status
```

This distinction is important.

A candidate can be:

```text
ACTIVE
```

while simultaneously being:

```text
L1_PENDING
```

for one requirement and:

```text
CI_REJECTED
```

for another.

---

# 36. Why CandidateSubmission Is Necessary

We must not put:

```text
requirement_id
current_status
interview_status
```

directly inside `candidate`.

A candidate can participate in multiple requirements.

Therefore:

```text
Candidate
     │
     ├── Submission → Requirement A
     │
     ├── Submission → Requirement B
     │
     └── Submission → Requirement C
```

Each submission has its own workflow.

This is the central relationship of TalentFlow.

---

# 37. Why Skills Are Separate Entities

We should not store skills as:

```text
candidate.skills = "Java, Spring Boot, Angular, AWS"
```

or:

```text
requirement.skills = "Java, AWS, MySQL"
```

because this makes searching and matching unnecessarily difficult.

Instead:

```text
Candidate
    ↓
CandidateSkill
    ↓
Skill
```

and:

```text
Requirement
    ↓
RequirementSkill
    ↓
Skill
```

This allows TalentFlow to query:

```text
Which candidates have Java?
Which requirements require AWS?
Which candidates match the required skills?
```

without parsing comma-separated strings.

---

# 38. Why AI Assessment Is Separate

AI analysis should not modify the candidate's permanent profile directly.

Instead:

```text
Candidate
   +
Requirement
   +
Resume
   ↓
AI Assessment
```

This allows:

* Re-running an assessment
* Comparing AI outputs
* Tracking model versions
* Tracking prompt versions
* Debugging AI failures
* Preserving historical results

The AI assessment therefore becomes an auditable artifact.

---

# 39. Data Ownership

| Data              | Primary Owner                           |
| ----------------- | --------------------------------------- |
| User account      | TalentFlow                              |
| Client            | TalentFlow                              |
| Requirement       | TalentFlow / Client input               |
| Candidate profile | TalentFlow / recruiter input            |
| Resume            | Candidate-provided / recruiter-uploaded |
| Submission        | TalentFlow                              |
| L1 evaluation     | Interviewer                             |
| Client interview  | Client / reviewer                       |
| Workflow status   | TalentFlow                              |
| Status history    | TalentFlow                              |
| AI assessment     | TalentFlow AI service                   |
| Notification      | TalentFlow                              |

---

# 40. Sensitive Data Considerations

TalentFlow should minimize unnecessary personal information.

The V1 database should not store unnecessary information such as:

```text
Government identification numbers
Bank information
Medical information
Passwords in plaintext
Personal financial information
```

Only information required for the MVP workflow should be captured.

---

# 41. Resume Storage Considerations

The database stores:

```text
filename
storage path
content type
file size
hash
extracted text
parsing status
```

The actual PDF should be stored separately from the relational database.

For local development, a filesystem-based storage strategy can be used.

A future production deployment can replace this with object storage without fundamentally changing the candidate data model.

---

# 42. Database Transaction Boundaries

Important operations should execute transactionally.

For example, a workflow transition should conceptually perform:

```text
Validate transition
       ↓
Update current_status
       ↓
Insert status_history
       ↓
Create notification
       ↓
Commit
```

These operations should not leave the system in a partially updated state.

Spring's transaction management will handle this at the service layer.

---

# 43. Referential Integrity

Foreign keys should be enforced for all major relationships.

Examples:

```text
requirement.client_id
    → client.id
```

```text
candidate_submission.candidate_id
    → candidate.id
```

```text
candidate_submission.requirement_id
    → requirement.id
```

```text
status_history.submission_id
    → candidate_submission.id
```

This prevents orphaned business records.

---

# 44. Initial Database Table List

The proposed V1 schema contains:

```text
1.  user
2.  client
3.  requirement
4.  skill
5.  requirement_skill
6.  candidate
7.  resume_document
8.  candidate_skill
9.  candidate_submission
10. l1_evaluation
11. client_interview
12. status_history
13. ai_assessment
14. notification
```

---

# 45. Tables Deliberately Not Included

The following are intentionally excluded from V1:

```text
candidate_application
job_board
linkedin_profile
email_message
calendar_event
offer
employee
payroll
background_check
client_user
recruiter_agency
```

They are outside the MVP scope.

---

# 46. Future Extensions

The schema should leave room for future functionality without implementing it now.

Possible future entities:

```text
candidate_experience
candidate_education
candidate_certification
interview_feedback
offer
calendar_event
email_log
client_user
audit_log
```

These should only be introduced when an actual V2 requirement exists.

---

# 47. Database Design Decision Summary

The V1 database intentionally separates:

```text
Candidate
```

from:

```text
CandidateSubmission
```

and:

```text
Requirement
```

from:

```text
RequirementSkill
```

and:

```text
Candidate
```

from:

```text
CandidateSkill
```

and:

```text
ResumeDocument
```

from:

```text
Candidate
```

and:

```text
AI Assessment
```

from:

```text
CandidateSubmission
```

This produces a normalized model while remaining simple enough for a modular monolith.

---

# 48. Final V1 Data Model

The conceptual model is:

```text
                              USER
                         /      |      \
                        /       |       \
                       ↓        ↓        ↓
                REQUIREMENT  CANDIDATE  NOTIFICATION
                    │            │
                    │            ├────────→ RESUME_DOCUMENT
                    │            │
                    ↓            ↓
             REQUIREMENT_SKILL  CANDIDATE_SKILL
                    │            │
                    └────→ SKILL ←┘

CLIENT
   │
   └──────→ REQUIREMENT
                │
                │
                ↓
       CANDIDATE_SUBMISSION
          │      │      │
          │      │      ├────→ AI_ASSESSMENT
          │      │
          │      ├──────────→ CLIENT_INTERVIEW
          │
          ├────────────────→ L1_EVALUATION
          │
          └────────────────→ STATUS_HISTORY
```

---

# 49. Database Design Status

```text
Entity identification          ✅
Primary keys                   ✅
Foreign keys                   ✅
Relationships                  ✅
Required fields                ✅
Optional fields                ✅
Workflow status                ✅
Audit fields                   ✅
Indexes                        ✅
Delete strategy                ✅
Resume strategy                ✅
AI data strategy               ✅
Client data                    ✅
Candidate data                 ✅
Interview data                 ✅
Notification data              ✅
Security-sensitive data        ✅
```

The next step is to review this model before generating any SQL.

---

# 50. Next Step

Do **not** create:

```text
V1__initial_schema.sql
```

yet.

The next step is:

```text
DATABASE DESIGN
       ↓
ARCHITECTURAL REVIEW
       ↓
ER DIAGRAM
       ↓
RELATIONSHIP REVIEW
       ↓
CONSTRAINT / INDEX REVIEW
       ↓
V1__initial_schema.sql
```

The database design becomes the source from which the ER diagram and initial Flyway migration are derived.

---

**TalentFlow Database Design V1**

The database should model the business accurately while remaining intentionally small enough for the TalentFlow MVP.


