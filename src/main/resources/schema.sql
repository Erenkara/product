CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NULL,
    category VARCHAR(255) NULL,
    price DECIMAL(10, 2) NULL,
    description VARCHAR(1000) NULL,
    stock INT NOT NULL,
    version INT NOT NULL
);