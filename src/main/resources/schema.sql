-- Create database
CREATE DATABASE IF NOT EXISTS PCM;
USE PCM;

-- Users table
CREATE TABLE IF NOT EXISTS Users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'TECHNICIAN') NOT NULL,
    status ENUM('AVAILABLE', 'NOT_AVAILABLE', 'BUSY') DEFAULT 'AVAILABLE'
);

-- PC table
CREATE TABLE IF NOT EXISTS PC (
    id INT PRIMARY KEY AUTO_INCREMENT,
    brand VARCHAR(50) NOT NULL,
    hdd VARCHAR(50),
    ram VARCHAR(50),
    os VARCHAR(50),
    registration_year INT,
    status ENUM('WORKING', 'NOT_WORKING', 'DAMAGED', 'OLD') NOT NULL,
    location ENUM('LAB', 'OFFICE') NOT NULL,
    technician_id INT,
    FOREIGN KEY (technician_id) REFERENCES Users(id) ON DELETE SET NULL
);

-- Accessories table
CREATE TABLE IF NOT EXISTS Accessories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type ENUM('MOUSE', 'KEYBOARD', 'MONITOR', 'PROJECTOR') NOT NULL,
    brand VARCHAR(50),
    registration_year INT,
    status ENUM('WORKING', 'NOT_WORKING', 'DAMAGED', 'OLD') NOT NULL,
    location ENUM('LAB', 'OFFICE') NOT NULL,
    technician_id INT,
    FOREIGN KEY (technician_id) REFERENCES Users(id) ON DELETE SET NULL
);

-- Network Devices table
CREATE TABLE IF NOT EXISTS NetworkDevices (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type ENUM('ACCESS_POINT', 'SWITCH', 'ROUTER') NOT NULL,
    brand VARCHAR(50),
    registration_year INT,
    status ENUM('WORKING', 'NOT_WORKING', 'DAMAGED', 'OLD') NOT NULL,
    location ENUM('LAB', 'OFFICE') NOT NULL,
    technician_id INT,
    FOREIGN KEY (technician_id) REFERENCES Users(id) ON DELETE SET NULL
);

-- Reporting table
CREATE TABLE IF NOT EXISTS Reporting (
    id INT PRIMARY KEY AUTO_INCREMENT,
    device_type ENUM('PC', 'ACCESSORY', 'NETWORK_DEVICE') NOT NULL,
    device_id INT NOT NULL,
    status ENUM('WORKING', 'NOT_WORKING', 'DAMAGED', 'OLD') NOT NULL,
    report_date DATE NOT NULL,
    location ENUM('LAB', 'OFFICE') NOT NULL,
    technician_id INT,
    FOREIGN KEY (technician_id) REFERENCES Users(id) ON DELETE SET NULL
);

-- Requests table
CREATE TABLE IF NOT EXISTS Requests (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    request_date DATE NOT NULL,
    unit VARCHAR(50) NOT NULL,
    status ENUM('PENDING', 'TECHNICIAN_ASSIGNED', 'FIXED', 'NOT_FIXED') DEFAULT 'PENDING',
    request_type VARCHAR(100) NOT NULL,
    technician_id INT,
    FOREIGN KEY (technician_id) REFERENCES Users(id) ON DELETE SET NULL
);

-- Insert default admin user
INSERT INTO Users (username, password, role, status) 
VALUES ('Dina', 'Dina123', 'ADMIN', 'AVAILABLE');