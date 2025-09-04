-- src/main/resources/schema.sql
CREATE TABLE IF NOT EXISTS queue_config (
    id INT AUTO_INCREMENT PRIMARY KEY,
    queue_name VARCHAR(255) NOT NULL,
    exchange_name VARCHAR(255) NOT NULL,
    routing_key VARCHAR(255) NOT NULL
);