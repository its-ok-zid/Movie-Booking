-- 1. Movie Table with EmbeddedId (movie_name + theatre_name as PK)
CREATE TABLE Movie (
    movie_name VARCHAR(255) NOT NULL,
    theatre_name VARCHAR(255) NOT NULL,
    total_tickets INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    PRIMARY KEY (movie_name, theatre_name)
);

-- 2. User Table
CREATE TABLE user_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    login_id VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    contact_number VARCHAR(20) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL
);

-- 3. Ticket Table with Embedded movie_id (movie_name + theatre_name)
CREATE TABLE Tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_name VARCHAR(255) NOT NULL,
    theatre_name VARCHAR(255) NOT NULL,
    number_of_tickets INT NOT NULL,
    seat_number VARCHAR(255) NOT NULL,
    CONSTRAINT fk_ticket_movie FOREIGN KEY (movie_name, theatre_name)
        REFERENCES Movie(movie_name, theatre_name)
);
