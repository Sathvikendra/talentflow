1. Purpose

Briefly explain what this document is for.

For example:

This document explains how to set up the TalentFlow development environment,
configure required dependencies, run the backend and frontend applications,
connect to the local MySQL database, and begin development.

Also mention:

Target developers:
- Sathvik
- Vinitha
2. Prerequisites

This is where our completed environment setup becomes useful.

We can document the versions we've actually chosen:

Java: 21 LTS
Node.js: 22.x
npm: 10.x
Angular CLI: 22.x
MySQL: 8.0.x
Git: Latest stable
VS Code: Latest stable

For your current machine we know the exact verified versions:

Java: OpenJDK 21.0.12.1
Node.js: 22.23.2
npm: 10.9.8
Angular CLI: 22.1.7
MySQL: 8.0.34

However, I would not make Vinitha install those exact patch versions unless we intentionally decide to pin them.

Instead, document the supported major/minor versions:

Java 21 LTS
Node.js 22.x
Angular CLI 22.x
MySQL 8.0.x

Then perhaps add:

Exact versions may be verified using the project's configuration files once the applications are bootstrapped.

That's more maintainable.

3. Clone Repository

Document the GitHub setup:

git clone <repository-url>
cd talentflow

Then:

git status

And explain that developers should start from main and create feature branches.

We don't need to put your personal GitHub credentials or tokens anywhere in this file.

4. Project Structure

Show the expected repository layout.

At the current preparation stage:

talentflow/
│
├── backend/
├── frontend/
├── database/
├── docs/
├── README.md
└── .gitignore

Then explain what each directory represents.

Later, after Spring Boot and Angular are generated, we'll expand this section.

For example:

backend/
    Spring Boot application

frontend/
    Angular application

database/
    Database-related project documentation/assets

docs/
    Architecture, API, workflow, setup and decision documentation
5. Backend Setup

This section will eventually contain things like:

cd backend/talentflow-api
./mvnw clean install

and:

./mvnw spring-boot:run

But don't put those commands in the document yet if the Spring Boot application doesn't exist.

For now:

Backend setup

The Spring Boot application will be generated during the project bootstrap phase.

Backend setup instructions will be updated after:
- Spring Boot project generation
- Maven Wrapper generation
- Database configuration
- Flyway configuration
- JWT configuration

This prevents our onboarding guide from containing imaginary commands.

6. Frontend Setup

Same principle.

Eventually we'll have something like:

cd frontend/talentflow-ui
npm install
ng serve

But the Angular project doesn't exist yet.

So currently:

Frontend setup instructions will be completed after the Angular
application is bootstrapped.

Once created, we'll document:

Node version
npm installation
Angular CLI
npm install
ng serve
development URL
build commands
test commands
7. MySQL Setup

This section is particularly useful for Vinitha.

We should explain:

Database:
MySQL 8.0

Local development:
Each developer uses a local MySQL instance.

Then eventually:

Database name:
talentflow

Application user:
talentflow_app

I recommend not using root from the application.

We'll create a dedicated application user when we actually configure the database.

Also document that database schema changes are handled by Flyway:

Do not manually modify the TalentFlow application schema
using MySQL Workbench.

Schema changes must be introduced through Flyway migrations.

That connects this document to our ADR-003 decision.

8. Environment Variables & Secrets

This section is critical.

Document the required variables:

DB_USERNAME=
DB_PASSWORD=
GEMINI_API_KEY=
JWT_SECRET=

Explain:

.env

is local-only and must never be committed.

And:

.env.example

can be committed with empty/placeholders.

For example:

DB_USERNAME=
DB_PASSWORD=
GEMINI_API_KEY=
JWT_SECRET=

Also explicitly state:

Never put actual passwords, API keys, JWT secrets, or personal access tokens in this repository.

9. Running the Application

Eventually this should become a nice copy-paste section:

Terminal 1:
cd backend/talentflow-api
./mvnw spring-boot:run

Terminal 2:
cd frontend/talentflow-ui
ng serve

Then:

Frontend:
http://localhost:4200

Backend:
http://localhost:8080

And eventually:

Swagger:
http://localhost:8080/swagger-ui/index.html

But again, we'll fill these in once the actual applications exist.

10. Running Tests

Eventually:

Backend
./mvnw test
Frontend

Something like:

npm test

or whatever testing setup we ultimately choose.

Don't hardcode this yet.

11. API Documentation

Document that TalentFlow will use:

OpenAPI / Swagger

Eventually we'll provide the actual URL.

Also point developers toward:

docs/api/API_CONVENTIONS.md

This creates a nice chain:

DEVELOPMENT_SETUP.md
        ↓
How do I run the project?
        ↓
API_CONVENTIONS.md
        ↓
How should I interact with the APIs?
12. Git Workflow

This should be very practical.

For example:

git checkout main
git pull origin main
git checkout -b feature/<feature-name>

Work:

git add .
git commit -m "feat: add candidate management"
git push -u origin feature/<feature-name>

Then:

Create Pull Request
        ↓
Code Review
        ↓
Merge into main

Also mention:

Do not directly push feature work to main.

This will be especially useful once Vinitha joins.

13. Troubleshooting

This is where the document becomes genuinely useful instead of being a ceremonial README wearing a tie. 😄

We can gradually add common problems.

For example:

Java version incorrect
Node/npm not found
Angular CLI not found
MySQL service not running
Database connection failed
Environment variable missing
Port already in use
Git branch conflicts

Don't fill it with solutions yet unless we've actually encountered and resolved them.

As we develop, whenever either of you hits a recurring setup issue, we add the solution here.

14. First-Day Checklist

This is something I strongly recommend for Vinitha.

A new developer should be able to tick:

[ ] Install Java 21
[ ] Install Node.js 22
[ ] Install Angular CLI 22
[ ] Install MySQL 8
[ ] Install Git
[ ] Install VS Code
[ ] Clone TalentFlow repository
[ ] Configure local environment variables
[ ] Configure MySQL
[ ] Run backend
[ ] Run frontend
[ ] Verify application
[ ] Run tests
[ ] Create feature branch

Later this becomes:

Vinitha can clone the repository
        ↓
Follow DEVELOPMENT_SETUP.md
        ↓
Application runs
        ↓
Ready to pick a feature

That's exactly what we want.