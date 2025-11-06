-- Create databases
CREATE DATABASE IF NOT EXISTS auth_db;
CREATE DATABASE IF NOT EXISTS sample_db;

-- Use auth_db for initial setup
USE auth_db;

-- Create authorities table
CREATE TABLE IF NOT EXISTS authorities (
    name VARCHAR(50) NOT NULL PRIMARY KEY
);

-- Insert default authorities
INSERT IGNORE INTO authorities (name) VALUES ('ROLE_ADMIN');
INSERT IGNORE INTO authorities (name) VALUES ('ROLE_USER');

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(254) UNIQUE,
    activated BOOLEAN NOT NULL DEFAULT FALSE
);

-- Create user_authority junction table
CREATE TABLE IF NOT EXISTS user_authority (
    user_id BIGINT NOT NULL,
    authority_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, authority_name),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (authority_name) REFERENCES authorities(name) ON DELETE CASCADE
);

-- Insert default admin user (password: admin123)
INSERT IGNORE INTO users (username, password, first_name, last_name, email, activated) 
VALUES ('admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIpBnEph0ya80jQ1ELrG', 'Admin', 'User', 'admin@example.com', TRUE);

-- Assign admin role to admin user
INSERT IGNORE INTO user_authority (user_id, authority_name) 
SELECT u.id, 'ROLE_ADMIN' FROM users u WHERE u.username = 'admin';

-- Insert default regular user (password: user123)
INSERT IGNORE INTO users (username, password, first_name, last_name, email, activated) 
VALUES ('user', '$2a$10$VEjxo0jq2YG5RBVHag/xfeVHEhJNNOhEqkJxnDdRgMBtQx6x9/1uy', 'Regular', 'User', 'user@example.com', TRUE);

-- Assign user role to regular user
INSERT IGNORE INTO user_authority (user_id, authority_name) 
SELECT u.id, 'ROLE_USER' FROM users u WHERE u.username = 'user';
