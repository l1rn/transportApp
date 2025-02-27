INSERT INTO users (username, password, role) VALUES
('user1', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'ROLE_USER'),
('admin', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'ROLE_ADMIN');

INSERT INTO routes (departure_city, arrival_city, transport_type, departure_time, arrival_time, price, available_seats) VALUES
('Москва', 'Санкт-Петербург', 'TRAIN', '2023-12-01 08:00:00', '2023-12-01 12:00:00', 1500, 50),
('Казань', 'Сочи', 'BUS', '2023-12-02 10:00:00', '2023-12-03 08:00:00', 2500, 30);
INSERT INTO booking (route_id, user_id, status) VALUES
(1, 1, 'CANCELED'),
(2, 2, 'CONFIRMED');