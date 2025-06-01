-- Insert Movies (with composite key: movie_name + theatre_name)
INSERT INTO Movie (movie_name, theatre_name, total_tickets, status) VALUES
('Inception', 'PVR Cinema', 10, 'BOOK_ASAP'),
('Interstellar', 'INOX', 20, 'BOOK_ASAP'),
('The Dark Knight', 'Cinepolis', 30, 'SOLD_OUT'),
('Avengers: Endgame', 'PVR Cinema', 40, 'BOOK_ASAP'),
('Dune', 'IMAX Theatre', 50, 'SOLD_OUT');

-- Insert Users
INSERT INTO user_details (first_name, last_name, email, login_id, password, contact_number, role) VALUES
('John', 'Doe', 'john.doe@example.com', 'john123', 'password123', '9876543210', 'USER'),
('Admin', 'Smith', 'admin@example.com', 'admin001', 'adminpass', '9998887776', 'ADMIN'),
('Zid', '.', 'zid@example.com', 'Sigma', 'Zid000@', '9876543010', 'ADMIN'),
('Soham', 'Ghosal', 'soham@example.com', 'Simon', 'Sg000@', '8886543010', 'USER');

-- Insert Tickets (linking with movie_name and theatre_name)
INSERT INTO Tickets (movie_name, theatre_name, number_of_tickets, seat_number) VALUES
('Inception', 'PVR Cinema', 2, 'A1,A2'),
('Interstellar', 'INOX', 3, 'B1,B2,B3');
