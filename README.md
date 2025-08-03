# Guacamole Request Access Extension

An Apache Guacamole extension that adds a user-facing portal to request and reserve access to remote desktop environments.

## Features

- **User Request Portal**: Users can submit requests for access to specific connections
- **Time-based Reservations**: Reserve access for specific time periods
- **Approval Workflow**: Administrative approval system for access requests
- **Audit Trail**: Complete history of requests, approvals, and access sessions
- **Self-service Cancellation**: Users can cancel their own requests and reservations
- **Admin Panel**: Administrative interface for managing all requests and reservations

## Requirements

- Apache Guacamole 1.6.0
- JDK 1.8 or higher
- MySQL 8.0+ or PostgreSQL 12+
- Maven 3.6+

## Building the Extension

### Prerequisites

Ensure you have Java 8 and Maven installed:

```bash
java -version
mvn -version
```

### Build Steps

1. Clone the repository:
```bash
git clone https://github.com/zx900930/guacamole-request-access.git
cd guacamole-request-access
```

2. Build the extension:
```bash
mvn clean package
```

3. The built extension will be available in the `target/` directory:
   - `guacamole-request-access-1.6.0.jar` - Main extension file
   - `target/lib/` - Required dependencies

## Installation

### Using Docker (Recommended)

#### For MySQL

```bash
# Start the services
docker-compose up -d

# Initialize the database (first time only)
docker exec guacamole-request-access-db mysql -u root -prootpass guacamole_db < /path/to/initdb.sql

# Access Guacamole at http://localhost:8080
# Default credentials: guacadmin/guacadmin
```

#### For PostgreSQL

```bash
# Start the services
docker-compose -f docker-compose.postgresql.yml up -d

# Access Guacamole at http://localhost:8080
# Default credentials: guacadmin/guacadmin
```

### Manual Installation

1. Copy the extension JAR to Guacamole's extensions directory:
```bash
cp target/guacamole-request-access-1.6.0.jar /etc/guacamole/extensions/
```

2. Copy dependencies to Guacamole's lib directory:
```bash
cp -r target/lib/* /etc/guacamole/lib/
```

3. Initialize the database schema:
```bash
# For MySQL
mysql -u root -p guacamole_db < src/main/resources/org/apache/guacamole/request/access/schema/001-request-access-schema.sql
mysql -u root -p guacamole_db < src/main/resources/org/apache/guacamole/request/access/schema/002-request-access-data.sql

# For PostgreSQL
psql -U postgres -d guacamole_db -f src/main/resources/org/apache/guacamole/request/access/schema/001-request-access-schema.sql
psql -U postgres -d guacamole_db -f src/main/resources/org/apache/guacamole/request/access/schema/002-request-access-data.sql
```

4. Restart Guacamole:
```bash
systemctl restart tomcat9  # or your application server
```

## Configuration

### Database Configuration

The extension reads database configuration from Guacamole's `guacamole.properties` file:

#### MySQL Configuration
```properties
# MySQL database configuration
mysql-hostname: localhost
mysql-port: 3306
mysql-database: guacamole_db
mysql-username: guacamole_user
mysql-password: guacamole_pass
mysql-default-authentication-plugin: mysql_native_password
```

#### PostgreSQL Configuration
```properties
# PostgreSQL database configuration
postgresql-hostname: localhost
postgresql-port: 5432
postgresql-database: guacamole_db
postgresql-username: guacamole_user
postgresql-password: guacamole_pass
```

### Extension Configuration

Add the following to `guacamole.properties`:

```properties
# Request Access Extension configuration
request-access.admin-users: admin,user1,user2  # Users with admin privileges
request-access.max-duration: 360  # Maximum reservation duration in minutes
request-access.advance-notice: 1440  # Minimum advance notice in minutes
```

## Usage

### For Users

1. **Submit a Request**:
   - Navigate to the "Request Access" section in Guacamole
   - Select a connection from the available list
   - Specify the reason for access
   - Set the start and end times for your reservation
   - Submit the request

2. **View Your Requests**:
   - Check the status of your requests in the "My Requests" section
   - Cancel pending requests if needed

3. **Access Approved Connections**:
   - Once approved, the connection will appear in your main Guacamole interface
   - Connect during your reserved time slot

### For Administrators

1. **Review Requests**:
   - Access the "Admin Panel" to view all pending requests
   - Review request details and user information

