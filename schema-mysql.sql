--
-- MySQL schema for the Guacamole Request Portal extension
--

CREATE TABLE IF NOT EXISTS guacamole_access_request (
    request_id VARCHAR(36) NOT NULL PRIMARY KEY,
    username VARCHAR(128) NOT NULL,
    target_environment VARCHAR(256) NOT NULL,
    reason_for_request TEXT NOT NULL,
    estimated_time INT NOT NULL,
    request_date DATETIME NOT NULL,
    status VARCHAR(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS guacamole_target_environment (
    environment_id VARCHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(256) NOT NULL UNIQUE,
    description TEXT
);