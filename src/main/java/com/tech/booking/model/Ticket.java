package com.tech.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MovieId movieId;

    @Column(name = "number_of_tickets", nullable = false)
    @Min(value = 1, message = "Number of tickets must be at least 1")
    private int numberOfTickets;

    @Column(name = "seat_number", nullable = false)
    @NotBlank(message = "Seat number is mandatory")
    private String seatNumber;
}
