# Guacamole Request Portal Extension

A Guacamole extension for moderated access requests and reservations.

## Project Objective

This extension provides a moderated request and reservation system for remote desktop access, with support for both PostgreSQL and MySQL databases.

## Development

### Prerequisites

*   Java Development Kit (JDK) 8 or higher
*   Apache Maven
*   Docker and Docker Compose

### Building the Project

To build the `.jar` file, navigate to the project root directory and run:

```bash
mvn clean package
```

This will generate `guac-request-portal-1.0.0.jar` in the `target/` directory.

### Running with Docker Compose

This project includes a `docker-compose.yml` file to set up a full Guacamole environment with PostgreSQL as the backend database. The extension will be automatically mounted into the Guacamole container.

To start the environment:

```bash
docker compose up --build
```

To stop the environment:

```bash
docker compose down
```

### Accessing the Guacamole UI

Once the Docker containers are running, you can access the Guacamole web interface at `http://localhost:8080/guacamole/`.

## Core Functional Requirements (Planned)

*   **Frontend Implementation:** Custom HTML pages for user requests and administrator management, following the `guacamole-branding-example` pattern.
*   **User-Facing UI:** A custom HTML page for users to submit access requests with fields for "Target Environment", "Reason for Request", and "Estimated Time".
*   **Administrator-Facing UI:** New settings tabs/pages for administrators to manage requests (Approve/Deny) and configure "Target Environments".
*   **Database Support:** Compatibility with both MySQL and PostgreSQL, with separate schema files (`schema-mysql.sql`, `schema-postgres.sql`).
*   **Data Export:** CSV and XLSX export functionality for access requests on the admin management page.
*   **Internationalization (i18n):** UI text externalized using Guacamole's standard JSON translation files (`en.json`, `zh.json`).
