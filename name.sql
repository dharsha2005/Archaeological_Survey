CREATE DATABASE TicketBooking;

USE TicketBooking;

CREATE TABLE cities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE monuments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    city_id INT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    childPrice DOUBLE,
    adultPrice DOUBLE,
    seniorPrice DOUBLE,
    intlChildPrice DOUBLE,
    intlAdultPrice DOUBLE,
    intlSeniorPrice DOUBLE,
    openingTime VARCHAR(255),
    closingTime VARCHAR(255),
    availableTickets INT,
    FOREIGN KEY (city_id) REFERENCES cities(id)
);
