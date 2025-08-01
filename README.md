# Guacamole Request Access Extension

An Apache Guacamole extension that adds a user-facing portal to request and reserve access to remote desktop environments.

## Features

*   Users can request access to predefined environments.
*   Administrators can approve or deny requests.
*   Administrators can manage the list of available environments.
*   Export request data to CSV or XLSX.

## Building

To build the extension, you will need:

*   Java 1.8+
*   Maven 3+

Run the following command to build the extension:

```
mvn clean package
```

This will create a `.jar` file in the `target` directory. This is the extension file that you will deploy to Guacamole.

## Deployment

1.  Copy the `guacamole-request-access-1.5.3-jar-with-dependencies.jar` file from the `target` directory to the Guacamole `extensions` directory.
2.  Restart Tomcat (or your servlet container).

## Development

A `docker-compose.yml` file is provided for setting up a development environment. To use it, you will need:

*   Docker
*   Docker Compose

Run the following command to start the development environment:

```
docker-compose up -d
```

This will start the following services:

*   `guacd`
*   `postgres`
*   `guacamole`

The Guacamole web application will be available at `http://localhost:8080/guacamole`.

The extension is mounted as a volume, so you can make changes to the source code and see them reflected in the running application without having to rebuild and redeploy the extension.
