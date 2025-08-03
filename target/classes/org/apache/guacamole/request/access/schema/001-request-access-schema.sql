-- Request Access Extension Database Schema
-- This schema must be initialized AFTER the official Guacamole schemas

-- Create requests table
CREATE TABLE IF NOT EXISTS request_access_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    connection_id VARCHAR(255) NOT NULL,
    connection_name VARCHAR(255) NOT NULL,
    reason TEXT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    approved_by VARCHAR(255),
    approved_at DATETIME,
    rejection_reason TEXT,
    INDEX idx_user_id (user_id),
    INDEX idx_connection_id (connection_id),
    INDEX idx_status (status),
    INDEX idx_start_time (start_time),
    INDEX idx_end_time (end_time)
);

-- Create reservations table
CREATE TABLE IF NOT EXISTS request_access_reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    request_id INT NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    connection_id VARCHAR(255) NOT NULL,
    connection_name VARCHAR(255) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    status ENUM('ACTIVE', 'EXPIRED', 'CANCELLED') NOT NULL DEFAULT 'ACTIVE',
    reserved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reserved_by VARCHAR(255) NOT NULL,
    INDEX idx_request_id (request_id),
    INDEX idx_user_id (user_id),
    INDEX idx_connection_id (connection_id),
    INDEX idx_status (status),
    INDEX idx_start_time (start_time),
    INDEX idx_end_time (end_time),
    FOREIGN KEY (request_id) REFERENCES request_access_requests(request_id) ON DELETE CASCADE
);

-- Create approval history table
CREATE TABLE IF NOT EXISTS request_access_approval_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    request_id INT NOT NULL,
    action VARCHAR(50) NOT NULL,
    performed_by VARCHAR(255) NOT NULL,
    performed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comments TEXT,
    INDEX idx_request_id (request_id),
    INDEX idx_action (action),
    FOREIGN KEY (request_id) REFERENCES request_access_requests(request_id) ON DELETE CASCADE
);