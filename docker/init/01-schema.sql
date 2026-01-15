-- Schema initialization for Java JDBC Tutorial
-- Database: skola_dev

-- Users table (from tutorial examples)
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- Contacts table (from challenge exercise)
CREATE TABLE contacts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(255)
);

-- Insert sample data for testing
INSERT INTO users (name, email) VALUES
    ('Maria Silva', 'maria@skola.dev'),
    ('João Santos', 'joao@skola.dev');
