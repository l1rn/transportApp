CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS routes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    departure_city VARCHAR(255) NOT NULL,
    arrival_city VARCHAR(255) NOT NULL,
    transport_type VARCHAR(20) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    available_seats INT NOT NULL
);

CREATE TABLE IF NOT EXISTS booking(
    id INT PRIMARY KEY AUTO_INCREMENT,
    route_id INT NOT NULL,
    user_id INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (route_id) REFERENCES routes(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);