2. **Approve or Reject**:
   - Approve requests that meet your organization's policies
   - Reject requests with appropriate reasons

3. **Monitor Usage**:
   - View active reservations and usage history
   - Generate reports for compliance and auditing

## API Endpoints

The extension provides REST API endpoints for integration:

### User Endpoints

- `POST /api/session/ext/guac-request-access/requests` - Submit new request
- `GET /api/session/ext/guac-request-access/requests/my` - Get user's requests
- `POST /api/session/ext/guac-request-access/requests/{id}/cancel` - Cancel request

### Admin Endpoints

- `GET /api/session/ext/guac-request-access/requests` - Get all requests
- `POST /api/session/ext/guac-request-access/requests/{id}/approve` - Approve request
- `POST /api/session/ext/guac-request-access/requests/{id}/reject` - Reject request
- `GET /api/session/ext/guac-request-access/connections` - Get available connections

## Database Schema

The extension creates the following tables:

### request_access_requests
Stores access request information:
- `request_id` - Primary key
- `user_id` - User identifier
- `username` - Username
- `connection_id` - Connection identifier
- `connection_name` - Connection display name
- `reason` - Reason for access request
- `start_time` - Requested start time
- `end_time` - Requested end time
- `status` - Request status (PENDING, APPROVED, REJECTED, CANCELLED)
- `submitted_at` - Submission timestamp
- `updated_at` - Last update timestamp
- `approved_by` - Admin who approved
- `approved_at` - Approval timestamp
- `rejection_reason` - Reason for rejection

### request_access_reservations
Stores reservation information:
- `reservation_id` - Primary key
- `request_id` - Foreign key to requests
- `user_id` - User identifier
- `username` - Username
- `connection_id` - Connection identifier
- `connection_name` - Connection display name
- `start_time` - Reservation start time
- `end_time` - Reservation end time
- `status` - Reservation status (ACTIVE, EXPIRED, CANCELLED)
- `reserved_at` - Reservation creation timestamp
- `reserved_by` - Who created the reservation

### request_access_approval_history
Stores approval history:
- `history_id` - Primary key
- `request_id` - Foreign key to requests
- `action` - Action performed (APPROVED, REJECTED, CANCELLED)
- `performed_by` - Who performed the action
- `performed_at` - Action timestamp
- `comments` - Additional comments

## Troubleshooting

### Common Issues

1. **Extension not loading**:
   - Check that the JAR file is in the correct extensions directory
   - Verify file permissions
   - Check Guacamole logs for error messages

2. **Database connection issues**:
   - Verify database configuration in `guacamole.properties`
   - Check database connectivity
   - Ensure the database schema is properly initialized

3. **Permission errors**:
   - Verify user permissions in the database
   - Check admin user configuration
   - Review system permission settings

### Logging

Check the following logs for troubleshooting:

- Guacamole logs: `/var/log/tomcat9/catalina.out`
- Application logs: Check your application server logs
- Database logs: MySQL or PostgreSQL error logs

### Debug Mode

Enable debug logging by adding to `guacamole.properties`:

```properties
# Enable debug logging
logback-level: DEBUG
```

## Development

### Project Structure

```
src/main/java/org/apache/guacamole/request/access/
├── injector/          # Dependency injection configuration
├── listener/          # Event listeners
├── model/            # Data models
├── rest/             # REST API endpoints
├── service/          # Business logic services
└── RequestAccessAuthenticationProvider.java  # Authentication provider

src/main/resources/org/apache/guacamole/request/access/
├── schema/           # Database schema files
├── scripts/          # JavaScript files
├── styles/           # CSS files
├── translations/     # Translation files
├── service/          # MyBatis mapper XML files
└── guac-manifest.json # Extension manifest
```

### Building and Testing

```bash
# Run tests
mvn test

# Build with dependencies
mvn package

# Clean and build
mvn clean package
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.

## Support

For issues and questions:
- Create an issue on GitHub
- Check the [Apache Guacamole documentation](https://guacamole.apache.org/doc/)
- Review the troubleshooting section above

## Changelog

### Version 1.6.0
- Initial release
- Support for Apache Guacamole 1.6.0
- MySQL and PostgreSQL support
- REST API for request management
- User and admin interfaces
- Approval workflow system
- Audit trail functionality